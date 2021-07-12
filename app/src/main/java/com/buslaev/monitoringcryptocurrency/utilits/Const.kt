package com.buslaev.monitoringcryptocurrency.utilits

import com.buslaev.monitoringcryptocurrency.MainActivity
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository

lateinit var APP_ACTIVITY: MainActivity
const val BASE_URL = "https://data.messari.io"
const val SYMBOL_KEY = "symbol"

lateinit var REPOSITORY:CryptoRepository