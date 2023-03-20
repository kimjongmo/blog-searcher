package com.example.blogsearcher.app.common

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class EventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    fun publish(event: Any) {
        applicationEventPublisher.publishEvent(event)
    }
}
