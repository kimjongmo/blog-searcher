package com.example.blogsearcher.app.search

import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSource
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import io.github.resilience4j.circuitbreaker.CircuitBreaker

class SearchSourceAdapter(
    val searchSource: BlogSearchSource,
    val circuitBreaker: CircuitBreaker
) : BlogSearchSource {
    override val searchVendor = searchSource.searchVendor

    val available: Boolean
        get() = circuitBreaker.state in listOf(CircuitBreaker.State.HALF_OPEN, CircuitBreaker.State.CLOSED)

    override fun query(keyword: Keyword, page: Page): BlogSearchResult? {
        return circuitBreaker.executeSupplier { searchSource.query(keyword, page) }
    }
}
