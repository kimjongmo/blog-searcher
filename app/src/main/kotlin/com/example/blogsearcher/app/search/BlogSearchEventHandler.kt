package com.example.blogsearcher.app.search

import com.example.blogsearcher.app.config.AsyncConfig
import com.example.blogsearcher.domain.record.BlogSearchRecord
import com.example.blogsearcher.domain.record.BlogSearchRecordRepository
import com.example.blogsearcher.domain.search.event.BlogSearchEvent
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class BlogSearchEventHandler(
    private val blogSearchEventRepository: BlogSearchRecordRepository,
    private val cache: LoadingCache<String, Long>
) {
    @Async(AsyncConfig.EVENT_POOL_NAME)
    @EventListener(BlogSearchEvent::class)
    fun handle(event: BlogSearchEvent) {
        blogSearchEventRepository.save(BlogSearchRecord(event.vendor, event.keyword, event.searchAt))
    }

    @Async(AsyncConfig.EVENT_POOL_NAME)
    @EventListener(BlogSearchEvent::class)
    fun saveOrIncrease(blogSearchEvent: BlogSearchEvent) {
        val keyword = blogSearchEvent.keyword.value
        val count = cache.get(keyword)
        cache.put(keyword, count + 1)
    }
}
