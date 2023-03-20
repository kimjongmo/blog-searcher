package com.example.blogsearcher.app.search

import com.example.blogsearcher.app.common.EventPublisher
import com.example.blogsearcher.domain.search.BlogSearchItem
import com.example.blogsearcher.domain.search.BlogSearchPage
import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSourceRepository
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.never
import org.mockito.BDDMockito.spy
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.lang.IllegalArgumentException
import java.time.LocalDate

internal class BlogSearchServiceImplTest {
    private val blogSearchSourceRepository: BlogSearchSourceRepository = mock { }
    private val eventPublisher: EventPublisher = mock { }
    private val blogSearchService = BlogSearchServiceImpl(blogSearchSourceRepository, eventPublisher)
    private val mockSource = spy(MockKakaoBlogSearchSource())

    @Test
    fun `검색 소스가 없는 경우 검색을 하지 않고 예외를 발생시킨다`() {
        // given
        val keyword = Keyword("검색")
        val page = Page(1, 50, Sorting.ACCURACY)
        given(blogSearchSourceRepository.findAll()).willReturn(emptyList())

        assertThrows<NotFoundSearchSourceException> {
            try {
                // when
                blogSearchService.search(keyword, page)
            } finally {
                // then
                verify(blogSearchSourceRepository, times(1)).findAll()
                verify(mockSource, never()).query(eq(keyword), eq(page))
                verify(eventPublisher, never()).publish(anyOrNull())
            }
        }
    }

    @Test
    fun `검색이 성공하면 검색 이벤트를 발행시킨다`() {
        // given
        val keyword = Keyword("검색")
        val page = Page(1, 50, Sorting.ACCURACY)

        val searchResult = BlogSearchResult(
            BlogSearchPage(1, 1, 50, 1),
            listOf(BlogSearchItem("제목", "내용", "블로그", postdate = LocalDate.now(), url = "url"))
        )

        given(mockSource.query(eq(keyword), eq(page))).willReturn(searchResult)
        given(blogSearchSourceRepository.findAll()).willReturn(listOf(mockSource))
        doNothing().`when`(eventPublisher).publish(anyOrNull())

        // when
        val result = blogSearchService.search(keyword, page)

        // then
        assertNotNull(result)
        verify(eventPublisher, times(1)).publish(anyOrNull())
    }

    @Test
    fun `검색 중간 오류가 발생하면 null을 리턴하도록 한다`() {
        // given
        val keyword = Keyword("검색")
        val page = Page(1, 50, Sorting.ACCURACY)

        given(mockSource.query(eq(keyword), eq(page))).willThrow(IllegalArgumentException())
        given(blogSearchSourceRepository.findAll()).willReturn(listOf(mockSource))

        // when
        val result = blogSearchService.search(keyword, page)

        // then
        assertNull(result)
        verify(blogSearchSourceRepository, times(1)).findAll()
        verify(mockSource, times(1)).query(eq(keyword), eq(page))
        verify(eventPublisher, never()).publish(anyOrNull())
    }
}
