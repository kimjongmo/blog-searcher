package com.example.blogsearcher.domain.search.vo

enum class Sorting {
    ACCURACY, // 정확도순
    RECENCY; // 최신순

    companion object {
        fun from(sort: String) = values().find { it.name == sort.uppercase() }
    }
}
