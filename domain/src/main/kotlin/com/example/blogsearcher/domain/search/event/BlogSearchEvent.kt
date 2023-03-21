package com.example.blogsearcher.domain.search.event

import com.example.blogsearcher.domain.search.BlogSearchVendor
import com.example.blogsearcher.domain.search.vo.Keyword
import java.time.LocalDateTime

/**
 * 블로그 검색이 일어났을 때 이벤트를 발행하기 위한 클래스
 * */
class BlogSearchEvent(
    val vendor: BlogSearchVendor,
    val keyword: Keyword,
    val searchAt: LocalDateTime
)
