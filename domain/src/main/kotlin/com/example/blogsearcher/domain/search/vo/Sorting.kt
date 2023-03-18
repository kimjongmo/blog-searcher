package com.example.blogsearcher.domain.search.vo

enum class Sorting {
    ACCURACY,
    RECENCY;

    companion object {
        fun from(sort: String) = values().find { it.name == sort.uppercase() }
    }
}
