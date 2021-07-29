package com.buslaev.monitoringcryptocurrency.screens.metrics


import android.os.Build
import androidx.lifecycle.*
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponse
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.screens.metrics.MetricsFragment.Range.*
import com.buslaev.monitoringcryptocurrency.utilits.NetworkConnectionHelper
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MetricsViewModel @Inject constructor(
    private val networkConnectionHelper: NetworkConnectionHelper,
    private val repository: CryptoRepository
) : ViewModel() {

    private val _metrics: MutableLiveData<Resource<MetricsResponse>> = MutableLiveData()
    val metrics: LiveData<Resource<MetricsResponse>> get() = _metrics

    private val defaultInterval = "1d"
    private var symbol: String = ""

    fun setCurrentSymbol(currentSymbol: String) {
        this.symbol = currentSymbol
    }

    val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now()
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    fun getMetrics(range: MetricsFragment.Range) = viewModelScope.launch {
        getSafeMetrics(range)
    }

    private suspend fun getSafeMetrics(range: MetricsFragment.Range) {
        _metrics.postValue(Resource.Loading())
        try {
            if (networkConnectionHelper.hasInternetConnection()) {
                val response = getResponseWithParameters(range)
                _metrics.postValue(handleMetricsResponse(response))
            } else {
                _metrics.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _metrics.postValue(Resource.Error("Network Failure"))
                else -> _metrics.postValue(Resource.Error("Conversion Error"))
            }
        }
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
            repository.getMetricsAll(symbol, startDate, "1w")
        } else {
            repository.getMetrics(symbol, startDate, endDate, defaultInterval)
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