package com.example.blogsearcher.app.rank

import com.example.blogsearcher.domain.rank.QueryKeywordRank
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface KeywordRankService {
    fun getRankFromDB(from: LocalDateTime, to: LocalDateTime): Map<String, Long>
    fun getRankFromCache(rank: Int): Map<String, Long>
}

@Service
class KeywordRankServiceImpl(
    private val queryKeywordRank: QueryKeywordRank,
    private val cache: LoadingCache<String, Long>
) : KeywordRankService {

    override fun getRankFromDB(from: LocalDateTime, to: LocalDateTime): Map<String, Long> {
        return queryKeywordRank.getRank(from, to)
    }

    override fun getRankFromCache(rank: Int): Map<String, Long> {
        return cache.asMap()
            .toList()
            .sortedByDescending { it.second }
            .take(rank)
            .associate { it.first to it.second }
    }
}
