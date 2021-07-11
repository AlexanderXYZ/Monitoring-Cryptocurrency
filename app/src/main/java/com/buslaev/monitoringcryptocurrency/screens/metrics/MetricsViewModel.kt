package com.buslaev.monitoringcryptocurrency.screens.metrics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponce
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MetricsViewModel(application: Application) : AndroidViewModel(application) {

    private var cryptoRepository = CryptoRepository(CryptoDatabase(getApplication()))
    var metrics: MutableLiveData<Resource<MetricsResponce>> = MutableLiveData()

    fun getMetrics(symbol: String) = viewModelScope.launch {
        metrics.postValue(Resource.Loading())
        val response = cryptoRepository.getMetricsCrypto(symbol)
        metrics.postValue(handleMetricsResponse(response))
    }

    private fun handleMetricsResponse(response: Response<MetricsResponce>): Resource<MetricsResponce> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}