package com.example.blogsearcher.domain.search

import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page

interface BlogSearchSource {
    fun query(keyword: Keyword, page: Page): BlogSearchResult?
}
