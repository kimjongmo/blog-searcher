package com.example.blogsearcher.infra.retrofit.naver.mapper

import com.example.blogsearcher.domain.search.BlogSearchItem
import com.example.blogsearcher.domain.search.BlogSearchPage
import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.infra.retrofit.naver.NaverConstants
import com.example.blogsearcher.infra.retrofit.naver.dto.NaverBlogSearchResult

object NaverBlogSearchMapper {
    fun toQueryMap(keyword: Keyword, page: Page): Map<String, String> {
        return mapOf(
            NaverConstants.PARAM_QUERY to keyword.value,
            NaverConstants.PARAM_SORT to when (page.sorting) {
                Sorting.ACCURACY -> NaverConstants.SORT_ACCURACY
                Sorting.RECENCY -> NaverConstants.SORT_RECENCY
            },
            NaverConstants.PARAM_SIZE to page.size.toString(),
            NaverConstants.PARAM_START to ((page.size * (page.no - 1)) + 1).toString()
        )
    }

    fun toBlogSearchResult(naverBlogSearchResult: NaverBlogSearchResult, page: Page): BlogSearchResult {
        val items = naverBlogSearchResult.items.map {
            BlogSearchItem(
                title = it.title,
                content = it.description,
                blogname = it.bloggername,
                postdate = it.postdate,
                url = it.link
            )
        }

        val blogSearchPage = BlogSearchPage(
            total = naverBlogSearchResult.total,
            pageNo = page.no,
            sizePerPage = page.size,
            resultCnt = items.size
        )

        return BlogSearchResult(blogSearchPage, items)
    }
}
