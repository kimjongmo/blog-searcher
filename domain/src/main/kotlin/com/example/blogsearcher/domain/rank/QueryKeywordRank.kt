package com.example.blogsearcher.domain.rank

import java.time.LocalDateTime

interface QueryKeywordRank {
    fun getRank(from: LocalDateTime, to: LocalDateTime): Map<String, Long>
}
