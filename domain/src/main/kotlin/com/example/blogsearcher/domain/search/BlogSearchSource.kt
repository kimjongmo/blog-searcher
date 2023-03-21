package com.example.blogsearcher.domain.search

import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page

/**
 * 블로그 검색 소스가 지녀야할 행위와 필드를 명시
 * */
interface BlogSearchSource {
    val searchVendor: BlogSearchVendor
    fun query(keyword: Keyword, page: Page): BlogSearchResult?
}
