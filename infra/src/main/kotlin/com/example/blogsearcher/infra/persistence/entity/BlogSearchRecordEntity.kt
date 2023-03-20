package com.example.blogsearcher.infra.persistence.entity

import com.example.blogsearcher.domain.search.BlogSearchVendor
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.infra.persistence.dbconverter.KeywordDbConverter
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "blog_search_record")
class BlogSearchRecordEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor")
    val vendor: BlogSearchVendor,

    @Column(name = "keyword")
    @Convert(converter = KeywordDbConverter::class)
    val keyword: Keyword,

    @Column(name = "search_at")
    val searchAt: LocalDateTime
)
