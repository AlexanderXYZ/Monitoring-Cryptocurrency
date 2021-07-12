package com.buslaev.monitoringcryptocurrency.models.metrics

data class ValuesSchema(
    val close: String,
    val high: String,
    val low: String,
    val `open`: String,
    val timestamp: String,
    val volume: String
)