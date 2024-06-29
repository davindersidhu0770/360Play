package com.a360play.a360nautica.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a360play.a360nautica.data.booking.*
import com.a360play.a360nautica.data.card.SlotDiscountResponse
import com.a360play.a360nautica.data.entrypoint.GetBookingDetailsResponse
import com.app.starterkit.network.Repository
import com.app.starterkit.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class BookingViewModel(val repository: Repository) : ViewModel() {

    //    val gameresponse = MutableLiveData<Resource<GameListResponse>>()
    val accessoryListResponse = MutableLiveData<Resource<AccessoryListResponse>>()
    val versionCheckResponse = MutableLiveData<Resource<CheckResponse>>()
    val nationalityResponse = MutableLiveData<Resource<NationalityListResponse>>()
    val slotsResponse = MutableLiveData<Resource<GamingListResponse>>()
    val suggestionlistresponse = MutableLiveData<Resource<NameSuggestionsResponse>>()
    val booksucessResponse = MutableLiveData<Resource<BookGameSucessData>>()
    val preiewResponse = MutableLiveData<Resource<PreviewSucessData>>()
    val imageuploadResponse = MutableLiveData<Resource<ImageUploadresponse>>()

    val enquiryResponse = MutableLiveData<Resource<EnquiryGameSucessData>>()
    val bookingDetailsResponse = MutableLiveData<Resource<GetBookingDetailsResponse>>()
    val ageGroupResponse = MutableLiveData<Resource<GetAgeGroupResponse>>()
    val paymentTyperesponse = MutableLiveData<Resource<PaymentTypeResponse>>()
    val discountListResponse = MutableLiveData<Resource<SlotDiscountResponse>>()


    val customerDataResponse = MutableLiveData<Resource<OldCustomerDetails>>()


    fun getCustomerDetails(phonenumber: String, isMonthlyPass: Boolean) {
        customerDataResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.customerDetails(phonenumber, isMonthlyPass)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        customerDataResponse.value = Resource.success(response.body(), "")
                    } else {
                        customerDataResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    customerDataResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }


    fun getBookingDetails(id: String) {
        bookingDetailsResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.bookingDetails(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        bookingDetailsResponse.value = Resource.success(response.body(), "")
                    } else {
                        bookingDetailsResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    bookingDetailsResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

    }

    fun uploadAttachment(map: MultipartBody.Part, mapChild: MultipartBody.Part) {
        imageuploadResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.uploadFile(map, mapChild)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        imageuploadResponse.value = Resource.success(response.body(), "")
                    } else {
                        imageuploadResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    imageuploadResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

    }


/*
    fun preview(map: BookRequestData) {
        preiewResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.preview(map)
                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {
                        preiewResponse.value = Resource.success(response.body(), "")
                    } else {
                        preiewResponse.value = Resource.error(null, response.message())
                        Log.d("ISUEEEEEEE", response.message())

                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    preiewResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

    }
*/

    fun preview(map: BookRequestData) {
        preiewResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.preview(map)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        preiewResponse.value = Resource.success(response.body(), "")
                    } else {
                        val errorMessage =
                            "Unsuccessful response: ${response.code()} - ${response.message()}"
                        Log.e("API ERROR", errorMessage)
                        preiewResponse.value = Resource.error(null, errorMessage)
                    }
                }
            } catch (e: Exception) {
                val errorMessage = "Exception occurred: ${e.localizedMessage}"
                Log.e("API ERROR", errorMessage, e)
                withContext(Dispatchers.Main) {
                    preiewResponse.value = Resource.error(null, errorMessage)
                }
            }
        }
    }


    fun bookAndPrint(map: BookRequestData) {
        booksucessResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.saveAndPreviewPrint(map)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        booksucessResponse.value = Resource.success(response.body(), "")
                    } else {
                        booksucessResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    booksucessResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

    }

    fun sendEnquiry(map: EnquiryRequestData) {
        enquiryResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.sendEnquiry(map)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        enquiryResponse.value = Resource.success(response.body(), "")
                    } else {
                        enquiryResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "apiiiiiiiii: ${e.localizedMessage}")
                    enquiryResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }

    }

    fun getGamesByUserId(id: Int) {
        slotsResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.getSlotsList(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        slotsResponse.value = Resource.success(response.body(), "")
                    } else {
                        slotsResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    slotsResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }

    fun getNamesList(name: String) {
        suggestionlistresponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.getNameSuggestions(name)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        suggestionlistresponse.value = Resource.success(response.body(), "")
                    } else {
                        suggestionlistresponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    suggestionlistresponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }

/*
    fun getBookingData(id: Int) {
        previewBookingResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.getBookinData(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        previewBookingResponse.value = Resource.success(response.body(), "")
                    } else {
                        previewBookingResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    previewBookingResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }
*/

/*
    fun getGameList(userId: String) {
        gameresponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.getGameList(userId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        gameresponse.value = Resource.success(response.body(), "")
                    } else {
                        gameresponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    gameresponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }
*/

    fun getAccessoryList(countryId: String) {
        accessoryListResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.getAccessoryList(countryId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        accessoryListResponse.value = Resource.success(response.body(), "")
                    } else {
                        accessoryListResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    accessoryListResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }

    fun getCurrentVersion() {
        versionCheckResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.getVersionUpdate()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        versionCheckResponse.value = Resource.success(response.body(), "")
                    } else {
                        versionCheckResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    versionCheckResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }

    fun getNatinality() {
        nationalityResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.getNationalityList()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        nationalityResponse.value = Resource.success(response.body(), "")
                    } else {
                        nationalityResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    nationalityResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }

    fun getAgeGroup() {
        ageGroupResponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.getAgeList()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        ageGroupResponse.value = Resource.success(response.body(), "")
                    } else {
                        ageGroupResponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    ageGroupResponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }

    fun getPaymentType() {
        paymentTyperesponse.value = Resource.loading(null)
        viewModelScope.launch {
            try {
                val response = repository.getPaymentType()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        paymentTyperesponse.value = Resource.success(response.body(), "")
                    } else {
                        paymentTyperesponse.value = Resource.error(null, response.message())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("TAG", "register: ${e.localizedMessage}")
                    paymentTyperesponse.value = Resource.error(null, e.localizedMessage)
                }
            }
        }
    }


}