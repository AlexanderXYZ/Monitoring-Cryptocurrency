package com.buslaev.monitoringcryptocurrency.models.metrics

data class Data(
    val _internal_temp_agora_id: String,
    val contract_addresses: List<Any>,
    val id: String,
    val name: String,
    val parameters: Parameters,
    val schema: Schema,
    val slug: String,
    val symbol: String,
    val values: List<List<Double>>
)