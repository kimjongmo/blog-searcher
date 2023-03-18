package com.example.blogsearcher.infra.client.kakao.dto

import java.time.LocalDateTime

class KakaoBlogSearchResult(
    val meta: Meta,
    val documents: List<Document>
)

class Meta(
    val isEnd: Boolean,
    val pageableCount: Int,
    val totalCount: Int
)

class Document(
    val contents: String,
    val datetime: LocalDateTime,
    val title: String,
    val url: String
)
