package com.example.blogsearcher.infra.retrofit.core.interceptor

import okhttp3.Interceptor
import okhttp3.Response

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
