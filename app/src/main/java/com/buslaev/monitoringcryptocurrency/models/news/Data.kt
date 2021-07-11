package com.buslaev.monitoringcryptocurrency.models.news

import java.io.Serializable

data class Data(
    val author: Author,
    val content: String,
    val id: String,
    val published_at: String,
    val reference_title: Any,
    val references: List<Reference>,
    val tags: List<String>,
    val title: String,
    val url: String
) : Serializable