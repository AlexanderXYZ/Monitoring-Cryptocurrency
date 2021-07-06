package com.buslaev.monitoringcryptocurrency.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.buslaev.monitoringcryptocurrency.models.allCrypto.Data

@Database(entities = [Data::class], version = 1)
@TypeConverters(Converters::class)
abstract class CryptoDatabase : RoomDatabase() {

    abstract fun getCryptoDao(): CryptoDao

    companion object {
        @Volatile
        private var instance: CryptoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CryptoDatabase::class.java,
                "crypto_db"
            ).build()
    }
}