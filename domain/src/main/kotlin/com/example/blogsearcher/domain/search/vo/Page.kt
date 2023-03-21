package com.example.blogsearcher.domain.search.vo

/**
 * @property no 페이지 번호
 * @property size 페이지에 노출될 문서 개수
 * @property sorting 페이지 검색시 정렬 옵션
 * */
data class Page(val no: Int, val size: Int, val sorting: Sorting)
