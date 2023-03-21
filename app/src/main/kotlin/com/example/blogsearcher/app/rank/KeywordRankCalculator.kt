package com.example.blogsearcher.app.rank

import com.example.blogsearcher.app.config.AsyncConfig
import com.example.blogsearcher.domain.search.event.BlogSearchEvent
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class KeywordRankCalculator(
    private val cache: LoadingCache<String, Long>
) {
    @Async(AsyncConfig.EVENT_POOL_NAME)
    @EventListener(BlogSearchEvent::class)
    fun saveOrIncrease(blogSearchEvent: BlogSearchEvent) {
        val keyword = blogSearchEvent.keyword.value
        val count = cache.get(keyword)
        cache.put(keyword, count + 1)
    }

    fun getTop(rank: Int): Map<String, Long> {
        return cache.asMap()
            .toList()
            .sortedByDescending { it.second }
            .take(rank)
            .associate { it.first to it.second }
    }
}
