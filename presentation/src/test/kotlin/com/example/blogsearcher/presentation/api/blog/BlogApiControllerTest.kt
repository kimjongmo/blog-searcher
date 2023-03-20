package com.example.blogsearcher.presentation.api.blog

import com.example.blogsearcher.app.search.BlogSearchService
import com.example.blogsearcher.app.search.NotFoundSearchSourceException
import com.example.blogsearcher.domain.search.BlogSearchItem
import com.example.blogsearcher.domain.search.BlogSearchPage
import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
internal class BlogApiControllerTest {
    @MockBean
    private lateinit var blogSearchService: BlogSearchService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `정상적인 요청`() {
        val keyword = Keyword("검색어")
        val page = Page(no = 1, size = 10, sorting = Sorting.ACCURACY)
        val result = BlogSearchResult(
            BlogSearchPage(1, 1, 50, 1),
            listOf(BlogSearchItem("제목", "내용", "블로그", postdate = LocalDate.now(), url = "url"))
        )

        given(blogSearchService.search(eq(keyword), eq(page))).willReturn(result)

        val mvcResult = request(keyword, page)

        mvcResult.andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        verify(blogSearchService, times(1)).search(eq(keyword), eq(page))
    }

    @Test
    fun `잘못된 요청 파라미터 전달 - sorting`() {
        val keyword = Keyword("검색어")
        val page = Page(no = 1, size = 10, sorting = Sorting.ACCURACY)

        val mvcResult = mockMvc.perform(
            get("/api/v1/blog/search")
                .param("keyword", keyword.value)
                .param("page", page.no.toString())
                .param("size", page.size.toString())
                .param("sort", page.sorting.name.lowercase())
                .contentType("application/json; ")
        )

        val expectedValue = "{\"message\":\"요청을 확인해주세요.\",\"field\":{\"sort\":\"must be [ACCURACY, RECENCY]\"}}"

        mvcResult.andReturn().response.setDefaultCharacterEncoding("utf-8")

        mvcResult.andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(expectedValue))
    }

    @Test
    fun `잘못된 요청 파라미터 전달 - 빈 키워드`() {
        val keyword = Keyword("  ")
        val page = Page(no = 1, size = 10, sorting = Sorting.ACCURACY)

        val mvcResult = request(keyword, page)

        val expectedValue = "{\"message\":\"요청을 확인해주세요.\",\"field\":{\"keyword\":\"검색어를 입력해주세요\"}}"

        mvcResult.andReturn().response.setDefaultCharacterEncoding("utf-8")

        mvcResult.andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(expectedValue))
    }

    @Test
    fun `잘못된 요청 파라미터 전달 - 범위를 벗어난 요청 값`() {
        // given
        val keyword = Keyword("검색")
        val page = Page(no = 100, size = 100, sorting = Sorting.ACCURACY)

        // when
        val mvcResult = request(keyword, page)

        // then
        mvcResult.andReturn().response.setDefaultCharacterEncoding("utf-8")

        mvcResult.andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString("message")))
            .andExpect(content().string(containsString("field")))
            .andExpect(content().string(containsString("size")))
            .andExpect(content().string(containsString("page")))
    }

    @Test
    fun `검색 소스가 없는 경우`() {
        val keyword = Keyword("검색")
        val page = Page(no = 1, size = 10, sorting = Sorting.ACCURACY)

        given(blogSearchService.search(eq(keyword), eq(page))).willThrow(NotFoundSearchSourceException())

        val mvcResult = request(keyword, page)

        val expectedValue = "{\"message\":\"현재 검색을 이용하실 수 없습니다.\"}"

        mvcResult.andReturn().response.setDefaultCharacterEncoding("utf-8")

        mvcResult.andExpect(status().isServiceUnavailable)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(expectedValue))

        verify(blogSearchService, times(1)).search(eq(keyword), eq(page))
    }

    private fun request(
        keyword: Keyword,
        page: Page
    ) = mockMvc.perform(
        get("/api/v1/blog/search")
            .param("keyword", keyword.value)
            .param("page", page.no.toString())
            .param("size", page.size.toString())
            .param("sort", page.sorting.name)
            .contentType("application/json; ")
    )
}
