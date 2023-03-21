package com.example.blogsearcher.app.rank

import java.time.LocalDateTime

interface QueryKeywordRank {
    fun getRank(from: LocalDateTime, to: LocalDateTime): Map<String, Long>
}
