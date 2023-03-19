package com.example.blogsearcher.domain.record

import com.example.blogsearcher.domain.search.BlogSearchVendor
import com.example.blogsearcher.domain.search.vo.Keyword
import java.time.LocalDateTime

class BlogSearchRecord(
    val vendor: BlogSearchVendor,
    val keyword: Keyword,
    val searchAt: LocalDateTime
)
