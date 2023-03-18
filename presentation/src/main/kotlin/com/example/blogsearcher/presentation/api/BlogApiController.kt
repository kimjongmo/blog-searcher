package com.example.blogsearcher.presentation.api

import com.example.blogsearcher.app.search.query.BlogSearchResult
import com.example.blogsearcher.app.search.query.BlogSearchService
import com.example.blogsearcher.app.search.query.BlogSearchSpec
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/blog")
class BlogApiController(
    private val blogSearchService: BlogSearchService
) {
    @GetMapping("/search")
    fun searchBlog(requestDto: BlogSearchRequestDto): BlogSearchResult? {
        return blogSearchService.search(requestDto.toSpec())
    }

    class BlogSearchRequestDto(
        val keyword: String,
        val page: Int = 1,
        val size: Int = 10,
        val sort: String = "ACCURACY"
    ) {
        fun toSpec() = BlogSearchSpec(
            keyword = Keyword(keyword),
            page = Page(page, size, Sorting.from(sort)!!)
        )
    }
}
