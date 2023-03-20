package com.example.blogsearcher.domain.rank

import com.example.blogsearcher.domain.search.vo.Keyword

class BlogSearchKeywordRank(
    val rank: Int,
    val keyword: Keyword,
    val searchCount: Long
)
