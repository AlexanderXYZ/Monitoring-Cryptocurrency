package com.buslaev.monitoringcryptocurrency.api

import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponse
import com.buslaev.monitoringcryptocurrency.models.news.NewsResponse
import com.buslaev.monitoringcryptocurrency.models.profile.ProfileResponce
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoAPI {

    @GET("api/v1/assets?fields=id,slug,symbol,metrics/market_data/price_usd,metrics/market_data/percent_change_usd_last_24_hours")
    suspend fun getAllCrypto(): Response<CryptoResponse>

    @GET("api/v1/assets/{assetKey}/profile")
    suspend fun getProfileCrypto(
        @Path("assetKey")
        symbol: String = "btc"
    ): Response<ProfileResponce>

    @GET("api/v1/news")
    suspend fun getNews(): Response<NewsResponse>

    @GET("api/v1/assets/{assetKey}/metrics/price/time-series")
    suspend fun getMetrics(
        @Path("assetKey")
        symbol: String = "btc",
        @Query("start")
        start: String = "2021-06-12",
        @Query("end")
        end: String = "2021-07-12",
        @Query("interval")
        interval: String = "1d"
    ): Response<MetricsResponse>

    @GET("api/v1/assets/{assetKey}/metrics/price/time-series")
    suspend fun getMetricsAll(
        @Path("assetKey")
        symbol: String = "btc",
        @Query("before")
        before: String = "2021-06-12",
        @Query("interval")
        interval: String = "1d"
    ): Response<MetricsResponse>
}