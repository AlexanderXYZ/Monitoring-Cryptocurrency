package com.buslaev.monitoringcryptocurrency.di.module

import android.content.Context
import com.buslaev.monitoringcryptocurrency.api.CryptoAPI
import com.buslaev.monitoringcryptocurrency.api.HelperApi
import com.buslaev.monitoringcryptocurrency.api.HelperImplApi
import com.buslaev.monitoringcryptocurrency.utilits.BASE_URL
import com.buslaev.monitoringcryptocurrency.utilits.NetworkConnectionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String) =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideCryptoApi(retrofit: Retrofit) = retrofit.create(CryptoAPI::class.java)

    @Provides
    @Singleton
    fun provideHelperApi(helperImplApi: HelperImplApi): HelperApi = helperImplApi
}