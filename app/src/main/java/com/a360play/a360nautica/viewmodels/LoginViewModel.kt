package com.a360play.a360nautica.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a360play.a360nautica.model.LoginResponse
import com.app.starterkit.network.Repository
import com.app.starterkit.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class LoginViewModel(val repository: Repository) : ViewModel() {

    val loginResponse = MutableLiveData<Resource<LoginResponse>>()

    fun login(map: HashMap<String, String>) {
        loginResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.login(map)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        loginResponse.value = Resource.success(response.body(), "")
                    } else {
                        loginResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    loginResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }
}