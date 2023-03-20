package com.example.blogsearcher.presentation.api.config

import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.KeywordJsonDeserializer
import com.example.blogsearcher.domain.search.vo.KeywordJsonSerializer
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class WebConfig {
    @Bean
    fun jsonCustomizer(): Jackson2ObjectMapperBuilderCustomizer? {
        return Jackson2ObjectMapperBuilderCustomizer { builder: Jackson2ObjectMapperBuilder ->
            builder
                .serializerByType(Keyword::class.java, KeywordJsonSerializer())
                .deserializerByType(Keyword::class.java, KeywordJsonDeserializer())
        }
    }
}
