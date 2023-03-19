package com.example.blogsearcher.infra.search

import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.BlogSearchSource
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.infra.retrofit.core.handler.ApiHandler
import com.example.blogsearcher.infra.retrofit.kakao.KakaoClient
import com.example.blogsearcher.infra.retrofit.kakao.mapper.KakaoBlogSearchMapper
import org.springframework.stereotype.Service

@Service
class KakaoBlogSearch(private val kakaoClient: KakaoClient) : BlogSearchSource {
    override fun query(keyword: Keyword, page: Page): BlogSearchResult? = ApiHandler.handle(
        fn = { kakaoClient.searchBlog(params = KakaoBlogSearchMapper.toQueryMap(keyword, page)) },
        onSuccess = { response -> KakaoBlogSearchMapper.toBlogSearchResult(response, page) }
    )
}
