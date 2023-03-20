package com.example.blogsearcher.app.common

import com.example.blogsearcher.app.config.AsyncConfig
import com.example.blogsearcher.domain.record.BlogSearchRecord
import com.example.blogsearcher.domain.record.BlogSearchRecordRepository
import com.example.blogsearcher.domain.search.event.BlogSearchEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class BlogSearchEventHandler(
    private val blogSearchEventRepository: BlogSearchRecordRepository
) {
    @Async(AsyncConfig.EVENT_POOL_NAME)
    @EventListener(BlogSearchEvent::class)
    fun handle(event: BlogSearchEvent) {
        blogSearchEventRepository.save(BlogSearchRecord(event.vendor, event.keyword, event.searchAt))
    }
}
