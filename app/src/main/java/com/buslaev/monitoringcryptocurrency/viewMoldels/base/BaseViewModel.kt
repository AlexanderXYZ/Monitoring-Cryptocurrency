package com.buslaev.monitoringcryptocurrency.viewMoldels.base

import androidx.lifecycle.ViewModel
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import retrofit2.Response

abstract class BaseViewModel:ViewModel() {

    fun <T> handleResponse(response: Response<T>): Resource<T> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}