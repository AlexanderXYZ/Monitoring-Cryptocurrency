package com.buslaev.monitoringcryptocurrency.models.profile

data class TokenDistribution(
    val current_supply: Any,
    val description: String,
    val initial_distribution: Int,
    val max_supply: Double,
    val sale_end: Any,
    val sale_start: Any
)