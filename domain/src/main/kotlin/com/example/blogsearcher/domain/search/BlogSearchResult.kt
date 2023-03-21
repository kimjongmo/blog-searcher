package com.example.blogsearcher.domain.search

import java.time.LocalDate

class BlogSearchResult(
    val page: BlogSearchPage,
    val items: List<BlogSearchItem>
)

/**
 * 검색 페이지 정보를 담는 클래스
 * @property total 전체 검색된 글 개수
 * @property pageNo 현재 페이지 번호
 * @property sizePerPage 페이지에 노출될 글 갯수
 * @property resultCnt 실제 검색된 글 갯수
 * */
class BlogSearchPage(
    val total: Int,
    val pageNo: Int,
    val sizePerPage: Int,
    val resultCnt: Int
)

/**
 * 블로그 글 정보를 담는 클래스
 * @property title 글 제목
 * @property content 글 요약 내용
 * @property blogname 블로그 이름
 * @property postdate 글 게시 날짜
 * @property url 글 링크
 * */
class BlogSearchItem(
    val title: String,
    val content: String,
    val blogname: String,
    val postdate: LocalDate,
    val url: String
)
