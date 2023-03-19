package com.example.blogsearcher.app.search.query

import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSourceRepository
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import org.springframework.stereotype.Service

interface BlogSearchService {
    fun search(keyword: Keyword, page: Page): BlogSearchResult?
}

@Service
class BlogSearchServiceImpl(
    private val blogSearchSourceRepository: BlogSearchSourceRepository
) : BlogSearchService {
    override fun search(keyword: Keyword, page: Page): BlogSearchResult? {
        val sources = blogSearchSourceRepository.findAll()
        return sources.first().query(keyword, page)
    }
}
