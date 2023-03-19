package com.example.blogsearcher.infra.client.kakao

import com.example.blogsearcher.app.search.query.BlogSearchResult
import com.example.blogsearcher.app.search.query.BlogSearchSpec
import com.example.blogsearcher.domain.search.vo.Keyword
import com.example.blogsearcher.domain.search.vo.Page
import com.example.blogsearcher.domain.search.vo.Sorting
import com.example.blogsearcher.infra.client.core.factory.RetrofitProperties
import com.example.blogsearcher.infra.client.kakao.api.KakaoBlogSearchApi
import com.example.blogsearcher.infra.client.kakao.dto.Document
import com.example.blogsearcher.infra.client.kakao.dto.KakaoBlogSearchResult
import com.example.blogsearcher.infra.client.kakao.dto.Meta
import com.example.blogsearcher.infra.client.kakao.factory.KakaoClientFactory
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class KakaoBlogSearchApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var kakaoClient: KakaoClient
    private lateinit var kakaoBlogSearchApi: KakaoBlogSearchApi
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun init() {
        mockWebServer = MockWebServer().also { it.start() }
        val properties = RetrofitProperties(baseUrl = "http://localhost:${mockWebServer.port}", headers = mapOf())
        val factory = KakaoClientFactory(properties)
        kakaoClient = factory.create(KakaoClient::class.java)
        objectMapper = factory.initObjectMapper()
        kakaoBlogSearchApi = KakaoBlogSearchApi(kakaoClient)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `조회 성공 케이스`() {
        // given
        val kakaoBlogSearchResult = KakaoBlogSearchResult(
            meta = Meta(isEnd = false, pageableCount = 100, totalCount = 100),
            documents = listOf(Document(contents = "본문요약", datetime = LocalDateTime.now(), title = "제목", url = "", blogname = "파워블로거", thumbnail = "썸네일"))
        )
        val jsonString = objectMapper.writeValueAsString(kakaoBlogSearchResult)
        mockWebServer.enqueue(MockResponse().setBody(jsonString))

        // when
        val spec = BlogSearchSpec(keyword = Keyword("keyword"), page = Page(no = 1, size = 10, sorting = Sorting.ACCURACY))
        val result = kakaoBlogSearchApi.search(spec)

        // then
        Assertions.assertNotNull(result)
        requireNotNull(result)

        assertEqual(spec, result, kakaoBlogSearchResult)
    }

    @Test
    fun `조회 실패시 retry`() {
        // given
        val kakaoBlogSearchResult = KakaoBlogSearchResult(
            meta = Meta(isEnd = false, pageableCount = 100, totalCount = 100),
            documents = listOf(Document(contents = "본문요약", datetime = LocalDateTime.now(), title = "제목", url = "", blogname = "파워블로거", thumbnail = "썸네일"))
        )
        val jsonString = objectMapper.writeValueAsString(kakaoBlogSearchResult)
        mockWebServer.enqueue(MockResponse().setResponseCode(400))
        mockWebServer.enqueue(MockResponse().setBody(jsonString))

        // when
        val spec = BlogSearchSpec(keyword = Keyword("keyword"), page = Page(no = 1, size = 10, sorting = Sorting.ACCURACY))
        val result = kakaoBlogSearchApi.search(spec)

        // then
        Assertions.assertNotNull(result)
        requireNotNull(result)

        Assertions.assertEquals(2, mockWebServer.requestCount)

        assertEqual(spec, result, kakaoBlogSearchResult)
    }

    @Test
    fun `최대 3회 재시도 후 실패시 null 리턴`() {
        // given
        mockWebServer.enqueue(MockResponse().setResponseCode(400))
        mockWebServer.enqueue(MockResponse().setResponseCode(400))
        mockWebServer.enqueue(MockResponse().setResponseCode(400))

        // when
        val spec = BlogSearchSpec(keyword = Keyword("keyword"), page = Page(no = 1, size = 10, sorting = Sorting.ACCURACY))
        val result = kakaoBlogSearchApi.search(spec)

        // then
        Assertions.assertNull(result)
        Assertions.assertEquals(3, mockWebServer.requestCount)
    }

    private fun assertEqual(
        spec: BlogSearchSpec,
        result: BlogSearchResult,
        kakaoBlogSearchResult: KakaoBlogSearchResult
    ) {
        Assertions.assertEquals(spec.page.no, result.page.pageNo)
        Assertions.assertEquals(spec.page.size, result.page.sizePerPage)
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
