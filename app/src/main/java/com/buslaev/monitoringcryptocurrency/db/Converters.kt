package com.buslaev.monitoringcryptocurrency.db

import androidx.room.TypeConverter
import com.buslaev.monitoringcryptocurrency.models.allCrypto.MarketData
import com.buslaev.monitoringcryptocurrency.models.allCrypto.Metrics

class Converters {

    @TypeConverter
    fun fromMetrics(metrics: Metrics): Double {
        return metrics.market_data.price_usd
    }

    @TypeConverter
    fun toMetrics(value: Double): Metrics {
        return Metrics(MarketData(value,value))
    }
}