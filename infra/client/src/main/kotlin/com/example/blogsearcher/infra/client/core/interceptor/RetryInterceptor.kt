package com.example.blogsearcher.infra.client.core.interceptor

import mu.KotlinLogging
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

private val log = KotlinLogging.logger { }

class RetryInterceptor(private val maxRetries: Int = 3) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        repeat(maxRetries) {
            val response = runCatching {
                chain.proceed(request)
            }.getOrElse { e ->
                log.error("error = $e", e)
                null
            }

            val tryCnt = it + 1
            if (maxRetries > tryCnt && (response == null || !response.isSuccessful)) {
                response?.close()
                log.info("try count = $tryCnt")
                return@repeat
            }

            return response ?: throw IOException("Response is null")
        }

        throw IOException("Response is null")
    }
}
