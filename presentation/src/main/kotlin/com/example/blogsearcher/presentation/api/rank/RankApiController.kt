package com.example.blogsearcher.presentation.api.rank

import com.example.blogsearcher.app.rank.KeywordRankService
import com.example.blogsearcher.presentation.api.rank.dto.RankingResult
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/rank")
class RankApiController(
    private val keywordRankService: KeywordRankService
) : RankApi {
    /**
     * @param from 검색 범위 시작 yyyy-MM-dd HH:mm:ss 패턴
     * @param from 검색 범위 마지막 yyyy-MM-dd HH:mm:ss 패턴
     * */
    @GetMapping("/keyword-from-db")
    override fun getKeywordRankFromDb(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") from: LocalDateTime,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") to: LocalDateTime
    ): RankingResult {
        return RankingResult(keywordRankService.getRankFromDB(from, to))
    }

    @GetMapping("/keyword-from-cache")
    override fun getKeywordRankFromCache(
        @RequestParam rank: Int
    ): RankingResult {
        return RankingResult(keywordRankService.getRankFromCache(rank))
    }
}
