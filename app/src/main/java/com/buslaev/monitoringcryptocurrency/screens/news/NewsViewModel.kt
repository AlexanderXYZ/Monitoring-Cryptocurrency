package com.buslaev.monitoringcryptocurrency.screens.news

import androidx.lifecycle.*
import com.buslaev.monitoringcryptocurrency.models.news.NewsResponse
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.NetworkConnectionHelper
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val networkConnectionHelper: NetworkConnectionHelper,
    private val repository: CryptoRepository
) : ViewModel() {

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
                _news.postValue(handleNewsResponse(response))
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

    private fun handleNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}