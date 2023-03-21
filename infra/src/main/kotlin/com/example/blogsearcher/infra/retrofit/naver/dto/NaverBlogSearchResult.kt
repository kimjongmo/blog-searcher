package com.example.blogsearcher.infra.retrofit.naver.dto

import java.time.LocalDate

class NaverBlogSearchResult(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<Document>
)

class Document(
    val title: String,
    val link: String,
    val description: String,
    val bloggername: String,
    val bloggerlink: String,
    val postdate: LocalDate
)
