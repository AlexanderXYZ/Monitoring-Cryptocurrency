package com.buslaev.monitoringcryptocurrency.screens.metrics


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MetricsViewModelFactory(val application: Application, val symbol: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MetricsViewModel(application, symbol) as T
    }
}