package com.example.blogsearcher.domain.search

interface BlogSearchSourceRepository {
    fun findAll(): List<BlogSearchSource>
}
