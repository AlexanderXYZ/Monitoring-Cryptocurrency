package com.buslaev.monitoringcryptocurrency.models.allCrypto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cryptos")
data class Data(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val metrics: Metrics,
    val slug: String,
    val symbol: String
)