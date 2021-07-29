package com.buslaev.monitoringcryptocurrency.screens.profile

import androidx.lifecycle.*
import com.buslaev.monitoringcryptocurrency.models.profile.ProfileResponce
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.NetworkConnectionHelper
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val networkConnectionHelper: NetworkConnectionHelper,
    private val repository: CryptoRepository
) : ViewModel() {

    private val _profileData: MutableLiveData<Resource<ProfileResponce>> = MutableLiveData()
    val profileData: LiveData<Resource<ProfileResponce>> get() = _profileData

    fun getProfile(symbol: String) = viewModelScope.launch {
        getSafeProfileCall(symbol)
    }

    private suspend fun getSafeProfileCall(symbol: String) {
        _profileData.postValue(Resource.Loading())
        try {
            if (networkConnectionHelper.hasInternetConnection()) {
                val response = repository.getProfileCrypto(symbol)
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
}