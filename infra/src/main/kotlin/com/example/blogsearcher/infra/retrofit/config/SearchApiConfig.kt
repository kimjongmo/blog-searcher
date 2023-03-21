package com.example.blogsearcher.infra.retrofit.config

import com.example.blogsearcher.infra.retrofit.core.factory.RetrofitProperties
import com.example.blogsearcher.infra.retrofit.kakao.KakaoClient
import com.example.blogsearcher.infra.retrofit.kakao.factory.KakaoClientFactory
import com.example.blogsearcher.infra.retrofit.naver.NaverClient
import com.example.blogsearcher.infra.retrofit.naver.factory.NaverClientFactory
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
    fun kakaoClient(kakaoSearchApiProperties: KakaoSearchApiProperties): KakaoClient {
        val retrofitProp = RetrofitProperties(
            baseUrl = kakaoSearchApiProperties.host,
            headers = kakaoSearchApiProperties.headers
        )

        return KakaoClientFactory(retrofitProp).create(KakaoClient::class.java)
    }

    @ConstructorBinding
    @ConfigurationProperties(prefix = "search-api.naver")
    class NaverSearchApiProperties(
        host: String,
        headers: Map<String, String>
    ) : SearchApiProperties(host, headers)

    @Bean
    fun naverClient(naverSearchApiProperties: NaverSearchApiProperties): NaverClient {
        val retrofitProp = RetrofitProperties(
            baseUrl = naverSearchApiProperties.host,
            headers = naverSearchApiProperties.headers
        )

        return NaverClientFactory(retrofitProp).create(NaverClient::class.java)
    }
}
