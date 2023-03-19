package com.example.blogsearcher.infra.record.mapper

import com.example.blogsearcher.domain.record.BlogSearchRecord
import com.example.blogsearcher.infra.persistence.entity.BlogSearchRecordEntity

object BlogSearchRecordMapper {
    fun toEntity(domain: BlogSearchRecord) = BlogSearchRecordEntity(
        vendor = domain.vendor,
        keyword = domain.keyword,
        searchAt = domain.searchAt
    )
}
