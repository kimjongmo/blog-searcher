package com.example.blogsearcher.infra.record

import com.example.blogsearcher.domain.record.BlogSearchRecord
import com.example.blogsearcher.domain.record.BlogSearchRecordRepository
import com.example.blogsearcher.infra.persistence.repository.BlogSearchRecordEntityRepository
import com.example.blogsearcher.infra.record.mapper.BlogSearchRecordMapper
import org.springframework.stereotype.Service

@Service
class BlogSearchRecordH2Repository(
    private val entityRepository: BlogSearchRecordEntityRepository
) : BlogSearchRecordRepository {
    override fun save(blogSearchRecord: BlogSearchRecord) {
        entityRepository.save(BlogSearchRecordMapper.toEntity(blogSearchRecord))
    }
}
