package com.example.blogsearcher.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlogSearcherApplication

fun main(args: Array<String>) {
    runApplication<BlogSearcherApplication>(*args)
}
