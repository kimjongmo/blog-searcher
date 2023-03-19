package com.example.blogsearcher.infra.persistence.repository

import com.example.blogsearcher.infra.persistence.entity.BlogSearchRecordEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BlogSearchRecordEntityRepository : JpaRepository<BlogSearchRecordEntity, Long>
