package com.example.blogsearcher.infra.client.core.factory

interface RetrofitFactory<T> {

    val properties: RetrofitProperties

    fun create(clazz: Class<T>): T
}
