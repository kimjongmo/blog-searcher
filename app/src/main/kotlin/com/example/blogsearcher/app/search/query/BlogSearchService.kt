package com.example.blogsearcher.app.search.query

import org.springframework.stereotype.Service

interface BlogSearchService {
    fun search(spec: BlogSearchSpec): BlogSearchResult?
}

@Service
class BlogSearchServiceImpl(
    private val blogSearchApi: BlogSearchApi
) : BlogSearchService {
    override fun search(spec: BlogSearchSpec): BlogSearchResult? {
        return blogSearchApi.search(spec)
    }
}
