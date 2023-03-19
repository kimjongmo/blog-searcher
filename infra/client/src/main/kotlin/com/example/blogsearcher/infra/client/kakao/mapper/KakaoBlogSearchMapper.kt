package com.example.blogsearcher.infra.client.kakao.mapper

import com.example.blogsearcher.app.search.query.BlogSearchItem
import com.example.blogsearcher.app.search.query.BlogSearchPage
import com.example.blogsearcher.app.search.query.BlogSearchResult
import com.example.blogsearcher.app.search.query.BlogSearchSpec
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.infra.client.kakao.KakaoConstants
import com.example.blogsearcher.infra.client.kakao.dto.KakaoBlogSearchResult

object KakaoBlogSearchMapper {
    fun toQueryMap(spec: BlogSearchSpec): Map<String, String> {
        return mapOf(
            KakaoConstants.PARAM_QUERY to spec.keyword.value,
            KakaoConstants.PARAM_SORT to when (spec.page.sorting) {
                Sorting.ACCURACY -> KakaoConstants.SORT_ACCURACY
                Sorting.RECENCY -> KakaoConstants.SORT_RECENCY
            },
            KakaoConstants.PARAM_PAGE to spec.page.no.toString(),
            KakaoConstants.PARAM_SIZE to spec.page.size.toString()
        )
    }

    fun toBlogSearchResult(kakaoBlogSearchResult: KakaoBlogSearchResult, spec: BlogSearchSpec): BlogSearchResult {

        val items = kakaoBlogSearchResult.documents.map {
            BlogSearchItem(
                title = it.title,
                content = it.contents,
                blogname = it.blogname,
                postdate = it.datetime.toLocalDate(),
                url = it.url
            )
        }

        val blogSearchPage = BlogSearchPage(
            total = kakaoBlogSearchResult.meta.totalCount,
            pageNo = spec.page.no,
            sizePerPage = spec.page.size,
            resultCnt = items.size
        )

        return BlogSearchResult(blogSearchPage, items)
    }
}
