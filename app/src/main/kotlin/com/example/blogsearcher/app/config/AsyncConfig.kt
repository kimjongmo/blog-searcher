package com.example.blogsearcher.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
@EnableAsync
class AsyncConfig : AsyncConfigurer {
    companion object {
        const val EVENT_POOL_NAME = "asyncEventHandlePool"
        const val EVENT_POOL_GROUP_NAME = "event-handle-pool-"
    }

    @Bean(EVENT_POOL_NAME)
    fun asyncPool() = createThreadPool(groupName = EVENT_POOL_GROUP_NAME)

    private fun createThreadPool(corePoolSize: Int = 10, maxPoolSize: Int = 100, queueCapacity: Int = Integer.MAX_VALUE, groupName: String) =
        ThreadPoolTaskExecutor().apply {
            this.corePoolSize = corePoolSize
            this.maxPoolSize = maxPoolSize
            this.queueCapacity = queueCapacity
            this.setThreadNamePrefix(groupName)
            this.setWaitForTasksToCompleteOnShutdown(true)
        }.also {
            it.initialize()
        }
}
