package com.example.blogsearcher.app.search

import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSource
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import io.github.resilience4j.circuitbreaker.CircuitBreaker

/**
 * 검색시 서킷브레이커로 감싸서 요청이 나가도록 하기 위해 만든 클래스
 * */
class SearchSourceAdapter(
    val searchSource: BlogSearchSource,
    val circuitBreaker: CircuitBreaker
) : BlogSearchSource {
    override val searchVendor = searchSource.searchVendor

    /**
     * 서킷의 상태가 CLOSED, HALF_OPEN 인 경우에만 이용 가능으로 판단한다.
     * */
    val available: Boolean
        get() = circuitBreaker.state in listOf(CircuitBreaker.State.HALF_OPEN, CircuitBreaker.State.CLOSED)

    /**
     * 서킷브레이커를 감싼 상태에서 요청이 나가도록 한다.
     * */
    override fun query(keyword: Keyword, page: Page): BlogSearchResult? {
        return circuitBreaker.executeSupplier { searchSource.query(keyword, page) }
    }
}
