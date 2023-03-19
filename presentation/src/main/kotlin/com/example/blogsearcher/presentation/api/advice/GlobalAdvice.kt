package com.example.blogsearcher.presentation.api.advice

import com.example.blogsearcher.presentation.api.ErrorResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private val log = KotlinLogging.logger { }

@RestControllerAdvice
class GlobalAdvice {
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(ex: Exception): ErrorResponse {
        log.error("not handled exception..", ex)
        return ErrorResponse("알수없는 오류가 발생하였습니다.")
    }
}
