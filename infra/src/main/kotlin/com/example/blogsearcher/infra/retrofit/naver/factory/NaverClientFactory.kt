package com.example.blogsearcher.infra.retrofit.naver.factory

import com.example.blogsearcher.infra.retrofit.core.factory.RetrofitProperties
import com.example.blogsearcher.infra.retrofit.core.factory.SimpleRetrofitFactory
import com.example.blogsearcher.infra.retrofit.core.interceptor.HeaderInterceptor
import com.example.blogsearcher.infra.retrofit.core.interceptor.RetryInterceptor
import com.example.blogsearcher.infra.retrofit.naver.NaverClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.time.LocalDate

class NaverClientFactory(properties: RetrofitProperties) : SimpleRetrofitFactory<NaverClient>(properties) {
    override fun initInterceptors(): List<Interceptor> {
        val loggingInterceptor = HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.valueOf(properties.loggingLevel) }
        val headerInterceptor = HeaderInterceptor(properties.headers)
        return listOf(RetryInterceptor(), loggingInterceptor, headerInterceptor)
    }

    override fun initObjectMapper(): ObjectMapper {
        val timeModule = JavaTimeModule().apply {
            addDeserializer(LocalDate::class.java, LocalDateDeserializer())
        }
        return ObjectMapper().registerModules(timeModule, SimpleModule(), KotlinModule.Builder().build())
    }
}
