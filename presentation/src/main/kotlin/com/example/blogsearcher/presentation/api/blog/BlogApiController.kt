package com.example.blogsearcher.presentation.api.blog

import com.example.blogsearcher.app.search.BlogSearchService
import com.example.blogsearcher.app.search.NotFoundSearchSourceException
import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.presentation.api.BindingErrorResponse
import com.example.blogsearcher.presentation.api.ErrorResponse
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

    @ExceptionHandler(NotFoundSearchSourceException::class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun handleNotFoundSource(exception: NotFoundSearchSourceException): ErrorResponse {
        return ErrorResponse(message = "현재 검색을 이용하실 수 없습니다.")
    }

    @GetMapping("/search")
    fun searchBlog(@Valid requestDto: BlogSearchRequestDto): BlogSearchResult? {
        val keyword = Keyword(requestDto.keyword)
        val page = Page(requestDto.page, requestDto.size, Sorting.from(requestDto.sort)!!)
        return blogSearchService.search(keyword, page)
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
)
