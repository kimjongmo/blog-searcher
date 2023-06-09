package com.example.blogsearcher.infra.retrofit.kakao

import com.example.blogsearcher.infra.retrofit.kakao.dto.KakaoBlogSearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface KakaoClient {

    @GET("/v2/search/blog")
    fun searchBlog(
        @QueryMap params: Map<String, String>
    ): Call<KakaoBlogSearchResult>
}
