package com.buslaev.monitoringcryptocurrency.screens.cryptos

import android.app.Application
import androidx.lifecycle.*
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CryptoViewModel(application: Application) : AndroidViewModel(application) {

    private var cryptoRepository = CryptoRepository(CryptoDatabase(APP_ACTIVITY))
    val allCrypto: MutableLiveData<Resource<CryptoResponse>> = MutableLiveData()


    fun getAllCrypto() = viewModelScope.launch {
        allCrypto.postValue(Resource.Loading())
        val response = cryptoRepository.getAllCrypto()
        allCrypto.postValue(handleAllCryptoResponse(response))

    }


    private fun handleAllCryptoResponse(response: Response<CryptoResponse>): Resource<CryptoResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}