package com.a360play.a360nautica.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a360play.a360nautica.data.card.*
import com.app.starterkit.network.Repository
import com.app.starterkit.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardViewModel(val repository: Repository) : ViewModel() {

    val cardDetailsResponse = MutableLiveData<Resource<CardDetailsResponse>>()
    val registercardresponse = MutableLiveData<Resource<RegisterBarcodeResponse>>()
    val rechargecardresponse = MutableLiveData<Resource<RechargeCardResponse>>()

    fun getCardDetailsInfo(barcode: String) {
        cardDetailsResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.getCardsDetails(barcode)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        cardDetailsResponse.value = Resource.success(response.body(), "")
                    } else {
                        cardDetailsResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    cardDetailsResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

    }

    fun registerCard(register_request: RegisterBarcodeRequest) {
        registercardresponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.registerwithCards(register_request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        registercardresponse.value = Resource.success(response.body(), "")
                    } else {
                        registercardresponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    registercardresponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

    }


    fun rechargeCard(recharge_card: HashMap<String, String>) {
        rechargecardresponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.rechargeCard(recharge_card)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        rechargecardresponse.value = Resource.success(response.body(), "")
                    } else {
                        rechargecardresponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    rechargecardresponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

    }

}