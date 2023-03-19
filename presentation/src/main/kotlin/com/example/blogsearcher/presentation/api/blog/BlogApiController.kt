package com.example.blogsearcher.presentation.api.blog

import com.example.blogsearcher.app.search.query.BlogSearchResult
import com.example.blogsearcher.app.search.query.BlogSearchService
import com.example.blogsearcher.app.search.query.BlogSearchSpec
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.presentation.api.BindingErrorResponse
import com.example.blogsearcher.presentation.api.validation.ValueOfEnum
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/api/v1/blog")
class BlogApiController(
    private val blogSearchService: BlogSearchService
) {
    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(bindException: BindException): BindingErrorResponse {
        return BindingErrorResponse.from(bindException)
    }

    @GetMapping("/search")
    fun searchBlog(@Valid requestDto: BlogSearchRequestDto): BlogSearchResult? {
        return blogSearchService.search(requestDto.toSpec())
    }
}

class BlogSearchRequestDto(
    @field:NotBlank(message = "검색어를 입력해주세요")
    val keyword: String,

    @field:Min(1)
    @field:Max(50)
    val page: Int = 1,

    @field:Min(1)
    @field:Max(50)
    val size: Int = 10,

    @field:ValueOfEnum(Sorting::class)
    val sort: String
) {
    fun toSpec() = BlogSearchSpec(
        keyword = Keyword(keyword),
        page = Page(page, size, Sorting.from(sort)!!)
    )
}
