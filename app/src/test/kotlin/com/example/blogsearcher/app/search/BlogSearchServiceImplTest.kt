package com.example.blogsearcher.app.search

import com.example.blogsearcher.app.common.CircuitBreakerRegister
import com.example.blogsearcher.app.common.EventPublisher
import com.example.blogsearcher.domain.search.BlogSearchItem
import com.example.blogsearcher.domain.search.BlogSearchPage
import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSource
import com.example.blogsearcher.domain.search.BlogSearchSourceRepository
import com.example.blogsearcher.domain.search.BlogSearchVendor
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.never
import org.mockito.BDDMockito.spy
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.util.function.Supplier

internal class BlogSearchServiceImplTest {
    private val blogSearchSourceRepository: BlogSearchSourceRepository = mock { }
    private val eventPublisher: EventPublisher = mock { }
    private val circuitBreakerRegister: CircuitBreakerRegister = mock { }
    private val kakaoMockSource: BlogSearchSource = mock {
        given(it.searchVendor).willReturn(BlogSearchVendor.KAKAO)
    }
    private val naverMockSource: BlogSearchSource = mock {
        given(it.searchVendor).willReturn(BlogSearchVendor.NAVER)
    }
    private val kakaoCircuitBreaker: CircuitBreaker = mock { circuitBreaker ->
        given(circuitBreaker.executeSupplier(any<Supplier<BlogSearchResult?>>())).will {
            val arg = it.arguments.first() as Supplier<BlogSearchResult?>
            return@will arg.get()
        }
    }
    private val naverCircuitBreaker: CircuitBreaker = mock { circuitBreaker ->
        given(circuitBreaker.executeSupplier(any<Supplier<BlogSearchResult?>>())).will {
            val arg = it.arguments.first() as Supplier<BlogSearchResult?>
            return@will arg.get()
        }
    }

    private lateinit var blogSearchService: BlogSearchService

    @BeforeEach
    fun init() {
        given(blogSearchSourceRepository.findAll()).willReturn(listOf(kakaoMockSource, naverMockSource))
        given(circuitBreakerRegister.register(eq(kakaoMockSource.searchVendor.name))).willReturn(kakaoCircuitBreaker)
        given(circuitBreakerRegister.register(eq(naverMockSource.searchVendor.name))).willReturn(naverCircuitBreaker)
        given(kakaoCircuitBreaker.state).willReturn(CircuitBreaker.State.CLOSED)
        given(naverCircuitBreaker.state).willReturn(CircuitBreaker.State.CLOSED)

        blogSearchService = spy(BlogSearchServiceImpl(blogSearchSourceRepository, eventPublisher, circuitBreakerRegister))

        // 기본적으로 생성시 검색 소스 조회 및 circuit breaker를 등록한다.
        verify(blogSearchSourceRepository, times(1)).findAll()
        verify(circuitBreakerRegister, times(2)).register(any())
    }

    @Test
    fun `이용 가능한 검색 소스가 없는 경우 예외를 발생시킨다`() {
        // given
        given(kakaoCircuitBreaker.state).willReturn(CircuitBreaker.State.OPEN)
        given(naverCircuitBreaker.state).willReturn(CircuitBreaker.State.OPEN)

        val keyword = Keyword("검색")
        val page = Page(1, 50, Sorting.ACCURACY)

        assertThrows<NotFoundSearchSourceException> {
            try {
                // when
                blogSearchService.search(keyword, page)
            } finally {
                // then
                verify(kakaoMockSource, never()).query(eq(keyword), eq(page))
                verify(naverMockSource, never()).query(eq(keyword), eq(page))
                verify(eventPublisher, never()).publish(anyOrNull())
            }
        }
    }

    @Test
    fun `검색이 성공하면 검색 이벤트를 발행시킨다`() {
        // given
        doNothing().`when`(eventPublisher).publish(anyOrNull())

        val keyword = Keyword("검색")
        val page = Page(1, 50, Sorting.ACCURACY)
        given(kakaoMockSource.query(eq(keyword), eq(page))).willReturn(
            BlogSearchResult(
                BlogSearchPage(1, 1, 50, 1),
                listOf(BlogSearchItem("제목", "내용", "블로그", postdate = LocalDate.now(), url = "url"))
            )
        )
        given(naverMockSource.query(eq(keyword), eq(page))).willReturn(
            BlogSearchResult(
                BlogSearchPage(1, 1, 50, 1),
                listOf(BlogSearchItem("제목", "내용", "블로그", postdate = LocalDate.now(), url = "url"))
            )
        )

        // when
        val result = blogSearchService.search(keyword, page)

        // then
        assertNotNull(result)

        verify(eventPublisher, times(1)).publish(anyOrNull())
    }

    @Test
    fun `처음 검색이 실패시 fallback 검색시도`() {
        // given
        doNothing().`when`(eventPublisher).publish(anyOrNull())

        val keyword = Keyword("검색")
        val page = Page(1, 50, Sorting.ACCURACY)

        given(kakaoMockSource.query(eq(keyword), eq(page))).willThrow(IllegalArgumentException("검색 실패"))
        given(naverMockSource.query(eq(keyword), eq(page))).willReturn(
            BlogSearchResult(
                BlogSearchPage(1, 1, 50, 1),
                listOf(BlogSearchItem("제목", "내용", "블로그", postdate = LocalDate.now(), url = "url"))
            )
        )

        // when
        val result = blogSearchService.search(keyword, page)

        // then
        assertNotNull(result)

        verify(kakaoMockSource, times(1)).query(eq(keyword), eq(page))
        verify(naverMockSource, times(1)).query(eq(keyword), eq(page))
        verify(eventPublisher, times(1)).publish(anyOrNull())
    }

    @Test
    fun `모든 검색 소스에서 검색이 실패했을 경우 null 반환`() {
        // given
        val keyword = Keyword("검색")
        val page = Page(1, 50, Sorting.ACCURACY)

        given(kakaoMockSource.query(eq(keyword), eq(page))).willThrow(IllegalArgumentException("검색 실패"))
        given(naverMockSource.query(eq(keyword), eq(page))).willThrow(IllegalArgumentException("검색 실패"))

        // when
        val result = blogSearchService.search(keyword, page)

        // then
        assertNull(result)
        verify(kakaoMockSource, times(1)).query(eq(keyword), eq(page))
        verify(naverMockSource, times(1)).query(eq(keyword), eq(page))
        verify(eventPublisher, never()).publish(anyOrNull())
    }
}
