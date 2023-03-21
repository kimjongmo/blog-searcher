package com.example.blogsearcher.app.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfig {
    companion object {
        const val RANKING_CACHE_NAME = "ranking"
    }

    private fun caffeineCacheBuilder(): Caffeine<Any, Any> {
        return Caffeine.newBuilder().expireAfterWrite(Duration.ofHours(1L))
    }

    @Bean
    fun loadingCache(): LoadingCache<String, Long> {
        return caffeineCacheBuilder().build { 0 }
    }

    @Bean
    fun cacheManager(): CaffeineCacheManager {
        val caffeineCacheManager = CaffeineCacheManager(RANKING_CACHE_NAME)
        caffeineCacheManager.setCaffeine(caffeineCacheBuilder())
        return caffeineCacheManager
    }
}
