package com.app.starterkit.network

import com.a360play.a360nautica.data.booking.*
import com.a360play.a360nautica.data.card.*
import com.a360play.a360nautica.data.entrypoint.GetBookingDetailsResponse
import com.a360play.a360nautica.data.entrypoint.UserEntryRequest
import com.a360play.a360nautica.data.entrypoint.UserEntryResponse
import com.a360play.a360nautica.data.entrypoint.UserExitRequest
import com.a360play.a360nautica.model.LoginResponse
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class Repository(val apiService: ApiInterface) {

    suspend fun register(map: HashMap<String, String>): Response<LoginResponse> {
        return apiService.register(map)
    }

    suspend fun login(map: HashMap<String, String>): Response<LoginResponse> {
        return apiService.login(map)
    }

   /* suspend fun getGameList(userID: String): Response<GameListResponse> {
        return apiService.getGameList(userID)
    }
*/
    suspend fun getAccessoryList(coutryId: String): Response<AccessoryListResponse> {
        return apiService.listAccessories(coutryId)
    }

    suspend fun getVersionUpdate(): Response<CheckResponse> {
        return apiService.checkVersion()
    }


    suspend fun getNationalityList(): Response<NationalityListResponse> {
        return apiService.getNationalityList()
    }

    suspend fun getAgeList(): Response<GetAgeGroupResponse> {
        return apiService.getAgeList()
    }

    suspend fun getPaymentType(): Response<PaymentTypeResponse> {
        return apiService.getPaymentType()
    }

    suspend fun getSlotsList(id: Int): Response<GamingListResponse> {
        return apiService.getGamingSlots(id)
    }

    suspend fun getNameSuggestions(name: String): Response<NameSuggestionsResponse> {
        return apiService.getNameSuggestions(name)
    }

    /* suspend fun getBookinData(id: Int): Response<BookGameSucessData> {
         return apiService.getBookingData(id)
     }*/

    suspend fun saveAndPreviewPrint(map: BookRequestData): Response<BookGameSucessData> {
        return apiService.bookAndPrint(map)
    }
    suspend fun preview(map: BookRequestData): Response<PreviewSucessData> {
        return apiService.previewOrder(map)
    }

    suspend fun uploadFile(part : MultipartBody.Part,partChild : MultipartBody.Part): Response<ImageUploadresponse> {
        return apiService.sendAttachmentToserver(part,partChild)
    }


    /*   suspend fun bookdAndPrintTicket(id: Int): Response<BookGameSucessData> {
           return apiService.bookAndPrint(id)
       }*/

    suspend fun sendEnquiry(map: EnquiryRequestData): Response<EnquiryGameSucessData> {
        return apiService.sendEnquiry(map)
    }

    suspend fun entryUser(user: UserEntryRequest): Response<UserEntryresponse> {
        return apiService.entryUser(user)
    }

    suspend fun exitUser(user: UserExitRequest): Response<BookGameSucessData> {
        return apiService.exitUser(user)
    }

    suspend fun payviaCard(payviaCard: HashMap<String, String>): Response<PayViaCardResponse> {
        return apiService.payviaCard(payviaCard)
    }


    suspend fun bookingDetails(bookingID: String): Response<GetBookingDetailsResponse> {
        return apiService.getBookingDetailsByID(bookingID)
    }


    suspend fun customerDetails(phonenumber: String,isMonthlyPass:Boolean): Response<OldCustomerDetails> {
        return apiService.getCustomerDetails(phonenumber,isMonthlyPass)
    }
    suspend fun discountTicketID(ticketID: Int): Response<SlotDiscountResponse> {
        return apiService.getDiscountList(ticketID)
    }

    suspend fun getCardsDetails(barcode: String): Response<CardDetailsResponse> {
        return apiService.getPlayCardDetails(barcode)
    }

    suspend fun registerwithCards(registerrequest: RegisterBarcodeRequest): Response<RegisterBarcodeResponse> {
        return apiService.registerwithCards(registerrequest)
    }

    suspend fun rechargeCard(registerrequest: HashMap<String, String>): Response<RechargeCardResponse> {
        return apiService.rechargeCard(registerrequest)
    }
}