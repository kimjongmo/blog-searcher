package com.example.blogsearcher.app.search

import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSource
import com.example.blogsearcher.domain.search.BlogSearchVendor
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page

class MockKakaoBlogSearchSource : BlogSearchSource {
    override val searchVendor = BlogSearchVendor.KAKAO
    override fun query(keyword: Keyword, page: Page): BlogSearchResult? = null
}
