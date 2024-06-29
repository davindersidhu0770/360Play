package com.app.starterkit.network

import android.media.Image
import com.a360play.a360nautica.data.booking.*
import com.a360play.a360nautica.data.card.*
import com.a360play.a360nautica.data.entrypoint.GetBookingDetailsResponse
import com.a360play.a360nautica.data.entrypoint.UserEntryRequest
import com.a360play.a360nautica.data.entrypoint.UserExitRequest
import com.a360play.a360nautica.model.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import okhttp3.MultipartBody

interface ApiInterface {

    @FormUrlEncoded
    @POST("starter_kit/register.php")
    suspend fun register(
        @FieldMap map: HashMap<String, String>
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("User/Login")
    suspend fun login(
        @FieldMap map: HashMap<String, String>
    ): Response<LoginResponse>

    /* @GET("/Service/ListGames")
     suspend fun getGameList(@Query("userId") userId: String): Response<GameListResponse>
 */
    @GET("/Service/ListAccessories")
    suspend fun listAccessories(@Query("countryId") countryId: String): Response<AccessoryListResponse>

    @GET("Service/CheckAppVersion")
    suspend fun checkVersion(): Response<CheckResponse>

    @GET("Service/GetCountries")
    suspend fun getNationalityList(): Response<NationalityListResponse>

    @GET("Service/List_AgeGroup")
    suspend fun getAgeList(): Response<GetAgeGroupResponse>

    @GET("Service/List_Payment_Type")
    suspend fun getPaymentType(): Response<PaymentTypeResponse>

    @GET("Service/GetGamesByUserId?")
    suspend fun getGamingSlots(
        @Query("userId") id: Int
    ): Response<GamingListResponse>

    @GET("Service/GetNameSuggestion?")
    suspend fun getNameSuggestions(
        @Query("name") name: String
    ): Response<NameSuggestionsResponse>

    @POST("Service/Book")
    suspend fun bookAndPrint(
        @Body map: BookRequestData
    ): Response<BookGameSucessData>

    @POST("Service/GetTotalAmount")
    suspend fun previewOrder(
        @Body map: BookRequestData
    ): Response<PreviewSucessData>

    /*  @Multipart
      @POST("Service/SaveFile")
      suspend fun sendAttachmentToserver(
          @Part file: MultipartBody.Part,
          @Part childCompImage: MultipartBody.Part
      ): Response<ImageUploadresponse>
  */

    @Multipart
    @POST("Service/SaveFile")
    suspend fun sendAttachmentToserver(
        @Part file: MultipartBody.Part,
        @Part childCompImage: MultipartBody.Part
    ): Response<ImageUploadresponse>

    /*  @GET("Service/BookAndPrint")
      suspend fun bookAndPrint(
          @Query("Id") id: Int
      ): Response<BookGameSucessData>
  */

    @POST("Service/Enquiry")
    suspend fun sendEnquiry(
        @Body map: EnquiryRequestData
    ): Response<EnquiryGameSucessData>

    @POST("Service/Entry")
    suspend fun entryUser(@Body user: UserEntryRequest): Response<UserEntryresponse>

    @POST("Service/Exit")
    suspend fun exitUser(@Body user: UserExitRequest): Response<BookGameSucessData>

    @FormUrlEncoded
    @POST("/Service/Pay_By_PlayCard")
    suspend fun payviaCard(@FieldMap map: HashMap<String, String>): Response<PayViaCardResponse>

    @GET("Service/Status?")
    suspend fun getBookingDetailsByID(
        @Query("id") id: String
    ): Response<GetBookingDetailsResponse>

    @GET("Service/GetCustomerDetailsByMobile?")
    suspend fun getCustomerDetails(
        @Query("mobile") phonenumber: String,
        @Query("isMonthlyPass") isMonthlyPass: Boolean
    ): Response<OldCustomerDetails>


    @GET("/Service/DiscountList?")
    suspend fun getDiscountList(
        @Query("TicketId") TicketId: Int
    ): Response<SlotDiscountResponse>


    @GET("/Service/Get_PlayCardDetails?")
    suspend fun getPlayCardDetails(
        @Query("barcode") barcode: String
    ): Response<CardDetailsResponse>

    @POST("/Service/Register_PlayCard")
    suspend fun registerwithCards(@Body register_barcode: RegisterBarcodeRequest): Response<RegisterBarcodeResponse>


    @FormUrlEncoded
    @POST("/Service/Recharge_PlayCard")
    suspend fun rechargeCard(
        @FieldMap map: HashMap<String, String>
    ): Response<RechargeCardResponse>

//    @POST("/Service/Recharge_PlayCard")
//    suspend fun rechargeCard( @Body recharge_code: RechargeCardRequest):Response<RechargeCardResponse>

}