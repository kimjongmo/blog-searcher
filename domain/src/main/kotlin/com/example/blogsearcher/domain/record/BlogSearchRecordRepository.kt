package com.example.blogsearcher.domain.record

interface BlogSearchRecordRepository {
    fun save(blogSearchRecord: BlogSearchRecord)
}
