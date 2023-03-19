package com.example.blogsearcher.infra.retrofit.kakao.dto

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
    val title: String,
    val contents: String,
    val url: String,
    val blogname: String,
    val thumbnail: String,
    val datetime: LocalDateTime
)
