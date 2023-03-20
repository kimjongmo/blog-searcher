package com.example.blogsearcher.infra.search

import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSource
import com.example.blogsearcher.domain.search.BlogSearchVendor
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.infra.retrofit.core.handler.ApiHandler
import com.example.blogsearcher.infra.retrofit.naver.NaverClient
import com.example.blogsearcher.infra.retrofit.naver.mapper.NaverBlogSearchMapper
import org.springframework.stereotype.Service

@Service
class NaverBlogSearch(private val naverClient: NaverClient) : BlogSearchSource {
    override val searchVendor = BlogSearchVendor.NAVER

    override fun query(keyword: Keyword, page: Page): BlogSearchResult? = ApiHandler.handle(
        fn = { naverClient.searchBlog(params = NaverBlogSearchMapper.toQueryMap(keyword, page)) },
        onSuccess = { response -> NaverBlogSearchMapper.toBlogSearchResult(response, page) }
    )
}
