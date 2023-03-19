package com.example.blogsearcher.domain.search.event

import com.example.blogsearcher.domain.search.BlogSearchVendor
import com.example.blogsearcher.domain.search.vo.Keyword
import java.time.LocalDateTime

class BlogSearchEvent(
    val vendor: BlogSearchVendor,
    val keyword: Keyword,
    val searchAt: LocalDateTime
)
