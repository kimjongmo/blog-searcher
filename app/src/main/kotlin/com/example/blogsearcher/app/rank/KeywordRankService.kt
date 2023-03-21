package com.example.blogsearcher.app.rank

import com.example.blogsearcher.domain.rank.QueryKeywordRank
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface KeywordRankService {
    /**
     * 데이터베이스에 저장된 검색 기록을 기반으로 키워드 랭킹을 리턴한다
     * @param from 검색 시작 시간
     * @param to 검색 끝 시간
     * @return <키워드, 검색 횟수> 리턴
     * */
    fun getRankFromDB(from: LocalDateTime, to: LocalDateTime): Map<String, Long>

    /**
     * 캐시에 저장된 키워드 랭킹을 리턴한다
     * @param rank 상위 몇 개를 뽑을지 결정
     * @return <키워드, 검색 횟수> 리턴
     * */
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
