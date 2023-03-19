package com.example.blogsearcher.presentation.api

import org.springframework.validation.BindException

open class ErrorResponse(val message: String)

class BindingErrorResponse private constructor(message: String, val field: Map<String, String>) : ErrorResponse(message) {
    companion object {
        fun from(bindException: BindException): BindingErrorResponse {
            val fieldErrors = bindException.bindingResult.fieldErrors

            return BindingErrorResponse(
                "요청을 확인해주세요.",
                fieldErrors.associate { it.field to (it.defaultMessage ?: "값을 확인해주세요") }
            )
        }
    }
}
