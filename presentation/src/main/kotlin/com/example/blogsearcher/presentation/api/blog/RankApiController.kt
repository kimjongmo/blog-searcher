package com.example.blogsearcher.presentation.api.blog

import com.example.blogsearcher.app.rank.RankService
import com.example.blogsearcher.domain.rank.BlogSearchKeywordRank
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/rank")
class RankApiController(
    private val rankService: RankService
) {
    @GetMapping("/blog/keyword")
    fun getBlogKeywordRank(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") from: LocalDateTime,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") to: LocalDateTime
    ): List<BlogSearchKeywordRank> {
        return rankService.getBlogKeywordRank(from, to)
    }
}
