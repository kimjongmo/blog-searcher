package com.example.blogsearcher.presentation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.example.blogsearcher"])
class BlogSearcherApplication

fun main(args: Array<String>) {
    runApplication<BlogSearcherApplication>(*args)
}
