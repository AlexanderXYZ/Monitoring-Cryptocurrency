package com.buslaev.monitoringcryptocurrency.screens.metrics


import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponse
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.screens.metrics.MetricsFragment.Range.*
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MetricsViewModel(val symbol: String) : ViewModel() {

    private var cryptoRepository = CryptoRepository(CryptoDatabase(APP_ACTIVITY))
    var metrics: MutableLiveData<Resource<MetricsResponse>> = MutableLiveData()

    private val defaultInterval = "1d"

    val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now()
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    fun getMetrics(range: MetricsFragment.Range) = viewModelScope.launch {
        metrics.postValue(Resource.Loading())
        val response = getResponseWithParameters(range)
        metrics.postValue(handleMetricsResponse(response))
    }

    private suspend fun getResponseWithParameters(range: MetricsFragment.Range): Response<MetricsResponse> {
        val startDate: String
        val endDate: String

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDate = currentDateTime.format(DateTimeFormatter.ISO_DATE)
            endDate = when (range) {
                HOUR -> currentDateTime.minusHours(1).format(DateTimeFormatter.ISO_DATE)
                DAY -> currentDateTime.minusDays(1).format(DateTimeFormatter.ISO_DATE)
                MONTH -> currentDateTime.minusMonths(1).format(DateTimeFormatter.ISO_DATE)
                YEAR -> currentDateTime.minusYears(1).format(DateTimeFormatter.ISO_DATE)
                //if else return MONTH
                else -> currentDateTime.minusMonths(1).format(DateTimeFormatter.ISO_DATE)
            }
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return if (range == ALL) {
            cryptoRepository.getMetricsAll(symbol, startDate, "1w")
        } else {
            cryptoRepository.getMetrics(symbol, startDate, endDate, defaultInterval)
        }
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