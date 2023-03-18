package com.example.blogsearcher.app.search.query

import java.time.LocalDate

class BlogSearchResult(
    val page: BlogSearchPage,
    val items: List<BlogSearchItem>
)

class BlogSearchPage(
    val total: Int,
    val currentPage: Int
)

class BlogSearchItem(
    val title: String,
    val content: String,
    val postdate: LocalDate,
    val url: String
)
