package com.buslaev.monitoringcryptocurrency.viewMoldels

import android.os.CountDownTimer
import androidx.lifecycle.*
import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.NetworkConnectionHelper
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import com.buslaev.monitoringcryptocurrency.viewMoldels.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val networkConnectionHelper: NetworkConnectionHelper,
    private val repository: CryptoRepository
) : BaseViewModel() {

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
            if (networkConnectionHelper.hasInternetConnection()) {
                val response = repository.getAllCrypto()
                _allCrypto.postValue(handleResponse(response))
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