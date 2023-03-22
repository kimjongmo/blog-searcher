package com.example.blogsearcher.presentation.api.rank.dto

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

class RankingResult private constructor(
    @field:ArraySchema(
        schema = Schema(
            description = "키워드 정보",
            implementation = KeywordInfo::class
        ),
        arraySchema = Schema(
            description = "키워드 정보 리스트"
        )
    )
    val keywordList: List<KeywordInfo>
) {
    constructor(rank: Map<String, Long>) : this(
        keywordList = rank.map { entry -> KeywordInfo(keyword = entry.key, count = entry.value) }
    )

    class KeywordInfo(
        @get:Schema(example = "테스트", description = "키워드")
        val keyword: String,
        @get:Schema(example = "12345", description = "키워드가 검색된 횟수")
        val count: Long
    )
}
