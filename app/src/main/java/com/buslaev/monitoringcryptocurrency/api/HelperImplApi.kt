package com.buslaev.monitoringcryptocurrency.api

import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponse
import com.buslaev.monitoringcryptocurrency.models.news.NewsResponse
import com.buslaev.monitoringcryptocurrency.models.profile.ProfileResponce
import retrofit2.Response
import javax.inject.Inject

class HelperImplApi @Inject constructor(
    private val cryptoAPI: CryptoAPI
) : HelperApi {

    override suspend fun getAllCrypto(): Response<CryptoResponse> =
        cryptoAPI.getAllCrypto()

    override suspend fun getProfileCrypto(symbol: String): Response<ProfileResponce> =
        cryptoAPI.getProfileCrypto(symbol)

    override suspend fun getNews(): Response<NewsResponse> = cryptoAPI.getNews()

    override suspend fun getMetrics(
        symbol: String,
        start: String,
        end: String,
        interval: String
    ): Response<MetricsResponse> = cryptoAPI.getMetrics(symbol, start, end, interval)

    override suspend fun getMetricsAll(
        symbol: String,
        before: String,
        interval: String
    ): Response<MetricsResponse> = cryptoAPI.getMetricsAll(symbol, before, interval)
}