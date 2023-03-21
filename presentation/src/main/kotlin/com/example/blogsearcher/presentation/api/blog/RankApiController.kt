package com.example.blogsearcher.presentation.api.blog

import com.example.blogsearcher.app.rank.KeywordRankService
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
) {
    @GetMapping("/keyword-from-db")
    fun getKeywordRankFromDb(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") from: LocalDateTime,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") to: LocalDateTime
    ): Map<String, Long> {
        return keywordRankService.getRankFromDB(from, to)
    }

    @GetMapping("/keyword-from-cache")
    fun getKeywordRankFromCache(
        rank: Int = 10
    ): Map<String, Long> {
        return keywordRankService.getRankFromCache(rank)
    }
}
