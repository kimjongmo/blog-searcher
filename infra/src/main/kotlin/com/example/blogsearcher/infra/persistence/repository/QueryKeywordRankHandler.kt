package com.example.blogsearcher.infra.persistence.repository

import com.example.blogsearcher.app.rank.QueryKeywordRank
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.infra.persistence.entity.QBlogSearchRecordEntity.blogSearchRecordEntity
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

class BlogSearchRecordRankDto @QueryProjection constructor(
    val keyword: Keyword,
    val count: Long
)

@Repository
class QueryKeywordRankHandler(
    private val factory: JPAQueryFactory
) : QueryKeywordRank {
    override fun getRank(from: LocalDateTime, to: LocalDateTime): Map<String, Long> {
        val result = this.factory.select(QBlogSearchRecordRankDto(blogSearchRecordEntity.keyword, blogSearchRecordEntity.count()))
            .from(blogSearchRecordEntity)
            .where(blogSearchRecordEntity.searchAt.between(from, to))
            .groupBy(blogSearchRecordEntity.keyword)
            .limit(10)
            .fetch()

        return result
            .sortedByDescending { it.count }
            .associate { it.keyword.value to it.count }
    }
}
