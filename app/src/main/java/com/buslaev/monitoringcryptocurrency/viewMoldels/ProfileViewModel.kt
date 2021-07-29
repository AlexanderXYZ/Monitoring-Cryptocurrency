package com.buslaev.monitoringcryptocurrency.viewMoldels

import androidx.lifecycle.*
import com.buslaev.monitoringcryptocurrency.models.profile.ProfileResponce
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.NetworkConnectionHelper
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import com.buslaev.monitoringcryptocurrency.viewMoldels.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val networkConnectionHelper: NetworkConnectionHelper,
    private val repository: CryptoRepository
) : BaseViewModel() {

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
                _profileData.postValue(handleResponse(response))
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
}