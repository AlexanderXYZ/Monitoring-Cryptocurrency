package com.buslaev.monitoringcryptocurrency.screens.metrics


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.buslaev.monitoringcryptocurrency.CryptoApplication
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponse
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.screens.metrics.MetricsFragment.Range.*
import com.buslaev.monitoringcryptocurrency.screens.metrics.chart.MetricsChart
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import com.github.mikephil.charting.charts.LineChart
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MetricsViewModel(application: Application, val symbol: String) :
    AndroidViewModel(application) {

    private var cryptoRepository = CryptoRepository(CryptoDatabase(APP_ACTIVITY))

    private val _metrics: MutableLiveData<Resource<MetricsResponse>> = MutableLiveData()
    val metrics: LiveData<Resource<MetricsResponse>> get() = _metrics

    private val defaultInterval = "1d"

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
            if (hasInternetConnection()) {
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

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<CryptoApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun buildChart(range: MetricsFragment.Range, listValues: List<List<Double>>): LineChart {
        val metricsChart = MetricsChart(getApplication(), listValues, range)
        return metricsChart.getLineChart()
    }

}