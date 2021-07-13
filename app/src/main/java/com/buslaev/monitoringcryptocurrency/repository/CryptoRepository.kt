package com.buslaev.monitoringcryptocurrency.repository

import com.buslaev.monitoringcryptocurrency.api.RetrofitInstance
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase

class CryptoRepository(
    val database: CryptoDatabase
) {
    private val dao = database.getCryptoDao()

    suspend fun getAllCrypto() = RetrofitInstance.api.getAllCrypto()

    suspend fun getNews() = RetrofitInstance.api.getNews()

    suspend fun getProfileCrypto(symbol: String) = RetrofitInstance.api.getProfileCrypto(symbol)

    suspend fun getMetrics(symbol: String, start: String, end: String, interval: String) =
        RetrofitInstance.api.getMetrics(symbol, end, start, interval)

    suspend fun getMetricsAll(symbol: String, before: String, interval: String) =
        RetrofitInstance.api.getMetricsAll(symbol, before, interval)
}