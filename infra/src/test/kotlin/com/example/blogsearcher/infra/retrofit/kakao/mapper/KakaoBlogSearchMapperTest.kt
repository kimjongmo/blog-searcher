package com.example.blogsearcher.infra.retrofit.kakao.mapper

import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.infra.retrofit.kakao.KakaoConstants
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class KakaoBlogSearchMapperTest {
    @Test
    fun `query map 생성 검증 CASE1`() {
        val keyword = Keyword("검색")
        val page = Page(no = 2, size = 10, sorting = Sorting.RECENCY)
        val queryMap = KakaoBlogSearchMapper.toQueryMap(keyword, page)

        assertEquals("2", queryMap[KakaoConstants.PARAM_PAGE])
        assertEquals("검색", queryMap[KakaoConstants.PARAM_QUERY])
        assertEquals(KakaoConstants.SORT_RECENCY, queryMap[KakaoConstants.PARAM_SORT])
        assertEquals("10", queryMap[KakaoConstants.PARAM_SIZE])
    }

    @Test
    fun `query map 생성 검증 CASE2`() {
        val keyword = Keyword("검색기")
        val page = Page(no = 6, size = 11, sorting = Sorting.ACCURACY)
        val queryMap = KakaoBlogSearchMapper.toQueryMap(keyword, page)

        assertEquals("6", queryMap[KakaoConstants.PARAM_PAGE])
        assertEquals("검색기", queryMap[KakaoConstants.PARAM_QUERY])
        assertEquals(KakaoConstants.SORT_ACCURACY, queryMap[KakaoConstants.PARAM_SORT])
        assertEquals("11", queryMap[KakaoConstants.PARAM_SIZE])
    }
}
