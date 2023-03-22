package com.example.blogsearcher.presentation.api.blog.dto

import com.example.blogsearcher.domain.search.BlogSearchItem
import com.example.blogsearcher.domain.search.BlogSearchPage
import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.presentation.api.validation.ValueOfEnum
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

class BlogSearchRequestDto(
    @get:Schema(example = "테스트", description = "검색 키워드 입력")
    @field:NotBlank(message = "검색어를 입력해주세요")
    val keyword: String,

    @get:Schema(example = "5", description = "페이지 번호 입력")
    @field:Min(value = 1, message = "페이지는 1 ~ 50 사이의 값이어야 합니다")
    @field:Max(value = 50, message = "페이지는 1 ~ 50 사이의 값이어야 합니다")
    val page: Int = 1,

    @get:Schema(example = "10", description = "페이지에 노출할 글 갯수")
    @field:Min(value = 1, message = "최소 1이상 설정 가능합니다.")
    @field:Max(value = 50, message = "최대 50까지만 설정 가능합니다.")
    val size: Int = 10,

    @get:Schema(example = "ACCURACY", description = "정렬 옵션 ACCURACY: 정확도순, RECENCY: 최신순")
    @field:ValueOfEnum(Sorting::class)
    val sort: String
)

class BlogSearchResponseDto private constructor(
    @get:Schema(description = "검색된 페이지 정보", implementation = BlogSearchPageDto::class)
    val page: BlogSearchPageDto,
    @get:ArraySchema(
        schema = Schema(
            description = "검색된 글 정보",
            implementation = BlogSearchItemDto::class
        ),
        arraySchema = Schema(
            description = "글 리스트"
        )
    )
    val items: List<BlogSearchItemDto>
) {
    constructor(result: BlogSearchResult) : this(
        page = BlogSearchPageDto(result.page),
        items = result.items.map { BlogSearchItemDto(it) }
    )
}

class BlogSearchPageDto(
    @get:Schema(example = "12345", description = "전체 검색된 글 갯수")
    val total: Int,
    @get:Schema(example = "2", description = "현재 페이지 번호")
    val pageNo: Int,
    @get:Schema(example = "20", description = "페이지 글 노출 갯수")
    val sizePerPage: Int,
    @get:Schema(example = "15", description = "실제 검색된 글 갯수")
    val resultCnt: Int
) {
    constructor(page: BlogSearchPage) : this(
        total = page.total,
        pageNo = page.pageNo,
        sizePerPage = page.sizePerPage,
        resultCnt = page.resultCnt
    )
}

class BlogSearchItemDto(
    @get:Schema(example = "제목", description = "글 제목")
    val title: String,
    @get:Schema(example = "본문", description = "글 요약")
    val content: String,
    @get:Schema(example = "블로그 이름", description = "블로그 이름")
    val blogname: String,
    @get:Schema(example = "게시 날짜", description = "게시 날짜")
    val postdate: LocalDate,
    @get:Schema(example = "http://localhost:8888", description = "글 링크")
    val url: String
) {
    constructor(item: BlogSearchItem) : this(
        title = item.title,
        content = item.content,
        blogname = item.blogname,
        postdate = item.postdate,
        url = item.url
    )
}
