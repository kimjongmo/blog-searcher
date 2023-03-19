package com.example.blogsearcher.app.search.query

interface BlogSearchApi {
    fun search(spec: BlogSearchSpec): BlogSearchResult?
}
