package com.example.blogsearcher.domain.rank.query

import java.time.LocalDateTime

interface QueryBlogSearchKeywordRank {
    fun getRank(from: LocalDateTime, to: LocalDateTime): Map<String, Int>
}
