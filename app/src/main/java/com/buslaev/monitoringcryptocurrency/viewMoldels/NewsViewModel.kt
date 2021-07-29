package com.buslaev.monitoringcryptocurrency.viewMoldels

import androidx.lifecycle.*
import com.buslaev.monitoringcryptocurrency.models.news.NewsResponse
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.NetworkConnectionHelper
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import com.buslaev.monitoringcryptocurrency.viewMoldels.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val networkConnectionHelper: NetworkConnectionHelper,
    private val repository: CryptoRepository
) : BaseViewModel() {

    private val _news: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val news: LiveData<Resource<NewsResponse>> get() = _news

    init {
        getNews()
    }

    fun getNews() = viewModelScope.launch {
        getSafeNews()
    }

    private suspend fun getSafeNews() {
        _news.postValue(Resource.Loading())
        try {
            if (networkConnectionHelper.hasInternetConnection()) {
                val response = repository.getNews()
                _news.postValue(handleResponse(response))
            } else {
                _news.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _news.postValue(Resource.Error("Network Failure"))
                else -> _news.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
}