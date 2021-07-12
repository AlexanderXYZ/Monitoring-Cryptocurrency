package com.buslaev.monitoringcryptocurrency.models.metrics

data class Schema(
    val description: String,
    val metric_id: String,
    val minimum_interval: String,
    val source_attribution: List<SourceAttribution>,
    val values_schema: ValuesSchema
)