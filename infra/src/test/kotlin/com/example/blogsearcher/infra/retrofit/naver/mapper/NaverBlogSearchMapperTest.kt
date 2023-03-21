package com.example.blogsearcher.infra.retrofit.naver.mapper

import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.infra.retrofit.naver.NaverConstants
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class NaverBlogSearchMapperTest {
    @Test
    fun `query map 생성 검증 CASE1`() {
        val keyword = Keyword("검색")
        val page = Page(no = 2, size = 10, sorting = Sorting.RECENCY)
        val queryMap = NaverBlogSearchMapper.toQueryMap(keyword, page)

        assertEquals("11", queryMap[NaverConstants.PARAM_START])
        assertEquals("검색", queryMap[NaverConstants.PARAM_QUERY])
        assertEquals(NaverConstants.SORT_RECENCY, queryMap[NaverConstants.PARAM_SORT])
        assertEquals("10", queryMap[NaverConstants.PARAM_SIZE])
    }

    @Test
    fun `query map 생성 검증 CASE2`() {
        val keyword = Keyword("네이버")
        val page = Page(no = 1, size = 15, sorting = Sorting.ACCURACY)
        val queryMap = NaverBlogSearchMapper.toQueryMap(keyword, page)

        assertEquals("1", queryMap[NaverConstants.PARAM_START])
        assertEquals("네이버", queryMap[NaverConstants.PARAM_QUERY])
        assertEquals(NaverConstants.SORT_ACCURACY, queryMap[NaverConstants.PARAM_SORT])
        assertEquals("15", queryMap[NaverConstants.PARAM_SIZE])
    }
}
