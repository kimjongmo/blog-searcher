package com.example.blogsearcher.domain.search

/**
 * 애플리케이션에 등록된 블로그 검색 소스들을 리턴한다
 * */
interface BlogSearchSourceRepository {
    fun findAll(): List<BlogSearchSource>
}
