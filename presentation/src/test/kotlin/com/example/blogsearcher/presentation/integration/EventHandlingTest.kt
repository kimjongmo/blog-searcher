package com.example.blogsearcher.presentation.integration

import com.example.blogsearcher.app.common.EventPublisher
import com.example.blogsearcher.domain.record.BlogSearchRecordRepository
import com.example.blogsearcher.domain.search.BlogSearchVendor
import com.example.blogsearcher.domain.search.event.BlogSearchEvent
import com.example.blogsearcher.domain.search.vo.Keyword
import com.github.benmanes.caffeine.cache.LoadingCache
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime

@SpringBootTest
class EventHandlingTest {
    @Autowired
    private lateinit var eventPublisher: EventPublisher

    @MockBean
    private lateinit var caffeineCache: LoadingCache<String, Long>

    @MockBean
    private lateinit var blogSearchEventRepository: BlogSearchRecordRepository

    @Test
    fun `검색 이벤트 발행시 DB 저장`() {
        doNothing().`when`(blogSearchEventRepository).save(any())

        eventPublisher.publish(BlogSearchEvent(BlogSearchVendor.NAVER, Keyword("keyword"), LocalDateTime.now()))

        Thread.sleep(100)

        verify(blogSearchEventRepository, times(1)).save(any())
    }

    @Test
    fun `검색 이벤트 발행시 cache 저장`() {
        given(caffeineCache.get(eq("keyword"))).willReturn(0)
        doNothing().`when`(caffeineCache).put(any(), any())

        eventPublisher.publish(BlogSearchEvent(BlogSearchVendor.NAVER, Keyword("keyword"), LocalDateTime.now()))

        Thread.sleep(100)

        verify(caffeineCache, times(1)).get(eq("keyword"))
        verify(caffeineCache, times(1)).put(eq("keyword"), eq(1))
    }
}
