package com.example.blogsearcher.presentation.api.blog

import com.example.blogsearcher.app.search.BlogSearchService
import com.example.blogsearcher.app.search.NotFoundSearchSourceException
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.presentation.api.BindingErrorResponse
import com.example.blogsearcher.presentation.api.ErrorResponse
import com.example.blogsearcher.presentation.api.blog.dto.BlogSearchRequestDto
import com.example.blogsearcher.presentation.api.blog.dto.BlogSearchResponseDto
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/blog")
class BlogApiController(
    private val blogSearchService: BlogSearchService
) : BlogApi {
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
    override fun searchBlog(@Valid requestDto: BlogSearchRequestDto): BlogSearchResponseDto? {
        val keyword = Keyword(requestDto.keyword)
        val page = Page(requestDto.page, requestDto.size, Sorting.from(requestDto.sort)!!)
        val blogSearchResult = blogSearchService.search(keyword, page)

        return blogSearchResult?.let { BlogSearchResponseDto(it) }
    }
}
