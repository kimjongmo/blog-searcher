package com.example.blogsearcher.domain.search

import java.time.LocalDate

class BlogSearchResult(
    val page: BlogSearchPage,
    val items: List<BlogSearchItem>
)

class BlogSearchPage(
    val total: Int,
    val pageNo: Int,
    val sizePerPage: Int,
    val resultCnt: Int
)

class BlogSearchItem(
    val title: String,
    val content: String,
    val blogname: String,
    val postdate: LocalDate,
    val url: String
)
