package com.example.blogsearcher.app.rank

import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface KeywordRankService {
    fun getRankFromDB(from: LocalDateTime, to: LocalDateTime): Map<String, Long>
    fun getRankFromCache(rank: Int): Map<String, Long>
}

@Service
class KeywordRankServiceImpl(
    private val queryKeywordRank: QueryKeywordRank,
    private val keywordRankCalculator: KeywordRankCalculator
) : KeywordRankService {

    override fun getRankFromDB(from: LocalDateTime, to: LocalDateTime): Map<String, Long> {
        return queryKeywordRank.getRank(from, to)
    }

    override fun getRankFromCache(rank: Int): Map<String, Long> {
        return keywordRankCalculator.getTop(rank)
    }
}
