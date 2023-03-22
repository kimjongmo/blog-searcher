package com.example.blogsearcher.presentation.api.blog

import com.example.blogsearcher.presentation.api.BindingErrorResponse
import com.example.blogsearcher.presentation.api.ErrorResponse
import com.example.blogsearcher.presentation.api.blog.dto.BlogSearchRequestDto
import com.example.blogsearcher.presentation.api.blog.dto.BlogSearchResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "블로그 검색", description = "블로그 검색 엔드포인트")
interface BlogApi {
    @Operation(summary = "검색 엔드포인트", description = "블로그 검색을 하기 위한 엔드포인트")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "검색 성공 케이스",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BlogSearchResponseDto::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "요청 데이터가 유효하지 않은 경우",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = BindingErrorResponse::class)
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
            ),
            ApiResponse(
                responseCode = "503",
                description = "현재 이용 가능한 검색 소스가 없는 경우",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class)
                    )
                ]
            )
        ]
    )
    fun searchBlog(
        @Parameter(description = "keyword=test&page=2&size=20&sort=RECENCY 와 같이 쿼리 스트링으로 요청하면 됩니다.")
        requestDto: BlogSearchRequestDto
    ): BlogSearchResponseDto?
}
