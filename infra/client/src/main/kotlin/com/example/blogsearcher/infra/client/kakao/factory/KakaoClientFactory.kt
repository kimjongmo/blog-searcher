package com.example.blogsearcher.infra.client.kakao.factory

import com.example.blogsearcher.infra.client.core.factory.RetrofitProperties
import com.example.blogsearcher.infra.client.core.factory.SimpleRetrofitFactory
import com.example.blogsearcher.infra.client.core.interceptor.HeaderInterceptor
import com.example.blogsearcher.infra.client.core.interceptor.RetryInterceptor
import com.example.blogsearcher.infra.client.kakao.KakaoClient
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class KakaoClientFactory(properties: RetrofitProperties) : SimpleRetrofitFactory<KakaoClient>(properties) {
    override fun initInterceptors(): List<Interceptor> {
        val loggingInterceptor = HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.valueOf(properties.loggingLevel) }
        val headerInterceptor = HeaderInterceptor(properties.headers)
        return listOf(RetryInterceptor(), loggingInterceptor, headerInterceptor)
    }

    override fun initObjectMapper(): ObjectMapper {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

        val timeModule = JavaTimeModule().addDeserializer(
            LocalDateTime::class.java,
            object : JsonDeserializer<LocalDateTime>() {
                override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime {
                    val node = p.codec.readTree<JsonNode>(p)
                    val str = node.textValue()
                    return LocalDateTime.parse(str, formatter)
                }
            }
        )

        return ObjectMapper()
            .registerModules(timeModule, SimpleModule(), KotlinModule.Builder().build())
            .also { it.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE }
    }
}
