package com.example.blogsearcher.presentation.api

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.validation.BindException

open class ErrorResponse(
    @get:Schema(example = "오류 메시지", description = "오류 메시지")
    val message: String
)

class BindingErrorResponse private constructor(
    message: String,
    val field: Map<String, String>
) : ErrorResponse(message) {
    companion object {
        fun from(bindException: BindException): BindingErrorResponse {
            val fieldErrors = bindException.bindingResult.fieldErrors

            return BindingErrorResponse(
                message = "요청을 확인해주세요.",
                field = fieldErrors.associate { it.field to (it.defaultMessage ?: "값을 확인해주세요") }
            )
        }
    }
}
