package com.example.blogsearcher.infra.retrofit.kakao.mapper

import com.example.blogsearcher.domain.search.BlogSearchItem
import com.example.blogsearcher.domain.search.BlogSearchPage
import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.infra.retrofit.kakao.KakaoConstants
import com.example.blogsearcher.infra.retrofit.kakao.dto.KakaoBlogSearchResult

object KakaoBlogSearchMapper {
    fun toQueryMap(keyword: Keyword, page: Page): Map<String, String> {
        return mapOf(
            KakaoConstants.PARAM_QUERY to keyword.value,
            KakaoConstants.PARAM_SORT to when (page.sorting) {
                Sorting.ACCURACY -> KakaoConstants.SORT_ACCURACY
                Sorting.RECENCY -> KakaoConstants.SORT_RECENCY
            },
            KakaoConstants.PARAM_PAGE to page.no.toString(),
            KakaoConstants.PARAM_SIZE to page.size.toString()
        )
    }

    fun toBlogSearchResult(kakaoBlogSearchResult: KakaoBlogSearchResult, page: Page): BlogSearchResult {

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
            pageNo = page.no,
            sizePerPage = page.size,
            resultCnt = items.size
        )

        return BlogSearchResult(blogSearchPage, items)
    }
}
