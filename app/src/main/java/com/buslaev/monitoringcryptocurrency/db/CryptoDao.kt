package com.buslaev.monitoringcryptocurrency.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buslaev.monitoringcryptocurrency.models.allCrypto.Data

@Dao
interface CryptoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crypto: Data)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(crypto: Data): Long

    @Query("SELECT * FROM cryptos")
    fun getAllCrypto(): LiveData<List<Data>>

    @Delete
    suspend fun deleteCrypto(crypto: Data)
}