package com.buslaev.monitoringcryptocurrency.screens.profile

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.buslaev.monitoringcryptocurrency.CryptoApplication
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.models.profile.ProfileResponce
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var cryptoRepository = CryptoRepository(CryptoDatabase(APP_ACTIVITY))

    private val _profileData: MutableLiveData<Resource<ProfileResponce>> = MutableLiveData()
    val profileData: LiveData<Resource<ProfileResponce>> get() = _profileData

    fun getProfile(symbol: String) = viewModelScope.launch {
        getSafeProfileCall(symbol)
    }

    private suspend fun getSafeProfileCall(symbol: String) {
        _profileData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = cryptoRepository.getProfileCrypto(symbol)
                _profileData.postValue(handleAllCryptoResponse(response))
            } else {
                _profileData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _profileData.postValue(Resource.Error("Network Failure"))
                else -> _profileData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleAllCryptoResponse(response: Response<ProfileResponce>): Resource<ProfileResponce> {
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
}