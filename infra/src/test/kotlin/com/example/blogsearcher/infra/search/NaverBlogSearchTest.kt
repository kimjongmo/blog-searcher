package com.example.blogsearcher.infra.search

import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.infra.retrofit.core.factory.RetrofitProperties
import com.example.blogsearcher.infra.retrofit.naver.NaverClient
import com.example.blogsearcher.infra.retrofit.naver.dto.Document
import com.example.blogsearcher.infra.retrofit.naver.dto.NaverBlogSearchResult
import com.example.blogsearcher.infra.retrofit.naver.factory.NaverClientFactory
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class NaverBlogSearchTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var naverClient: NaverClient
    private lateinit var naverBlogSearch: NaverBlogSearch
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun init() {
        mockWebServer = MockWebServer().also { it.start() }
        val properties = RetrofitProperties(baseUrl = "http://localhost:${mockWebServer.port}", headers = mapOf())
        val factory = NaverClientFactory(properties)
        naverClient = factory.create(NaverClient::class.java)
        objectMapper = factory.initObjectMapper()
        naverBlogSearch = NaverBlogSearch(naverClient)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private val searchResult = NaverBlogSearchResult(
        lastBuildDate = "Tue, 21 Mar 2023 03:53:58 +0900",
        total = 100,
        start = 10,
        display = 10,
        items = listOf(
            Document(
                title = "제목",
                link = "",
                description = "본문요약",
                bloggername = "파워블로거",
                bloggerlink = "블로그링크",
                postdate = LocalDate.now()
            )
        )
    )

    @Test
    fun `조회 성공 케이스`() {
        // given
        val jsonString = objectMapper.writeValueAsString(searchResult)
        mockWebServer.enqueue(MockResponse().setBody(jsonString))

        // when
        val keyword = Keyword("keyword")
        val page = Page(no = 1, size = 10, sorting = Sorting.ACCURACY)
        val result = naverBlogSearch.query(keyword = keyword, page = page)

        // then
        Assertions.assertNotNull(result)
        requireNotNull(result)

        assertEqual(page, result, searchResult)
    }

    @Test
    fun `조회 실패시 retry`() {
        // given
        val jsonString = objectMapper.writeValueAsString(searchResult)
        mockWebServer.enqueue(MockResponse().setResponseCode(400))
        mockWebServer.enqueue(MockResponse().setBody(jsonString))

        // when
        val keyword = Keyword("keyword")
        val page = Page(no = 1, size = 10, sorting = Sorting.ACCURACY)
        val result = naverBlogSearch.query(keyword = keyword, page = page)

        // then
        Assertions.assertNotNull(result)
        requireNotNull(result)

        Assertions.assertEquals(2, mockWebServer.requestCount)

        assertEqual(page, result, searchResult)
    }

    @Test
    fun `최대 3회 재시도 후 실패시 null 리턴`() {
        // given
        mockWebServer.enqueue(MockResponse().setResponseCode(400))
        mockWebServer.enqueue(MockResponse().setResponseCode(400))
        mockWebServer.enqueue(MockResponse().setResponseCode(400))

        // when
        val keyword = Keyword("keyword")
        val page = Page(no = 1, size = 10, sorting = Sorting.ACCURACY)
        val result = naverBlogSearch.query(keyword = keyword, page = page)

        // then
        Assertions.assertNull(result)
        Assertions.assertEquals(3, mockWebServer.requestCount)
    }

    private fun assertEqual(
        page: Page,
        result: BlogSearchResult,
        naverBlogSearchResult: NaverBlogSearchResult
    ) {
        Assertions.assertEquals(page.no, result.page.pageNo)
        Assertions.assertEquals(page.size, result.page.sizePerPage)
        Assertions.assertEquals(naverBlogSearchResult.items.size, result.page.resultCnt)
        Assertions.assertEquals(naverBlogSearchResult.total, result.page.total)
        Assertions.assertEquals(naverBlogSearchResult.items.first().description, result.items.first().content)
        Assertions.assertEquals(
            naverBlogSearchResult.items.first().postdate,
            result.items.first().postdate
        )
        Assertions.assertEquals(naverBlogSearchResult.items.first().title, result.items.first().title)
        Assertions.assertEquals(naverBlogSearchResult.items.first().link, result.items.first().url)
        Assertions.assertEquals(naverBlogSearchResult.items.first().bloggername, result.items.first().blogname)
    }
}
