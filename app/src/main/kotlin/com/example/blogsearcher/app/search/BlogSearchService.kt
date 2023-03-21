package com.example.blogsearcher.app.search

import com.example.blogsearcher.app.common.CircuitBreakerRegister
import com.example.blogsearcher.app.common.EventPublisher
import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSource
import com.example.blogsearcher.domain.search.BlogSearchSourceRepository
import com.example.blogsearcher.domain.search.BlogSearchVendor
import com.example.blogsearcher.domain.search.event.BlogSearchEvent
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private val log = KotlinLogging.logger { }

interface BlogSearchService {
    /**
     * 블로그 검색
     * @param keyword 검색 키워드
     * @param page 검색 페이지 정보
     * @return 검색 결과
     * @throws NotFoundSearchSourceException 검색 소스를 가져오지 못한 경우 발생
     * */
    fun search(keyword: Keyword, page: Page): BlogSearchResult?
}

@Service
class BlogSearchServiceImpl(
    blogSearchSourceRepository: BlogSearchSourceRepository,
    private val eventPublisher: EventPublisher,
    private val circuitBreakerRegister: CircuitBreakerRegister
) : BlogSearchService {

    private lateinit var searchSourceList: List<SearchSourceAdapter>

    init {
        /**
         * 클래스 초기화시 블로그 검색 소스를 모두 가져온다
         * 검색 소스마다 서킷브레이커를 생성하고 이것을 이용해서 검색할 수 있도록 adapter 클래스에 담아 저장해놓는다
         * */
        searchSourceList = blogSearchSourceRepository.findAll().map { searchSource ->
            val circuitBreaker = circuitBreakerRegister.register(searchSource.searchVendor.name)
            SearchSourceAdapter(searchSource = searchSource, circuitBreaker = circuitBreaker)
        }
    }

    override fun search(keyword: Keyword, page: Page): BlogSearchResult? {
        val searchSource = getAvailableSource() ?: throw NotFoundSearchSourceException()

        return runCatching {
            searchSource.query(keyword, page)
        }.onSuccess {
            eventPublisher.publish(BlogSearchEvent(searchSource.searchVendor, keyword, LocalDateTime.now()))
        }.getOrElse {
            log.warn("검색에 실패하였습니다. vendor = ${searchSource.searchVendor}, message = ${it.message}")
            fallback(keyword, page, searchSource.searchVendor)
        }
    }

    /**
     * 검색 실패시 다른 검색 소스로도 시도해본다
     * */
    private fun fallback(keyword: Keyword, page: Page, vendor: BlogSearchVendor): BlogSearchResult? {
        val fallbackSearchSource = nextAvailableSource(vendor) ?: return null

        return runCatching {
            fallbackSearchSource.query(keyword, page)
        }.onSuccess {
            eventPublisher.publish(BlogSearchEvent(fallbackSearchSource.searchVendor, keyword, LocalDateTime.now()))
        }.getOrElse {
            log.warn("검색에 실패하였습니다. vendor = ${fallbackSearchSource.searchVendor}, message = ${it.message}")
            null
        }
    }

    /**
     * 이용 가능한 검색 소스 리턴.
     * */
    private fun getAvailableSource(): BlogSearchSource? {
        return searchSourceList.find { it.available }
    }

    /**
     * [currVendor]가 아니면서 이용 가능한 검색 소스 리턴.
     * */
    private fun nextAvailableSource(currVendor: BlogSearchVendor): BlogSearchSource? {
        return searchSourceList.find { it.available && it.searchVendor != currVendor }
    }
}
