package com.example.blogsearcher.presentation.api.rank

import com.example.blogsearcher.presentation.api.ErrorResponse
import com.example.blogsearcher.presentation.api.rank.dto.RankingResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import java.time.LocalDateTime

@Tag(name = "키워드 랭킹", description = "키워드 랭킹 조회 엔드포인트")
interface RankApi {
    @Operation(summary = "키워드 상위 10개", description = "데이터베이스에 저장된 히스토리를 기준으로 랭킹을 리턴")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공 케이스",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = RankingResult::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500",
                description = "캐치되지 못한 오류",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class)
                    )
                ]
            )
        ]
    )
    fun getKeywordRankFromDb(
        @Parameter(example = "2023-03-22 00:00:00", description = "검색 범위 시작. pattern = yyyy-MM-dd HH:mm:ss", required = true)
        from: LocalDateTime,
        @Parameter(example = "2023-04-01 00:00:00", description = "검색 범위 마지막. pattern = yyyy-MM-dd HH:mm:ss", required = true)
        to: LocalDateTime
    ): RankingResult

    @Operation(summary = "키워드 상위 10개", description = "캐시에 저장된 랭킹 리턴.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "성공 케이스",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = RankingResult::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500",
                description = "캐치되지 못한 오류",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class)
                    )
                ]
            )
        ]
    )
    fun getKeywordRankFromCache(
        @Parameter(example = "10", description = "지정한 랭킹 순위 조회 ", required = true)
        rank: Int
    ): RankingResult
}
