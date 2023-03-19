package com.example.blogsearcher.infra.retrofit.core.factory

interface RetrofitFactory<T> {

    val properties: RetrofitProperties

    fun create(clazz: Class<T>): T
}
