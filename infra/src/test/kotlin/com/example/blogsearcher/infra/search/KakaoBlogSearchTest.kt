package com.example.blogsearcher.infra.search

import com.example.blogsearcher.domain.search.BlogSearchResult
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.infra.retrofit.core.factory.RetrofitProperties
import com.example.blogsearcher.infra.retrofit.kakao.KakaoClient
import com.example.blogsearcher.infra.retrofit.kakao.dto.Document
import com.example.blogsearcher.infra.retrofit.kakao.dto.KakaoBlogSearchResult
import com.example.blogsearcher.infra.retrofit.kakao.dto.Meta
import com.example.blogsearcher.infra.retrofit.kakao.factory.KakaoClientFactory
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class KakaoBlogSearchTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var kakaoClient: KakaoClient
    private lateinit var kakaoBlogSearch: KakaoBlogSearch
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun init() {
        mockWebServer = MockWebServer().also { it.start() }
        val properties = RetrofitProperties(baseUrl = "http://localhost:${mockWebServer.port}", headers = mapOf())
        val factory = KakaoClientFactory(properties)
        kakaoClient = factory.create(KakaoClient::class.java)
        objectMapper = factory.initObjectMapper()
        kakaoBlogSearch = KakaoBlogSearch(kakaoClient)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private val searchResult = KakaoBlogSearchResult(
        meta = Meta(isEnd = false, pageableCount = 100, totalCount = 100),
        documents = listOf(
            Document(
                contents = "본문요약",
                datetime = LocalDateTime.now(),
                title = "제목",
                url = "",
                blogname = "파워블로거",
                thumbnail = "썸네일"
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
        val result = kakaoBlogSearch.query(keyword = keyword, page = page)

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
        val result = kakaoBlogSearch.query(keyword = keyword, page = page)

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
        val result = kakaoBlogSearch.query(keyword = keyword, page = page)

        // then
        Assertions.assertNull(result)
        Assertions.assertEquals(3, mockWebServer.requestCount)
    }

    private fun assertEqual(
        page: Page,
        result: BlogSearchResult,
        kakaoBlogSearchResult: KakaoBlogSearchResult
    ) {
        Assertions.assertEquals(page.no, result.page.pageNo)
        Assertions.assertEquals(page.size, result.page.sizePerPage)
        Assertions.assertEquals(kakaoBlogSearchResult.documents.size, result.page.resultCnt)
        Assertions.assertEquals(kakaoBlogSearchResult.meta.totalCount, result.page.total)
        Assertions.assertEquals(kakaoBlogSearchResult.documents.first().contents, result.items.first().content)
        Assertions.assertEquals(
            kakaoBlogSearchResult.documents.first().datetime.toLocalDate(),
            result.items.first().postdate
        )
        Assertions.assertEquals(kakaoBlogSearchResult.documents.first().title, result.items.first().title)
        Assertions.assertEquals(kakaoBlogSearchResult.documents.first().url, result.items.first().url)
        Assertions.assertEquals(kakaoBlogSearchResult.documents.first().blogname, result.items.first().blogname)
    }
}
