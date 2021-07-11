package com.buslaev.monitoringcryptocurrency.repository

import com.buslaev.monitoringcryptocurrency.api.RetrofitInstance
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.models.allCrypto.Data

class CryptoRepository(
    val database: CryptoDatabase
) {
    private val dao = database.getCryptoDao()

    suspend fun getAllCrypto() = RetrofitInstance.api.getAllCrypto()
    suspend fun getNews() = RetrofitInstance.api.getNews()

    //suspend fun getNewsCall() = RetrofitInstance.api.getNewsCall()
    suspend fun getProfileCrypto(symbol: String) = RetrofitInstance.api.getProfileCrypto(symbol)
    suspend fun getMetricsCrypto(symbol: String) = RetrofitInstance.api.getMetrics(symbol)

    suspend fun insertData(data: Data) {
        dao.insert(data)
    }
}