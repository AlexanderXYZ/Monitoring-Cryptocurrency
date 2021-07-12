package com.buslaev.monitoringcryptocurrency.screens.metrics


import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponse
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MetricsViewModel(val symbol: String) : ViewModel() {

    private var cryptoRepository = CryptoRepository(CryptoDatabase(APP_ACTIVITY))
    var metrics: MutableLiveData<Resource<MetricsResponse>> = MutableLiveData()


    val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now()
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    val start: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDateTime.format(DateTimeFormatter.ISO_DATE)
        } else {
            TODO("VERSION.SDK_INT < O")
        }

    fun getMetrics1h() = viewModelScope.launch {
//        val cal = Calendar.getInstance()
//        cal.add(Calendar.HOUR,-1)
//        val date = DateFormat.format("yyyy-MM-dd",cal).toString()
        val end = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDateTime.minusHours(1).format(DateTimeFormatter.ISO_DATE)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        metrics.postValue(Resource.Loading())
        val response = cryptoRepository.getMetrics(symbol, start, end)
        metrics.postValue(handleMetricsResponse(response))
    }

    fun getMetrics1d() = viewModelScope.launch {
        val end = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDateTime.minusDays(1).format(DateTimeFormatter.ISO_DATE)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        metrics.postValue(Resource.Loading())
        val response = cryptoRepository.getMetrics(symbol, start, end)
        metrics.postValue(handleMetricsResponse(response))
    }

    fun getMetrics1m() = viewModelScope.launch {
        val end = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDateTime.minusMonths(1).format(DateTimeFormatter.ISO_DATE)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        metrics.postValue(Resource.Loading())
        val response = cryptoRepository.getMetrics(symbol, start, end)
        metrics.postValue(handleMetricsResponse(response))
    }

    fun getMetrics1y() = viewModelScope.launch {
        val end = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDateTime.minusYears(1).format(DateTimeFormatter.ISO_DATE)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        metrics.postValue(Resource.Loading())
        val response = cryptoRepository.getMetrics(symbol, start, end)
        metrics.postValue(handleMetricsResponse(response))
    }

    fun getMetricsAll() = viewModelScope.launch {
        val end = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDateTime.minusHours(1).format(DateTimeFormatter.ISO_DATE)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        metrics.postValue(Resource.Loading())
        val response = cryptoRepository.getMetrics(symbol, start, end)
        metrics.postValue(handleMetricsResponse(response))
    }

    private fun handleMetricsResponse(response: Response<MetricsResponse>): Resource<MetricsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}