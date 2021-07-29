package com.buslaev.monitoringcryptocurrency.api

import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponse
import com.buslaev.monitoringcryptocurrency.models.news.NewsResponse
import com.buslaev.monitoringcryptocurrency.models.profile.ProfileResponce
import retrofit2.Response

interface HelperApi {

    suspend fun getAllCrypto(): Response<CryptoResponse>
    suspend fun getProfileCrypto(symbol: String): Response<ProfileResponce>
    suspend fun getNews(): Response<NewsResponse>
    suspend fun getMetrics(
        symbol: String,
        start: String,
        end: String,
        interval: String
    ): Response<MetricsResponse>

    suspend fun getMetricsAll(
        symbol: String,
        before: String,
        interval: String
    ): Response<MetricsResponse>
}