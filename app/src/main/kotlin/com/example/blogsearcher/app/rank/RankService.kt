package com.example.blogsearcher.app.rank

import com.example.blogsearcher.domain.rank.BlogSearchKeywordRank
import com.example.blogsearcher.domain.rank.query.QueryBlogSearchKeywordRank
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface RankService {
    fun getBlogKeywordRank(from: LocalDateTime, to: LocalDateTime): List<BlogSearchKeywordRank>
}

@Service
class RankServiceImpl(
    private val queryBlogSearchKeywordRank: QueryBlogSearchKeywordRank
) : RankService {
    override fun getBlogKeywordRank(from: LocalDateTime, to: LocalDateTime): List<BlogSearchKeywordRank> {
        return queryBlogSearchKeywordRank.getRank(from, to)
    }
}
