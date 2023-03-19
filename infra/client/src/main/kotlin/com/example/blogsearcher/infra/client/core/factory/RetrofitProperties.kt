package com.example.blogsearcher.infra.client.core.factory

data class RetrofitProperties(
    val baseUrl: String,
    val connectTimeout: Long = 5L,
    val readTimeout: Long = 5L,
    val headers: Map<String, String>,
    val loggingLevel: String = "BASIC"
)
