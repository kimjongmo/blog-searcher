package com.example.blogsearcher.app.search

import com.example.blogsearcher.app.config.AsyncConfig
import com.example.blogsearcher.domain.record.BlogSearchRecord
import com.example.blogsearcher.domain.record.BlogSearchRecordRepository
import com.example.blogsearcher.domain.search.event.BlogSearchEvent
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Component
class BlogSearchEventHandler(
    private val blogSearchEventRepository: BlogSearchRecordRepository,
    private val cache: LoadingCache<String, Long>
) {
    private val lock = ReentrantLock()
    /**
     * 검색 이벤트 발생시 데이터베이스에 저장.
     * 검색 응답에 영향을 주지 않게하기 위해 비동기로 실행시키도록 한다
     * */
    @Async(AsyncConfig.EVENT_POOL_NAME)
    @EventListener(BlogSearchEvent::class)
    fun handle(event: BlogSearchEvent) {
        blogSearchEventRepository.save(BlogSearchRecord(event.vendor, event.keyword, event.searchAt))
    }

    /**
     * 검색 이벤트 발생시 캐시에 저장.
     * 검색 응답에 영향을 주지 않게하기 위해 비동기로 실행시키도록 한다
     * */
    @Async(AsyncConfig.EVENT_POOL_NAME)
    @EventListener(BlogSearchEvent::class)
    fun saveOrIncrease(blogSearchEvent: BlogSearchEvent) {
        val keyword = blogSearchEvent.keyword.value

        lock.withLock {
            val count = cache.get(keyword)
            cache.put(keyword, count + 1)
        }
    }
}
