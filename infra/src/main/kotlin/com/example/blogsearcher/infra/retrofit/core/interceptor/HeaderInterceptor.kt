package com.example.blogsearcher.infra.retrofit.core.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp 요청에 헤더 정보를 세팅하기 위한 인터셉터
 * */
class HeaderInterceptor(private val headers: Map<String, String>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var requestBuilder = chain.request().newBuilder()

        headers.forEach { (headerName, value) ->
            requestBuilder = requestBuilder.header(headerName, value)
        }

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}
