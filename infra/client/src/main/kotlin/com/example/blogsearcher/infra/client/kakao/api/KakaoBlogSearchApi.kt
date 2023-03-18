package com.example.blogsearcher.infra.client.kakao.api

import com.example.blogsearcher.app.search.query.BlogSearchApi
import com.example.blogsearcher.app.search.query.BlogSearchResult
import com.example.blogsearcher.app.search.query.BlogSearchSpec
import com.example.blogsearcher.infra.client.core.handler.ApiHandler
import com.example.blogsearcher.infra.client.kakao.KakaoClient
import com.example.blogsearcher.infra.client.kakao.mapper.KakaoBlogSearchMapper

class KakaoBlogSearchApi(private val kakaoClient: KakaoClient) : BlogSearchApi {
    override fun search(spec: BlogSearchSpec): BlogSearchResult? = ApiHandler.handle(
        fn = { kakaoClient.searchBlog(params = KakaoBlogSearchMapper.toQueryMap(spec)) },
        onSuccess = { response -> KakaoBlogSearchMapper.toBlogSearchResult(response) }
    )
}
