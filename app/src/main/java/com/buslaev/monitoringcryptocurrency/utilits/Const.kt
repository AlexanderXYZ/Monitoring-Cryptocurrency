package com.buslaev.monitoringcryptocurrency.utilits

import android.app.Application
import com.buslaev.monitoringcryptocurrency.MainActivity
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository

lateinit var APP_ACTIVITY: MainActivity
lateinit var APPLICATION_ACTIVITY:Application
const val BASE_URL = "https://data.messari.io"
const val SYMBOL_KEY = "symbol"

lateinit var REPOSITORY:CryptoRepository