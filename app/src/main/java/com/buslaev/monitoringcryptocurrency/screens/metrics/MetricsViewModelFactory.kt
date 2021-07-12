package com.buslaev.monitoringcryptocurrency.screens.metrics


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MetricsViewModelFactory(val symbol: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MetricsViewModel(symbol) as T
    }
}