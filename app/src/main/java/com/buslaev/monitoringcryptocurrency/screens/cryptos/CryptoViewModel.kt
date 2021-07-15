package com.buslaev.monitoringcryptocurrency.screens.cryptos

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.buslaev.monitoringcryptocurrency.CryptoApplication
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CryptoViewModel(application: Application) : AndroidViewModel(application) {

    private var cryptoRepository = CryptoRepository(CryptoDatabase(application))

    private val _allCrypto: MutableLiveData<Resource<CryptoResponse>> = MutableLiveData()
    val allCrypto: LiveData<Resource<CryptoResponse>> get() = _allCrypto

    private val timer: UpdatesTimerData = UpdatesTimerData()

    init {
        getAllCrypto()
    }

    fun getAllCrypto() = viewModelScope.launch {
        getSafeAllCrypto()
    }

    private suspend fun getSafeAllCrypto() {
        _allCrypto.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = cryptoRepository.getAllCrypto()
                _allCrypto.postValue(handleAllCryptoResponse(response))
            } else {
                _allCrypto.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _allCrypto.postValue(Resource.Error("Network Failure"))
                else -> _allCrypto.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun handleAllCryptoResponse(response: Response<CryptoResponse>): Resource<CryptoResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<CryptoApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun startUpdatesData() {
        timer.start()
    }

    fun stopUpdatesData() {
        timer.cancel()
    }

    inner class UpdatesTimerData : CountDownTimer(10000L, 1000L) {
        override fun onTick(p0: Long) {}

        override fun onFinish() {
            getAllCrypto()
            startUpdatesData()
        }
    }
}