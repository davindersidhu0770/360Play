package com.a360play.a360nautica.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a360play.a360nautica.data.booking.BookGameSucessData
import com.a360play.a360nautica.data.booking.UserEntryresponse
import com.a360play.a360nautica.data.card.PayViaCardRequest
import com.a360play.a360nautica.data.card.PayViaCardResponse
import com.a360play.a360nautica.data.entrypoint.GetBookingDetailsResponse
import com.a360play.a360nautica.data.entrypoint.UserEntryRequest
import com.a360play.a360nautica.data.entrypoint.UserEntryResponse
import com.a360play.a360nautica.data.entrypoint.UserExitRequest
import com.app.starterkit.network.Repository
import com.app.starterkit.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntryViewModel (val repository: Repository): ViewModel()   {

    val entryResponse= MutableLiveData<Resource<UserEntryresponse>>()
    val exitResponse= MutableLiveData<Resource<BookGameSucessData>>()
    val payviacardResponse= MutableLiveData<Resource<PayViaCardResponse>>()



    fun payviaCard(payViaCardRequest: HashMap<String, String>){
        payviacardResponse.value=Resource.loading(null)
        viewModelScope.launch {
            try {
                val response=repository.payviaCard(payViaCardRequest)
                withContext(Dispatchers.Main){
                    if (response.isSuccessful){
                        payviacardResponse.value= Resource.success(response.body(),"")
                    }else{
                        payviacardResponse.value= Resource.error(null,response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    payviacardResponse.value= Resource.error(null,e.localizedMessage)
                }
            }
        }


    }

    fun exitUser(user: UserExitRequest){
        exitResponse.value=Resource.loading(null)
        viewModelScope.launch {
            try {
                val response=repository.exitUser(user)
                withContext(Dispatchers.Main){
                    if (response.isSuccessful){
                        exitResponse.value= Resource.success(response.body(),"")
                    }else{
                        exitResponse.value= Resource.error(null,response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    exitResponse.value= Resource.error(null,e.localizedMessage)
                }
            }
        }

    }



    fun entryUser(user: UserEntryRequest){
        entryResponse.value=Resource.loading(null)
        viewModelScope.launch {
            try {
                val response=repository.entryUser(user)
                withContext(Dispatchers.Main){
                    if (response.isSuccessful){
                        entryResponse.value= Resource.success(response.body(),"")
                    }else{
                        entryResponse.value= Resource.error(null,response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    entryResponse.value= Resource.error(null,e.localizedMessage)
                }
            }
        }

    }



}