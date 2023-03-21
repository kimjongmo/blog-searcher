package com.example.blogsearcher.app.rank

import com.example.blogsearcher.app.config.CacheConfig
import com.example.blogsearcher.domain.rank.BlogSearchKeywordRank
import com.example.blogsearcher.domain.rank.query.QueryBlogSearchKeywordRank
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface RankService {
    fun getKeywordRankFromDB(from: LocalDateTime, to: LocalDateTime): Map<String, Int>
    fun getKeywordRankFromCache(rank: Int): Map<String, Int>
}

@Service
class RankServiceImpl(
    private val queryBlogSearchKeywordRank: QueryBlogSearchKeywordRank,
    private val keywordRankCalculator: KeywordRankCalculator
) : RankService {

    override fun getKeywordRankFromDB(from: LocalDateTime, to: LocalDateTime): Map<String, Int> {
        return queryBlogSearchKeywordRank.getRank(from, to)
    }

    override fun getKeywordRankFromCache(rank: Int): Map<String, Int> {
        return keywordRankCalculator.getTop(rank)
    }
}
