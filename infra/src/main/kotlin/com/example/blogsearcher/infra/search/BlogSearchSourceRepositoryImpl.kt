package com.example.blogsearcher.infra.search

import com.example.blogsearcher.domain.search.BlogSearchSource
import com.example.blogsearcher.domain.search.BlogSearchSourceRepository
import org.springframework.stereotype.Service

@Service
class BlogSearchSourceRepositoryImpl(
    private val sources: List<BlogSearchSource>
) : BlogSearchSourceRepository {
    override fun findAll(): List<BlogSearchSource> {
        return sources
    }
}
