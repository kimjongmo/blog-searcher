package com.example.blogsearcher.infra.client.core.factory

import okhttp3.OkHttpClient
import retrofit2.Retrofit

abstract class AbstractRetrofitFactory<T>(
    override val properties: RetrofitProperties
) : RetrofitFactory<T> {

    abstract fun buildClient(): OkHttpClient

    abstract fun buildRetrofit(okHttpClient: OkHttpClient): Retrofit
}
