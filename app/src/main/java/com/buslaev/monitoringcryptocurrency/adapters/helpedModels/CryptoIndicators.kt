package com.buslaev.monitoringcryptocurrency.adapters.helpedModels

import java.io.Serializable

data class CryptoIndicators(
    val title: String,
    val symbol: String,
    val price: String,
    val percent: String,
    val colorPercent: Int
) : Serializable
