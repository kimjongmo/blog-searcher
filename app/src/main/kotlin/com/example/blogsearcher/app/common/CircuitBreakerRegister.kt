package com.example.blogsearcher.app.common

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Duration

private val log = KotlinLogging.logger { }

@Service
class CircuitBreakerRegister(
    private val circuitBreakerRegistry: CircuitBreakerRegistry
) {

    // TODO: 세부 세팅까지 열어주면 좋을듯
    fun register(name: String): CircuitBreaker {
        val config = CircuitBreakerConfig.custom()
            .slidingWindow(10, 10, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .failureRateThreshold(50.0F) // 실패 임계율 50%
            .waitDurationInOpenState(Duration.ofMinutes(1)) // OPEN 상태 유지 시간
            .automaticTransitionFromOpenToHalfOpenEnabled(true) // 자동 OPEN -> HALF OPEN
            .permittedNumberOfCallsInHalfOpenState(5) // half_open 상태가 되었을 때 허용할 요청 개수
            .build()

        return circuitBreakerRegistry.circuitBreaker("$name-circuit-breaker", config).also {
            loggingStateTransition(it)
        }
    }

    private fun loggingStateTransition(circuitBreaker: CircuitBreaker) {
        circuitBreaker.eventPublisher.onStateTransition { event ->
            val message =
                "CircuitBreaker[\"${circuitBreaker.name}\"] State [${event.stateTransition.fromState} -> ${event.stateTransition.toState}]"
            when (event.stateTransition) {
                CircuitBreaker.StateTransition.CLOSED_TO_OPEN,
                CircuitBreaker.StateTransition.HALF_OPEN_TO_OPEN -> {
                    log.error(message)
                }

                CircuitBreaker.StateTransition.OPEN_TO_CLOSED,
                CircuitBreaker.StateTransition.HALF_OPEN_TO_CLOSED,
                CircuitBreaker.StateTransition.OPEN_TO_HALF_OPEN -> {
                    log.info(message)
                }

                else -> log.info(message)
            }
        }
    }
}
