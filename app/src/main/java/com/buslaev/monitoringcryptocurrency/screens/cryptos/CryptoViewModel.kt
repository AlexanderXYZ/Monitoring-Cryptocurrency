package com.buslaev.monitoringcryptocurrency.screens.cryptos

import android.app.Application
import androidx.lifecycle.*
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.REPOSITORY
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CryptoViewModel(application: Application) : AndroidViewModel(application) {

    private var cryptoRepository = CryptoRepository(CryptoDatabase(application))

    private val _allCrypto: MutableLiveData<Resource<CryptoResponse>> = MutableLiveData()
    val allCrypto: LiveData<Resource<CryptoResponse>> get() = _allCrypto

    init {
        getAllCrypto()
    }

    fun getAllCrypto() = viewModelScope.launch {
        _allCrypto.postValue(Resource.Loading())
        val response = cryptoRepository.getAllCrypto()
        _allCrypto.postValue(handleAllCryptoResponse(response))

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