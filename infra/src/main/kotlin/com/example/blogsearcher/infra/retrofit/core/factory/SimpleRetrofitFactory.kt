package com.example.blogsearcher.infra.retrofit.core.factory

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

abstract class SimpleRetrofitFactory<T>(properties: RetrofitProperties) : AbstractRetrofitFactory<T>(properties) {

    override fun buildClient(): OkHttpClient {
        var builder = OkHttpClient.Builder()

        initInterceptors().forEach {
            builder = builder.addInterceptor(it)
        }

        return builder
            .connectTimeout(properties.connectTimeout, TimeUnit.SECONDS)
            .readTimeout(properties.readTimeout, TimeUnit.SECONDS)
            .build()
    }

    override fun buildRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(properties.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(createConverter())
            .build()
    }

    override fun create(clazz: Class<T>): T {
        val okHttpBuilder = OkHttpClient.Builder()
        initInterceptors().forEach {
            okHttpBuilder.addInterceptor(it)
        }
        okHttpBuilder
            .connectTimeout(properties.connectTimeout, TimeUnit.SECONDS)
            .readTimeout(properties.readTimeout, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(properties.baseUrl)
            .client(okHttpBuilder.build())
            .addConverterFactory(createConverter())
            .build()
            .create(clazz)

        return buildRetrofit(buildClient()).create(clazz)
    }

    protected open fun createConverter(): Converter.Factory {
//        return JacksonConverterFactory.create(ObjectMapper().registerModules(*initJacksonModules()).also {
//            it.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
//        })
        return JacksonConverterFactory.create(initObjectMapper())
    }

    abstract fun initObjectMapper(): ObjectMapper

    abstract fun initInterceptors(): List<Interceptor>
}
