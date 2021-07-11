package com.buslaev.monitoringcryptocurrency.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.models.profile.ProfileResponce
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var cryptoRepository = CryptoRepository(CryptoDatabase(APP_ACTIVITY))
    val data: MutableLiveData<Resource<ProfileResponce>> = MutableLiveData()

    fun getProfile(symbol: String) = viewModelScope.launch {
        data.postValue(Resource.Loading())
        val response = cryptoRepository.getProfileCrypto(symbol)
        data.postValue(handleAllCryptoResponse(response))
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