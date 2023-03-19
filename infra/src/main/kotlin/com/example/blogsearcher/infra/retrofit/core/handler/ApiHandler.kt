package com.example.blogsearcher.infra.retrofit.core.handler

import retrofit2.Call
import retrofit2.Response

object ApiHandler {
    fun <T, R> handle(fn: () -> Call<T>, onSuccess: (T) -> R, onFailure: ((Response<*>) -> Unit)? = null): R? {
        val response = fn().execute()
        return if (response.isSuccessful) {
            response.body()?.let { onSuccess(it) }
        } else {
            if (onFailure != null) {
                onFailure(response)
            }
            null
        }
    }
}
