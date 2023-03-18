package com.example.blogsearcher.infra.client.kakao.mapper

import com.example.blogsearcher.app.search.query.BlogSearchItem
import com.example.blogsearcher.app.search.query.BlogSearchPage
import com.example.blogsearcher.app.search.query.BlogSearchResult
import com.example.blogsearcher.app.search.query.BlogSearchSpec
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.infra.client.kakao.dto.KakaoBlogSearchResult

object KakaoBlogSearchMapper {
    fun toQueryMap(spec: BlogSearchSpec): Map<String, String> {
        return mapOf(
            "query" to spec.keyword.value,
            "sort" to when (spec.page.sorting) {
                Sorting.ACCURACY -> "accuracy"
                Sorting.RECENCY -> "recency"
            },
            "page" to spec.page.no.toString(),
            "size" to spec.page.size.toString()
        )
    }

    fun toBlogSearchResult(kakaoBlogSearchResult: KakaoBlogSearchResult): BlogSearchResult {
        val blogSearchPage = BlogSearchPage(total = kakaoBlogSearchResult.meta.totalCount, currentPage = 1)

        val items = kakaoBlogSearchResult.documents.map {
            BlogSearchItem(
                title = it.title,
                content = it.contents,
                postdate = it.datetime.toLocalDate(),
                url = it.url
            )
        }

        return BlogSearchResult(blogSearchPage, items)
    }
}
