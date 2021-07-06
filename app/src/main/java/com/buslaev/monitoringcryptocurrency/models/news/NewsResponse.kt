package com.buslaev.monitoringcryptocurrency.models.news

data class NewsResponse(
    val `data`: List<Data>,
    val status: Status
)