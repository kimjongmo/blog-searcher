package com.example.blogsearcher.app.search

import com.example.blogsearcher.app.common.EventPublisher
import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSourceRepository
import com.example.blogsearcher.domain.search.event.BlogSearchEvent
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface BlogSearchService {
    fun search(keyword: Keyword, page: Page): BlogSearchResult?
}

@Service
class BlogSearchServiceImpl(
    private val blogSearchSourceRepository: BlogSearchSourceRepository,
    private val eventPublisher: EventPublisher
) : BlogSearchService {
    override fun search(keyword: Keyword, page: Page): BlogSearchResult? {
        val sources = blogSearchSourceRepository.findAll()
        val selectedSource = sources.first()
        return selectedSource.query(keyword, page).also {
            eventPublisher.publish(BlogSearchEvent(selectedSource.searchVendor, keyword, LocalDateTime.now()))
        }
    }
}
