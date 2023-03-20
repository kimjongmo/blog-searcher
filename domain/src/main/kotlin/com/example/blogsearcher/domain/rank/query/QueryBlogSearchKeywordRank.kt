package com.example.blogsearcher.domain.rank.query

import com.example.blogsearcher.domain.rank.BlogSearchKeywordRank
import java.time.LocalDateTime

interface QueryBlogSearchKeywordRank {
    fun getRank(from: LocalDateTime, to: LocalDateTime): List<BlogSearchKeywordRank>
}
