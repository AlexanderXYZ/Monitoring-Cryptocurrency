package com.buslaev.monitoringcryptocurrency.repository

import com.buslaev.monitoringcryptocurrency.api.HelperApi
import javax.inject.Inject

class CryptoRepository @Inject constructor(
    private val helperApi: HelperApi
) {

    suspend fun getAllCrypto() = helperApi.getAllCrypto()

    suspend fun getNews() = helperApi.getNews()

    suspend fun getProfileCrypto(symbol: String) = helperApi.getProfileCrypto(symbol)

    suspend fun getMetrics(symbol: String, start: String, end: String, interval: String) =
        helperApi.getMetrics(symbol, end, start, interval)

    suspend fun getMetricsAll(symbol: String, before: String, interval: String) =
        helperApi.getMetricsAll(symbol, before, interval)
}