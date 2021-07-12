package com.buslaev.monitoringcryptocurrency.api

import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponce
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

//    @GET("api/v1/news")
//    suspend fun getNewsCall(): Call<NewsResponse>


    @GET("api/v1/assets/{assetKey}/metrics")
    suspend fun getMetrics(
        @Query("assetKey")
        symbol: String = "btc"
    ): Response<MetricsResponce>


}