package com.buslaev.monitoringcryptocurrency.models.allCrypto

data class CryptoResponse(
    val `data`: List<Data>,
    val status: Status
)