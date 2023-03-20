package com.example.blogsearcher.infra.retrofit.naver

import com.example.blogsearcher.infra.retrofit.naver.dto.NaverBlogSearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NaverClient {
    @GET("/v1/search/blog.json")
    fun searchBlog(
        @QueryMap params: Map<String, String>
    ): Call<NaverBlogSearchResult>
}
