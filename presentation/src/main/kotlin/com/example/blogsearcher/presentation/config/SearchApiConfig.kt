package com.example.blogsearcher.presentation.config

import com.example.blogsearcher.infra.client.core.factory.RetrofitProperties
import com.example.blogsearcher.infra.client.kakao.KakaoClient
import com.example.blogsearcher.infra.client.kakao.api.KakaoBlogSearchApi
import com.example.blogsearcher.infra.client.kakao.factory.KakaoClientFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan
class SearchApiConfig {
    open class SearchApiProperties(
        val host: String,
        val headers: Map<String, String>
    )

    @ConstructorBinding
    @ConfigurationProperties(prefix = "search-api.kakao")
    class KakaoSearchApiProperties(
        host: String,
        headers: Map<String, String>
    ) : SearchApiProperties(host, headers)

    @Bean
    fun kakaoBlogSearchApi(kakaoSearchApiProperties: KakaoSearchApiProperties): KakaoBlogSearchApi {
        val retrofitProp = RetrofitProperties(
            baseUrl = kakaoSearchApiProperties.host,
            headers = kakaoSearchApiProperties.headers
        )

        val kakaoClient = KakaoClientFactory(retrofitProp).create(KakaoClient::class.java)

        return KakaoBlogSearchApi(kakaoClient)
    }
}
