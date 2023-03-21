package com.example.blogsearcher.infra.retrofit.naver.dto

import java.time.LocalDate
/**
 * [문서 참조](https://developers.naver.com/docs/serviceapi/search/blog/blog.md#%ED%8C%8C%EB%9D%BC%EB%AF%B8%ED%84%B0)
 * */
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
