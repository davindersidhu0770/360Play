package com.a360play.a360nautica.view.fragment.book

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseFragment
import com.a360play.a360nautica.data.booking.AccessoryDataResponse
import com.a360play.a360nautica.data.booking.AccessoryResponse
import com.a360play.a360nautica.data.booking.BookRequestData
import com.a360play.a360nautica.data.booking.Data
import com.a360play.a360nautica.data.entrypoint.BookingDetailsData
import com.a360play.a360nautica.data.entrypoint.UserEntryRequest
import com.a360play.a360nautica.data.entrypoint.UserExitRequest
import com.a360play.a360nautica.databinding.FragmentUserdetailsBinding
import com.a360play.a360nautica.utils.BluetoothUtil
import com.a360play.a360nautica.utils.CustomSnackbar
import com.a360play.a360nautica.utils.ESCUtil
import com.a360play.a360nautica.utils.SunmiPrintHelper
import com.a360play.a360nautica.view.activities.MainActivity
import com.a360play.a360nautica.view.adapter.AccesoriesAdapter
import com.a360play.a360nautica.view.adapter.AccesoriesAdapterForPrint
import com.a360play.a360nautica.view.fragment.card.PayCardWithScanningFragment
import com.a360play.a360nautica.viewmodels.BookingViewModel
import com.a360play.a360nautica.viewmodels.EntryViewModel
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient
import com.app.starterkit.utils.MyViewModelFactory
import com.app.starterkit.utils.Status
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import java.text.DateFormat
import java.text.SimpleDateFormat
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class UserDetailsFragment : BaseFragment() {

    private lateinit var bookingApiResponse: BookingDetailsData
    private var kidsCount: String? = null
    private var adultCount: String? = null
    private var game_options: Int = 0
    private lateinit var exitDataResponse: Data
    private var additionalCost: Double = 0.0
    private var additionalVat: Double = 0.0
    var receivedAccessoryList: ArrayList<AccessoryDataResponse> = ArrayList<AccessoryDataResponse>()

    var accessoryCustomer: ArrayList<AccessoryResponse> = ArrayList<AccessoryResponse>()
    var accessoryMerchant: ArrayList<AccessoryResponse> = ArrayList<AccessoryResponse>()

    lateinit var binding: FragmentUserdetailsBinding
    val apiService = RetrofitClient.apiService
    val repository = Repository(apiService)
    lateinit var mBookingRequest: BookRequestData
    lateinit var userentryrequest: UserEntryRequest
    lateinit var userExitRequest: UserExitRequest

    //    lateinit var printme : PrintMe
    var booking_id: String = ""
    lateinit var shared: SharedPreferences
    var isDirectEnterInGame = false

    var selected_age = ""
    var selected_payment_mode = ""
    var selected_nationality = ""
    var selected_gender = ""
    var totalAccessoryTotal = 0
    var selected_additional_phonenumber = ""
    var barcode = ""
    var action = ""
    var dbId = 0
    var accessoryListData = ""

    var additional_amount: Double = 0.0
    var cashAmount: Double = 0.0
    var gameId: Int = 0

    var income_status_option = 0

    var bitmap: Bitmap? = null
    var iv_barcode: ImageView? = null

    // first receipt
    lateinit var tvCustomername: TextView
    lateinit var tvEntrytime: TextView
    lateinit var tvEmailaddrress: TextView
    lateinit var tvphone_ar: TextView
    lateinit var tvphone: TextView
    lateinit var tvMobilenumber: TextView
    lateinit var tvNooftickets: TextView
    lateinit var tvnumberofpersons: TextView
    lateinit var tvnumberofpersons1: TextView
    lateinit var tvvat: TextView
    lateinit var tv_finalamount: TextView
    lateinit var lloffer: LinearLayout
    lateinit var llprice: LinearLayout
    lateinit var llprice1: LinearLayout
    lateinit var lltotal: LinearLayout
    lateinit var lltotal1: LinearLayout
    lateinit var llpayment: LinearLayout
    lateinit var llpayment1: LinearLayout
    lateinit var llticket: LinearLayout
    lateinit var llnumberofpersons: LinearLayout
    lateinit var llnumberofpersons1: LinearLayout
    lateinit var llticket1: LinearLayout
    lateinit var lloffer1: LinearLayout
    lateinit var tv_finalamount1: TextView
    lateinit var tv_vat1: TextView
    lateinit var tv_offer: TextView
    lateinit var tv_offer1: TextView
    lateinit var tv_vat: TextView
    lateinit var tvtile: TextView
    lateinit var tvPaxtype: TextView
    lateinit var tv_bookingid: TextView
    lateinit var tv_discounttype: TextView
    lateinit var tvadditionalamount: TextView
    lateinit var tvadditionalamount2: TextView
    lateinit var tvadditionalmin: TextView
    lateinit var tvadditionalmin2: TextView
    lateinit var tv_additionalamt2: TextView
    lateinit var tv_additionalamt: TextView
    lateinit var tv_additionalmins: TextView
    lateinit var tv_additionalmins2: TextView
    lateinit var tv_discountvalue22: TextView
    lateinit var tv_cardetailsvalue: TextView
    lateinit var tvcardname: TextView
    lateinit var tv_cardetailsvalue22: TextView
    lateinit var tv_cardname2: TextView

    lateinit var ll_print: LinearLayout
    lateinit var ll_print2: LinearLayout
    lateinit var ll_additionalamount: LinearLayout
    lateinit var ll_additionalamount2: LinearLayout
    lateinit var ll_additionalmins: LinearLayout
    lateinit var ll_additionalmins2: LinearLayout
    lateinit var llvatPreview: LinearLayout
    lateinit var llvatPreview1: LinearLayout
    lateinit var llfinalamountPreview: LinearLayout
    lateinit var llfinalamountPreview1: LinearLayout

    lateinit var llaccessory1: LinearLayout
    lateinit var llaccessory2: LinearLayout
    lateinit var llservicetype1: LinearLayout
    lateinit var llservicetype2: LinearLayout

    lateinit var ll_discountype: LinearLayout
    lateinit var ll_cardDetails: LinearLayout
    lateinit var ll_cardname: LinearLayout

    lateinit var ll_carddetails22: LinearLayout
    lateinit var ll_cardname2: LinearLayout

    lateinit var ll_discounttype22: LinearLayout

    lateinit var tv_ridename: TextView
    lateinit var tv_price: TextView
    lateinit var tv_payment: TextView
    lateinit var tv_total: TextView
    lateinit var tv_todaysdate: TextView

    var myorientation = 0

    // second receipt

    lateinit var title: TextView
    lateinit var tv_gamenames2nd: TextView
    lateinit var tv_todaysdate2nd: TextView
    lateinit var tv_Customername2nd: TextView
    lateinit var tvEmailaddrress2nd: TextView
    lateinit var tv_mobilenumber2nd: TextView
    lateinit var tv_paxtype2nd: TextView
    lateinit var tv_entrytime2nd: TextView
    lateinit var tv_price2nd: TextView
    lateinit var tv_nooftickets2nd: TextView
    lateinit var tvvat1: TextView
    lateinit var tv_payment2nd: TextView
    lateinit var tv_total2nd: TextView
    lateinit var tv_bookingid2nd: TextView
    lateinit var iv_barcode2nd: ImageView
    lateinit var tvpass: TextView
    lateinit var tvemail2: TextView
    lateinit var exittime2: TextView
    lateinit var tvmerchant2: TextView
    lateinit var tvphone2: TextView
    lateinit var tvpowered2: TextView
    lateinit var tvtotal2: TextView
    lateinit var tvpayment2: TextView
    lateinit var tvticket2: TextView
    lateinit var tvprice2: TextView
    lateinit var tvetime2: TextView
    lateinit var tvpass2: TextView
    lateinit var tvdate2: TextView
    lateinit var tvname2: TextView
    lateinit var tvpowered: TextView
    lateinit var tvline: TextView
    lateinit var tv_entry_time: TextView
    lateinit var tvdate: TextView
    lateinit var tvdate_ar: TextView
    lateinit var tvemail_ar: TextView
    lateinit var tvemail: TextView
    lateinit var tvname: TextView
    lateinit var tvname_ar: TextView
    lateinit var tvetime: TextView
    lateinit var tvticket: TextView
    lateinit var tvprice: TextView
    lateinit var tvpayment: TextView
    lateinit var tvcustomercopy: TextView
    lateinit var tvtotal: TextView
    lateinit var tvexittime: TextView
    lateinit var tvexittime1: TextView
    lateinit var tvexittime2: TextView
    lateinit var tv_exittime: TextView
    lateinit var tv_entry_time2nd: TextView
    lateinit var tv_exittime2nd: TextView
    lateinit var tv_noted2nd: TextView
    lateinit var tvalert2: TextView
    lateinit var tvalert: TextView
    lateinit var tv_noted: TextView
    lateinit var ll_entry_time2nd: LinearLayout
    lateinit var ll_exittime2nd: LinearLayout
    lateinit var ll_entry_time: LinearLayout
    lateinit var ll_exittime: LinearLayout

    lateinit var rvaccessories2: RecyclerView
    lateinit var rvaccessories1: RecyclerView

    /*  // NGX variable setup
      private var ingxCallback: INGXCallback? = null
      var ngxPrinter = NGXPrinter.getNgxPrinterInstance()*/
    private val isBold = false
    private var isUnderLine: kotlin.Boolean = false
    private var showPayment: kotlin.Boolean = false
    private val record = 0
    private var testFont: String? = null

    //var size_width = 256
    var size_width = 200
    var size_height = 66
    var size: Float = 22.0F
    var dType = ""
    var currency = ""
    var cTimeZone = ""

    var cardDetails = ""
    var cardName = ""


    var discountID = 0
    var discountValue = 0
    var selectedGameId = 0
    private var paymentMode: String = ""
    var cashCreditHandled = true
    var errMessage = ""

    private val viewModel: BookingViewModel by viewModels() {
        MyViewModelFactory(repository)
    }

    private val entry_viewModel: EntryViewModel by viewModels() {
        MyViewModelFactory(repository)
    }

    private val exit_viewModel: EntryViewModel by viewModels() {
        MyViewModelFactory(repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserdetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        setCurrenttime()
        liseteners()
        bindObservers()

    }

    private fun setCurrenttime() {
        /* val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"))
         val currentLocalTime: Date = cal.getTime()
         val date: DateFormat = SimpleDateFormat("hh:mm a")
         val tz = TimeZone.getDefault()
         date.setTimeZone(tz)
         val localTime: String = date.format(currentLocalTime)*/

        val zoneId = ZoneId.of(cTimeZone)
        val currentTime = ZonedDateTime.now(zoneId)
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        val timeStr = currentTime.format(formatter)

        println("Current time in :+ $cTimeZone :  $timeStr")

        binding.tvCurrentime.setText("$timeStr")
    }

    private fun bindObservers() {

        exit_viewModel.exitResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireActivity(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    if (it.data!!.statusCode == 200) {
                        Toast.makeText(
                            requireActivity(),
                            it.data.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        exitDataResponse = it.data.data
                        ll_additionalamount.visibility = View.VISIBLE
                        ll_additionalamount2.visibility = View.VISIBLE
                        ll_additionalmins.visibility = View.VISIBLE
                        ll_additionalmins2.visibility = View.VISIBLE

                        //hide unrequired elements in case of Exit...

                        llprice.visibility = View.GONE
                        llprice1.visibility = View.GONE
                        llticket.visibility = View.GONE
                        llnumberofpersons.visibility = View.GONE
                        llticket1.visibility = View.GONE
                        llnumberofpersons1.visibility = View.GONE
                        llpayment.visibility = View.GONE
                        llpayment1.visibility = View.GONE
                        lltotal.visibility = View.GONE
                        lltotal1.visibility = View.GONE
                        ll_discountype.visibility = View.GONE
                        ll_discounttype22.visibility = View.GONE
                        ll_cardname.visibility = View.GONE
                        ll_cardname2.visibility = View.GONE

                        //print ticket here first if in case additional amount > 0.....
                        if (it.data.data.additionalAmount > 0) {

                            CustReceiptForExitCustomer(it.data.data)
                            Log.d("10May: ", "Additional amt")

                        } else {

                            backtoHome()
                            Log.d("10May: ", "Not additional amt")

                        }


                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "" + it.data.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        entry_viewModel.entryResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireActivity(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    if (it.data!!.statusCode == 200) {
                        Toast.makeText(
                            requireActivity(),
                            it.data.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().onBackPressed()

                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "" + it.data.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().onBackPressed()
                    }

                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.bookingDetailsResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireActivity(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {

                    if (it.data!!.statusCode == 200) {
                        bookingApiResponse = it.data!!.data
                        setBookingDetailsData(it.data.data)
                    } else if (it.data!!.statusCode == 300) {
                        Toast.makeText(
                            requireActivity(),
                            "" + it.data.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().onBackPressed()
                    } else if (it.data!!.statusCode == 400) {
                        Toast.makeText(
                            requireActivity(),
                            "" + it.data.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().onBackPressed()
                    } else if (it.data!!.statusCode == 404) {
                        Toast.makeText(
                            requireActivity(),
                            "" + it.data.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().onBackPressed()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "" + it.data.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        requireActivity().onBackPressed()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun CustReceiptForExitCustomer(response: Data) {
        if (response.id > 0) {
            tvcustomercopy.setText(resources.getString(R.string.customercopy_ar))
//          tvline.setText(context?.resources?.getString(R.string.multipledots)+"\n"+"Powered By Q-Tickets"+"\n"+context?.resources?.getString(R.string.multipledots))
            tvpowered.setText(resources.getString(R.string.poweredbyqtickets_ar))
            tvpowered2.setText(resources.getString(R.string.poweredbyqtickets_ar))
            tvmerchant2.setText(resources.getString(R.string.merchant_ar))
            tvalert2.setText(
                "Kindly request your punctuality when picking up your kids to avoid any additional charges; your timely presence is greatly appreciated."
            )

            tvalert.setText(
                "Kindly request your punctuality when picking up your kids to avoid any additional charges; your timely presence is greatly appreciated."
            )
            if (response.startTime == null) {
                ll_entry_time.visibility = View.GONE
                ll_exittime.visibility = View.GONE
                ll_entry_time2nd.visibility = View.GONE
                ll_exittime2nd.visibility = View.GONE

                tv_noted.setText(
                    "Please Note : \nThe ticket is only valid on the mentioned date. The customer should retain this slip during activity time."
                )

                tv_noted2nd.setText(
                    "Please Note :\nThe ticket is only valid on the mentioned date. The customer should retain this slip during activity time."
                )

            } else {
                ll_entry_time.visibility = View.VISIBLE
                ll_exittime.visibility = View.VISIBLE
                ll_entry_time2nd.visibility = View.VISIBLE
                ll_exittime2nd.visibility = View.VISIBLE
                var duration = response.duration
                setStartDateAndTime(response.startTime, duration)
                tv_noted.setText(
                    "Please Note :\nThe ticket is only valid on the mentioned date and time. The customer should retain this slip during activity time."
                )
                tv_noted2nd.setText(
                    "Please Note :\nThe ticket is only valid on the mentioned date and time. The customer should retain this slip during activity time."
                )
            }

            if (paymentMode.equals("Split")) {
                //height ...

                // Set the desired height in sp
                val desiredHeightSp = 30

                val desiredHeightPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    desiredHeightSp.toFloat(),
                    resources.displayMetrics
                ).toInt()

                val layoutParams = tv_payment.layoutParams
                layoutParams.height = desiredHeightPx
                tv_payment.layoutParams = layoutParams

            } else {

                // Set the desired height in sp
                val desiredHeightSp = 15

                val desiredHeightPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    desiredHeightSp.toFloat(),
                    resources.displayMetrics
                ).toInt()

                val layoutParams = tv_payment.layoutParams
                layoutParams.height = desiredHeightPx
                tv_payment.layoutParams = layoutParams
            }

            /* if (response.ListAccessories.size > 0) {

                 //print accessories...
                 llaccessory1.visibility = View.VISIBLE
                 accessoryCustomer.addAll(response.ListAccessories)

                 setAdapterCustomer()
             } else
                 llaccessory1.visibility = View.GONE*/


            var bookingID = response.id
            val bitmap1: Bitmap =
                CreateImageBarcode(bookingID.toString(), "Barcode")!!

//          bitmap = printMe.CreateImageBarcode(bookingID.toString(), "Barcode")
            iv_barcode!!.setImageBitmap(bitmap1)
            val params: LinearLayout.LayoutParams =
                iv_barcode!!.getLayoutParams() as LinearLayout.LayoutParams
            params.gravity = Gravity.CENTER
            iv_barcode!!.setLayoutParams(params)
            tv_bookingid.text = bookingID.toString()

            tv_ridename.text = response.passTime
            tvexittime1.text = response.time
            tvexittime2.text = response.time

            var total_payment = response.totalAmount.toDouble()

            tv_total.text = currency + " " + total_payment.toString()

            tv_todaysdate.text = getCurrentDate()

            tvCustomername.text = response.name
            tv_payment.text = response.payType
            tv_price.text = currency + " " + response.price.toString()

            tvMobilenumber.text = response.mobile
            tvPaxtype.text = response.quantity.toString()
//          tvEntrytime.text = response.duration.toString() + " minutes"
            tvNooftickets.text = response.quantity.toString()
            tvnumberofpersons.text = "0" //from api
            tvnumberofpersons1.text = "0" //from api
            if (response.totalVATAmount > 0) { //issue
                // inthat case only show vat and final amount fiels...
                llvatPreview.visibility = View.VISIBLE
                llfinalamountPreview.visibility = View.VISIBLE
            } else {
                llvatPreview.visibility = View.GONE
                llfinalamountPreview.visibility = View.GONE
            }

            if (response.vatPercentage > 0)
                tvvat.text =
                    "VAT(ضريبة القيمة المضافة " + "%)" + " " + response.vatPercentage.toString()
            else
                tvvat.text = resources.getString(R.string.vat)

            tv_finalamount.text =
                currency + " " + returnFinalAmt(
                    response.additionalAmount,
                    response.additionalVATAmount
                )

            tv_vat.text = currency + " " + response.additionalVATAmount.toString()

            if (response.additionalAmount > 0)
                tv_additionalamt.setText(currency + " " + response.additionalAmount.toString())
            else
                ll_additionalamount.visibility = View.GONE
            if (!response.extraDuration.equals("0"))
                tv_additionalmins.setText(response.extraDuration.toString() + " Mins")
            //12mayyy
            else
                ll_additionalmins.visibility = View.GONE

            printContentForExitCust(response)
            Log.d("7April:", "printContentForBookingOnly")

        }
    }

    fun returnFinalAmt(a: Double, b: Double): Double {
        return a + b
    }

    fun CreateImageBarcode(message: String?, type: String?): Bitmap? {
        var bitMatrix: BitMatrix? = null
        when (type) {
            "Barcode" -> try {
                bitMatrix = MultiFormatWriter().encode(
                    message,
                    BarcodeFormat.CODE_128,
                    size_width,
                    size_height
                )
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
        val width = bitMatrix!!.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (bitMatrix[j, i]) {
                    pixels[i * width + j] = -0x1000000
                } else {
                    pixels[i * width + j] = -0x1
                }
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    private fun setUpMerchantExitReceipt(response: Data) {
        if (response.id >0) {
            var bookingID = response.id
            bitmap = CreateImageBarcode(bookingID.toString(), "Barcode")
            iv_barcode2nd.setImageBitmap(bitmap)
            val params: LinearLayout.LayoutParams =
                iv_barcode2nd.getLayoutParams() as LinearLayout.LayoutParams
            params.gravity = Gravity.CENTER
            iv_barcode2nd.setLayoutParams(params)
            tv_bookingid2nd.text = bookingID.toString()

            tv_gamenames2nd.text = response.passTime

            if (paymentMode.equals("Split")) {
                //height ...

                // Set the desired height in sp
                val desiredHeightSp = 30

                val desiredHeightPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    desiredHeightSp.toFloat(),
                    resources.displayMetrics
                ).toInt()

                val layoutParams = tv_payment2nd.layoutParams
                layoutParams.height = desiredHeightPx
                tv_payment2nd.layoutParams = layoutParams

            } else {

                // Set the desired height in sp
                val desiredHeightSp = 15

                val desiredHeightPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    desiredHeightSp.toFloat(),
                    resources.displayMetrics
                ).toInt()

                val layoutParams = tv_payment2nd.layoutParams
                layoutParams.height = desiredHeightPx
                tv_payment2nd.layoutParams = layoutParams
            }

            llpayment1.visibility = View.VISIBLE
            tv_payment2nd.text = response.payType

            var total_payment = response.totalAmount

            tv_total2nd.text = currency + " " + total_payment.toString()

            tv_todaysdate2nd.text = getCurrentDate()
            tv_price2nd.text = currency + " " + response.price.toString()
            tv_Customername2nd.text = response.name
            tv_mobilenumber2nd.text = response.mobile
            tv_paxtype2nd.text = response.passTime
            tv_nooftickets2nd.text = response.quantity.toString()
            if (response.totalVATAmount > 0) {
                // inthat case only show vat and final amount fiels...
                llvatPreview1.visibility = View.VISIBLE
                llfinalamountPreview1.visibility = View.VISIBLE
            } else {
                llvatPreview1.visibility = View.GONE
                llfinalamountPreview1.visibility = View.GONE
            }

            if (response.vatPercentage > 0)
                tvvat1.text =
                    "VAT(ضريبة القيمة المضافة " + "%)" + " " + response.vatPercentage.toString()
            else
                tvvat1.text = resources.getString(R.string.vat)

            tv_vat1.text = currency + " " + response.additionalVATAmount.toString()

            tv_finalamount1.text =
                currency + " " + returnFinalAmt(
                    response.additionalAmount,
                    response.additionalVATAmount
                )

            if (response.additionalAmount > 0)
                tv_additionalamt2.setText(currency + " " + response.additionalAmount.toString())
            else
                ll_additionalamount2.visibility = View.GONE
            if (!response.extraDuration.equals("0"))
                tv_additionalmins2.setText(response.extraDuration.toString() + " Mins")
            else
                ll_additionalmins2.visibility = View.GONE

            printContentMerchant(response)
        }
    }

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        return dateFormat.format(calendar.time)
    }

    private fun printContentMerchant(response: Data) {
        var content = ""
        if (!com.a360play.a360nautica.utils.BluetoothUtil.isBlueToothPrinter) {
            if (!com.a360play.a360nautica.utils.BluetoothUtil.isBlueToothPrinter) {

                if (response.id >0) {

                    SunmiPrintHelper.getInstance()
                        .printBitmap(loadBitmapFromView(ll_print2), myorientation)
                    SunmiPrintHelper.getInstance().feedPaper()
                    if (dType.equals("njx"))
                        SunmiPrintHelper.getInstance().feedPaper()

                    backtoHome()

                }
            }
        } else {
            printByBluTooth(content)
        }
    }

    private fun printContentForExitCust(response: Data) {

        var content = ""
        if (!com.a360play.a360nautica.utils.BluetoothUtil.isBlueToothPrinter) {
            if (!com.a360play.a360nautica.utils.BluetoothUtil.isBlueToothPrinter) {

                if (response.id >0) {

                    /* if (response.startTime == null) {*/
                    ll_entry_time.visibility = View.GONE
                    ll_exittime.visibility = View.GONE
                    ll_entry_time2nd.visibility = View.GONE
                    ll_exittime2nd.visibility = View.GONE
                    SunmiPrintHelper.getInstance().setAlign(1)

                    SunmiPrintHelper.getInstance()
                        .printBitmap(loadBitmapFromView(ll_print), myorientation)
                    SunmiPrintHelper.getInstance().feedPaper()

                    if (dType.equals("njx"))
                        SunmiPrintHelper.getInstance().feedPaper()
/*
                    backtoHome()
*/
                    showDialogExit(requireContext())
                }


            }
        } else {
            printByBluTooth(content)
        }
    }

    fun showDialogExit(mContext: Context) {
        val alertDialog = AlertDialog.Builder(mContext)
        alertDialog.setMessage("Print Merchant Copy?")
        alertDialog.setCancelable(false)
        alertDialog.setNegativeButton("NO", object : DialogInterface.OnClickListener {

            override fun onClick(p0: DialogInterface?, p1: Int) {
                alertDialog.create().dismiss()
                backtoHome()
            }
        })

        alertDialog.setPositiveButton("YES",
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                    setUpMerchantExitReceipt(exitDataResponse)

                    alertDialog.create().dismiss()
                }
            })
        alertDialog.show()
    }

    private fun printByBluTooth(content: String) {
        if (isBold) {
            BluetoothUtil.sendData(ESCUtil.boldOn())
        } else {
            BluetoothUtil.sendData(ESCUtil.boldOff())
        }
        if (isUnderLine) {
            BluetoothUtil.sendData(ESCUtil.underlineWithOneDotWidthOn())
        } else {
            BluetoothUtil.sendData(ESCUtil.underlineOff())
        }
        if (record < 17) {
            BluetoothUtil.sendData(ESCUtil.singleByte())
            BluetoothUtil.sendData(ESCUtil.setCodeSystemSingle(codeParse(record)))
        } else {
            BluetoothUtil.sendData(ESCUtil.singleByteOff())
            BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(record)))
        }

//            BluetoothUtil.sendData(content.getBytes(mStrings[record]));
        BluetoothUtil.sendData(ESCUtil.nextLine(3))
    }

    private fun codeParse(value: Int): Byte {
        var res: Byte = 0x00
        when (value) {
            0 -> res = 0x00
            1, 2, 3, 4 -> res = (value + 1).toByte()
            5, 6, 7, 8, 9, 10, 11 -> res = (value + 8).toByte()
            12 -> res = 21
            13 -> res = 33
            14 -> res = 34
            15 -> res = 36
            16 -> res = 37
            17, 18, 19 -> res = (value - 17).toByte()
            20 -> res = 0xff.toByte()
            else -> {}
        }
        return res
    }

    fun loadBitmapFromView(view: View): Bitmap? {
        view.setDrawingCacheEnabled(true)
        view.buildDrawingCache(true)
        view.setDrawingCacheEnabled(true)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        if (dType.equals("njx"))
            view.layout(0, 0, 456, view.getMeasuredHeight())
        else
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight())
        view.buildDrawingCache(true)
        val b: Bitmap = Bitmap.createBitmap(view.getDrawingCache())

        return b
    }

    private fun backtoHome() {
        lifecycleScope.launch {
            delay(2000)
            val fm: FragmentManager = requireActivity().supportFragmentManager
            if (fm.getBackStackEntryCount() > 0) {
                for (i in 0 until fm.getBackStackEntryCount()) {
                    fm.popBackStack()
                }
            }
            requireActivity().finish()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setBookingDetailsData(bookingDetailsData: BookingDetailsData) {
        binding.tvCustomername.text = bookingDetailsData.name
//        binding.tvEmail.text = bookingDetailsData.email
        binding.tvCustomerphonenumber.text = bookingDetailsData.mobile
        binding.tvNumberoftickets.text = bookingDetailsData.quantity.toString()

        if (bookingDetailsData.quantity.toInt() > 1)
            binding.tvtickets.text = "Tickets"
        else
            binding.tvtickets.text = "Ticket"

        binding.tvSelectedpackage.text = bookingDetailsData.passTime
        binding.tvEntrytime.text = bookingDetailsData.duration.toString() + " minutes"

//        binding.tvTotalamount.text = currency + " " + bookingDetailsData.totalPrice.toString()
        binding.tvTotalamount.text = currency + " " + bookingDetailsData.totalAmount.toString()

        /* if (bookingDetailsData.totalVATAmount > 0) {
             binding.llvat.visibility = View.VISIBLE
             binding.llfinalamount.visibility = View.VISIBLE

         } else {
             binding.llvat.visibility = View.GONE
             binding.llfinalamount.visibility = View.GONE

         }*/

        binding.tvVatamt.text = currency + " " + bookingDetailsData.totalVATAmount.toString()
        binding.tvFinalamt.text = currency + " " + bookingDetailsData.totalAmount.toString()

//        binding.tvAdditionalcost.text = currency + " " + bookingDetailsData.additionalAmount.toString()
        binding.tvAdditionalcost.text = currency + " " + returnFinalAmt(
            bookingDetailsData.additionalAmount,
            bookingDetailsData.additionalVATAmount
        )


        if (bookingDetailsData.additionalAmount > 0) {
            additional_amount = bookingDetailsData.additionalAmount
            additionalCost = bookingDetailsData.additionalAmount.toDouble()
            additionalVat = bookingDetailsData.additionalVATAmount.toDouble()

            // show payment type options...

            if (showPayment)
                binding.llpayment.visibility = View.VISIBLE
            else
                binding.llpayment.visibility = View.GONE

            if (income_status_option == 3) {
/*
                binding.btnPayviacard.visibility = View.VISIBLE
*/
                binding.btnPayviacard.visibility = View.GONE
            }

        } else {
            binding.btnPayviacard.visibility = View.GONE
            binding.llpayment.visibility = View.GONE

        }

        if (bookingDetailsData.id != null) {
            barcode = bookingDetailsData.id.toString()
        }
        if (bookingDetailsData.entryOn == null) {
            binding.tvEntryexacttime.text = "      :    NA"
            binding.tvExitexacttime.text = "      :    NA"
            entry_exit_status(bookingDetailsData)
        } else {
            setStartDateAndTime(bookingDetailsData)
        }

        /* if (bookingDetailsData.endTime != null) {
             binding.btnPayviacard.visibility = View.GONE
         }*/


    }

    fun entry_exit_status(bookingDetailsData: BookingDetailsData) {

        this.arguments?.let {
            if (it.getString("eventtype", "").equals("entry")) {
                binding.llpayment.visibility = View.GONE

                if (it.getString("viewstatus", "").equals("entry")) {
                    binding.tvRidenotstart.visibility = View.GONE
                    if (bookingDetailsData.entryOn != null && bookingDetailsData.exitOn != null) {
                        binding.btnPayviacard.visibility = View.GONE
                    }
                } else {
                    binding.tvRidenotstart.visibility = View.VISIBLE
                    if (bookingDetailsData.entryOn != null && bookingDetailsData.exitOn == null) {
                        binding.tvRidenotstart.text = resources.getString(R.string.ridenotcomplete)
                    } else if (bookingDetailsData.entryOn != null && bookingDetailsData.exitOn != null) {
                        binding.tvRidenotstart.text =
                            resources.getString(R.string.ridealreadycompleted)
                    } else if (bookingDetailsData.entryOn == null) {
                        binding.tvRidenotstart.text = resources.getString(R.string.ridenotstarted)
                    }

                }

            }

            if (it.getString("eventtype", "").equals("exit")) {
                if (it.getString("viewstatus", "").equals("exit")) {
                    binding.tvRidenotstart.visibility = View.GONE
                } else {
                    binding.tvRidenotstart.visibility = View.VISIBLE //pending
                    if (bookingDetailsData.entryOn != null && bookingDetailsData.exitOn == null) {
                        binding.tvRidenotstart.text = resources.getString(R.string.ridenotcomplete)
                    } else if (bookingDetailsData.entryOn != null && bookingDetailsData.exitOn != null) {
                        binding.tvRidenotstart.text =
                            resources.getString(R.string.ridealreadycompleted)
                    } else if (bookingDetailsData.entryOn == null) {
                        binding.tvRidenotstart.text = resources.getString(R.string.ridenotstarted)
                    }
                }

            }

        }

    }

    fun setStartDateAndTime(start_time: String, duration: Int) {
        try {
            val original_format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
            original_format.timeZone = TimeZone.getTimeZone("UTC")
            val date = original_format.parse(start_time)
            original_format.timeZone = TimeZone.getDefault()
            val formattedDate = original_format.format(date)

            val targetFormat: DateFormat = SimpleDateFormat("hh:mm a")
            val actual_date_format = original_format.parse(formattedDate)
            val real_date = targetFormat.format(actual_date_format) // 20120821
            tv_entry_time.text = real_date
            tv_entry_time2nd.text = real_date

            val parsedDate = targetFormat.parse(real_date)
            val calendar = Calendar.getInstance()
            calendar.setTime(parsedDate)
            calendar.add(Calendar.MINUTE, duration)
            val formattedEndDate = targetFormat.format(calendar.getTime())
            tv_exittime.text = formattedEndDate.toString()
            tv_exittime2nd.text = formattedEndDate.toString()
        } catch (e: Exception) {

        }
    }

    fun setStartDateAndTime(bookingDetailsData: BookingDetailsData) {
        try {
            val dateStr = bookingDetailsData.entryOn
            Log.d("6July:", dateStr)
            /*  val formatter = DateTimeFormatter.ISO_DATE_TIME
              val utcDateTime = LocalDateTime.parse(dateStr, formatter)
              val zoneId = ZoneId.of(cTimeZone)
              val zonedDateTime = utcDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneId)
              val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a").withZone(zoneId)
              val result = zonedDateTime.format(timeFormatter)

              // Print the converted time string
              println(result)
              binding.tvEntryexacttime.text = "      :    " + result.toString()*/

            // modified on 5July.. as need to show the date from api...

            /* val formatter =
                 DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.ENGLISH)
             val dateTime = LocalDateTime.parse(dateStr, formatter)

             val outputFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
             val outputTime = dateTime.format(outputFormat)

             println(outputTime)*/

            val outputTime = bookingDetailsData.beginTime

            Log.d("6July:", outputTime)

            //set here...
            binding.tvEntryexacttime.text = "      :    " + outputTime

            val targetFormat: DateFormat = SimpleDateFormat("hh:mm a")
            val parsedDate = targetFormat.parse(outputTime)
            val calendar = Calendar.getInstance()
            calendar.setTime(parsedDate)
            calendar.add(Calendar.MINUTE, bookingDetailsData.duration)
            val formattedEndDate = targetFormat.format(calendar.getTime())
            binding.tvExitexacttime.text = "      :    " + formattedEndDate.toString()
            entry_exit_status(bookingDetailsData)
            Log.d(
                "14ap:",
                "Current Time Zone: " + cTimeZone + " result Expected End time : " + formattedEndDate.toString()
            )

        } catch (e: Exception) {
            binding.tvEntryexacttime.text = "      :    NA"
            binding.tvExitexacttime.text = "      :    NA"
        }
    }

    private fun liseteners() {

        binding.btnStartnow.setOnClickListener(View.OnClickListener {
            this.arguments?.let {
                if (checkForInternet(requireContext())) {
                    if (binding.btnStartnow.text.equals("Close")) {
                        requireActivity().onBackPressed()
                    } else {

                        if (it.getString("eventtype", "").equals("exit")) {

                            if (additionalCost > 0) {
                                if (paymentMode.isEmpty()) {
                                    Toast.makeText(
                                        requireActivity(),
                                        "Select any payment mode.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else if (!cashCreditHandled) {
                                    Toast.makeText(
                                        requireContext(),
                                        errMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {

                                    hitApitoExisttheGameZone()
                                }

                            } else {

                                cashAmount = 0.0
                                paymentMode = ""
                                hitApitoExisttheGameZone()//2may

                            }
                            // also print while exiting through app..
                        } else {
                            hitApitoEntertheGameZone()
                        }

                    }
                } else {
                    CustomSnackbar.make(
                        requireActivity().window.decorView.rootView as ViewGroup,
                        "No Internet Connection..."
                    ).show()
                }

            }
        })

        binding.llcash.setOnClickListener(View.OnClickListener {

            paymentMode = "Cash"
            binding.llsplitamt.visibility = View.GONE
            cashAmount = 0.0

            binding.llcash.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.corner_curve_yellow_selector
                )
            )
            binding.llcredit.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.corner_curve_normal_selector
                )
            )

            binding.llsplit.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.corner_curve_normal_selector
                )
            )

        })

        binding.llcredit.setOnClickListener(View.OnClickListener {
            cashAmount = 0.0

            paymentMode = "Card"
            binding.llsplitamt.visibility = View.GONE

            binding.llcredit.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.corner_curve_yellow_selector
                )
            )
            binding.llcash.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.corner_curve_normal_selector
                )
            )

            binding.llsplit.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.corner_curve_normal_selector
                )
            )

        })

        binding.llsplit.setOnClickListener(View.OnClickListener {

            paymentMode = "Split"

            binding.llsplitamt.visibility = View.VISIBLE

            binding.cardAmountTextView.setText(additionalCost.toString())
            binding.cashAmountString.setText(additionalVat.toString())

            binding.llsplit.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.corner_curve_yellow_selector
                )
            )
            binding.llcredit.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.corner_curve_normal_selector
                )
            )

            binding.llcash.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.corner_curve_normal_selector
                )
            )

        })

        binding.cashAmountString.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                // Get the cash amount entered by the user
                val cashAmountString = binding.cashAmountString.text.toString()
                cashAmount = cashAmountString.toDoubleOrNull() ?: 0.0
                // Validate cash amount not exceeding total amount
                if (cashAmount > additionalCost) {
                    binding.cashAmountString.error = "Cash amount cannot exceed total amount"
                    cashCreditHandled = false
                    errMessage = "Cash amount cannot exceed total amount"

                    return
                } else {

                    if (additionalVat > 0) {

                        if (cashAmount < additionalVat) {
                            binding.cashAmountString.error =
                                "Minimum cash amount is " + additionalVat
                            cashCreditHandled = false
                            errMessage = "Minimum cash amount is " + additionalVat

                            return
                        } else {
                            // Calculate the card amount
                            val cardAmount =
                                returnFinalAmt(additionalCost, additionalVat) - cashAmount
                            cashCreditHandled = true

                            binding.cardAmountTextView.setText(String.format("%.1f", cardAmount))

                        }
                    } else {
                        // Calculate the card amount
                        val cardAmount = additionalCost - cashAmount
                        cashCreditHandled = true
                        cashAmount = cashAmount

                        // Set the card amount in the cardAmountTextView
                        binding.cardAmountTextView.setText(String.format("%.1f", cardAmount))
                    }

                }
            }
        })

    }

    private fun hitApitoExisttheGameZone() {

        if (paymentMode.equals("Cash")) {

            //in case of cash, send additional cost to cashamount...
            cashAmount = returnFinalAmt(additionalCost, additionalVat)

        }


        userExitRequest =
            UserExitRequest(
                booking_id.toInt(),
                shared.getString("userID", "")!!.toInt(),
                bookingApiResponse.extraDuration,
                bookingApiResponse.additionalAmount,
                bookingApiResponse.additionalVATAmount,
                cashAmount,
                paymentMode,
                bookingApiResponse.additionalEntryFee
                )

        exit_viewModel.exitUser(userExitRequest)
        Log.d("30June: ", "cashAmount :" + cashAmount + " " + paymentMode)

    }

    private fun hitApitoEntertheGameZone() {

        userentryrequest =
            UserEntryRequest(booking_id.toInt(), shared.getString("userID", "")!!.toInt())
        entry_viewModel.entryUser(userentryrequest)//forentry
    }

    private fun initview() {
        shared = requireActivity().getSharedPreferences(
            resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
        currency = shared.getString("currency", "").toString()
        cTimeZone = shared.getString("ctimezone", "").toString()

        //get device type..
        dType = shared.getString("devicetype", "")!!

        Log.d("DType" + "Rrr ", dType.toString())

        var booking = shared.getBoolean("book_status", false)
        var entery = shared.getBoolean("entry_status", false)
        var exit = shared.getBoolean("exit_status", false)
        testFont = null
        testFont = "test.ttf"
        ll_entry_time = binding.root.findViewById(R.id.ll_entrytime)
        tvticket = binding.root.findViewById(R.id.tvticket)
        ll_exittime = binding.root.findViewById(R.id.ll_exittime)
        llservicetype1 = binding.root.findViewById(R.id.llservicetype1)
        llservicetype2 = binding.root.findViewById(R.id.llservicetype2)
        llaccessory2 = binding.root.findViewById(R.id.llaccessory2)
        llaccessory1 = binding.root.findViewById(R.id.llaccessory1)
        llvatPreview = binding.root.findViewById(R.id.llvatPreview)
        llvatPreview1 = binding.root.findViewById(R.id.llvatPreview1)
        llfinalamountPreview = binding.root.findViewById(R.id.llfinalamountPreview)
        llfinalamountPreview1 = binding.root.findViewById(R.id.llfinalamountPreview1)


        ll_discountype = binding.root.findViewById(R.id.ll_discounttype)
        ll_discounttype22 = binding.root.findViewById(R.id.ll_discounttype22)
        ll_cardDetails = binding.root.findViewById(R.id.ll_carddetails)
        ll_cardname = binding.root.findViewById(R.id.ll_cardname)

        ll_carddetails22 = binding.root.findViewById(R.id.ll_carddetails22)
        ll_cardname2 = binding.root.findViewById(R.id.ll_cardname2)


        rvaccessories2 = binding.root.findViewById(R.id.rvaccessories2)
        rvaccessories1 = binding.root.findViewById(R.id.rvaccessories1)

        ll_entry_time2nd = binding.root.findViewById(R.id.ll_entry2nd)
        ll_exittime2nd = binding.root.findViewById(R.id.ll_exit2nd)

        tv_noted2nd = binding.root.findViewById(R.id.tv_noted2nd)
        tvalert= binding.root.findViewById(R.id.tvalert)
        tvalert2 = binding.root.findViewById(R.id.tvalert2)
        tv_noted = binding.root.findViewById(R.id.tv_noted)

        tvprice = binding.root.findViewById(R.id.tvprice)
        tvcustomercopy = binding.root.findViewById(R.id.tvcustomercopy)
        tvtotal = binding.root.findViewById(R.id.tvtotal)
        tvpayment = binding.root.findViewById(R.id.tvpayment)
        tvexittime = binding.root.findViewById(R.id.tvexittime)
        tvexittime1 = binding.root.findViewById(R.id.tvexittime1)
        tvexittime2 = binding.root.findViewById(R.id.tvexittime2)
        tvpowered = binding.root.findViewById(R.id.tvpowered)
        tvline = binding.root.findViewById(R.id.tvline)
        tvname2 = binding.root.findViewById(R.id.tvname2)
        tvdate2 = binding.root.findViewById(R.id.tvdate2)
        tvemail2 = binding.root.findViewById(R.id.tvemail2)
        tvetime2 = binding.root.findViewById(R.id.tvetime2)
        exittime2 = binding.root.findViewById(R.id.exittime2)
        tvprice2 = binding.root.findViewById(R.id.tvprice2)
        tvmerchant2 = binding.root.findViewById(R.id.tvmerchant2)
        tvphone2 = binding.root.findViewById(R.id.tvphone2)
        tvpowered2 = binding.root.findViewById(R.id.tvpowered2)
        tvtotal2 = binding.root.findViewById(R.id.tvtotal2)
        tvpayment2 = binding.root.findViewById(R.id.tvpayment2)
        tvticket2 = binding.root.findViewById(R.id.tvticket2)
        tvpass2 = binding.root.findViewById(R.id.tvpass2)
        tvpass = binding.root.findViewById(R.id.tvpass)
        tvemail_ar = binding.root.findViewById(R.id.tvemail_ar)
        tvemail = binding.root.findViewById(R.id.tvemail)
        tvname = binding.root.findViewById(R.id.tvname)
        tvname_ar = binding.root.findViewById(R.id.tvname_ar)
        tvdate_ar = binding.root.findViewById(R.id.tvdate_ar)
        tvdate = binding.root.findViewById(R.id.tvdate)
        tv_entry_time = binding.root.findViewById(R.id.tv_entry_time)
        tvphone_ar = binding.root.findViewById(R.id.tvphone_ar)
        tvphone = binding.root.findViewById(R.id.tvphone)
        tv_exittime = binding.root.findViewById(R.id.tv_exittime)
        tvetime = binding.root.findViewById(R.id.tvetime)
        tv_entry_time2nd = binding.root.findViewById(R.id.tv_entry_time2nd)
        tv_exittime2nd = binding.root.findViewById(R.id.tv_exittime2nd)
        tv_ridename = binding.root.findViewById(R.id.tv_gamenames)
        tv_todaysdate = binding.root.findViewById(R.id.tv_todaysdate)
        tv_total = binding.root.findViewById(R.id.tv_total)
        tv_payment = binding.root.findViewById(R.id.tv_payment)
        tv_price = binding.root.findViewById(R.id.tv_price)
        tv_bookingid = binding.root.findViewById(R.id.tv_bookingid)
        ll_print = binding.root.findViewById(R.id.ll_print)
        ll_additionalamount2 = binding.root.findViewById(R.id.ll_additionalamount2)
        ll_additionalamount = binding.root.findViewById(R.id.ll_additionalamount)
        ll_additionalmins2 = binding.root.findViewById(R.id.ll_additionalmins2)
        ll_additionalmins = binding.root.findViewById(R.id.ll_additionalmins)
        ll_print2 = binding.root.findViewById(R.id.ll_print2)
        iv_barcode = binding.root.findViewById(R.id.iv_barcode)
        tvCustomername = binding.root.findViewById(R.id.tvcustomername)
        tvEmailaddrress = binding.root.findViewById(R.id.tv_emailaddrress)
        tvMobilenumber = binding.root.findViewById(R.id.tv_mobilenumber)
        tv_additionalamt2 = binding.root.findViewById(R.id.tv_additionalamt2)
        tv_additionalamt = binding.root.findViewById(R.id.tv_additionalamt)
        tv_additionalmins2 = binding.root.findViewById(R.id.tv_additionalmins2)
        tv_additionalmins = binding.root.findViewById(R.id.tv_additionalmins)
        tvadditionalamount = binding.root.findViewById(R.id.tvadditionalamount)
        tvadditionalamount2 = binding.root.findViewById(R.id.tvadditionalamount2)
        tvadditionalmin2 = binding.root.findViewById(R.id.tvadditionalmin2)
        tvadditionalmin = binding.root.findViewById(R.id.tvadditionalmin)
        tv_discounttype = binding.root.findViewById(R.id.tv_discountvalue)
        tv_discountvalue22 = binding.root.findViewById(R.id.tv_discountvalue22)
        tvcardname = binding.root.findViewById(R.id.tvcardname)
        tv_cardetailsvalue = binding.root.findViewById(R.id.tv_cardetailsvalue)
        tv_cardname2 = binding.root.findViewById(R.id.tv_cardname2)
        tv_cardetailsvalue22 = binding.root.findViewById(R.id.tv_cardetailsvalue22)
        tvtile = binding.root.findViewById(R.id.tvtile)
        tvPaxtype = binding.root.findViewById(R.id.tv_paxtype)
        tvEntrytime = binding.root.findViewById(R.id.tv_entrytime)
        tvvat = binding.root.findViewById(R.id.tvvat)
        tv_finalamount = binding.root.findViewById(R.id.tv_finalamount)
        tv_finalamount1 = binding.root.findViewById(R.id.tv_finalamount1)
        tv_vat1 = binding.root.findViewById(R.id.tv_vat1)
        tv_offer = binding.root.findViewById(R.id.tv_offer)
        tv_offer1 = binding.root.findViewById(R.id.tv_offer1)
        lloffer = binding.root.findViewById(R.id.lloffer)
        llnumberofpersons = binding.root.findViewById(R.id.llnumberofpersons)
        llticket = binding.root.findViewById(R.id.llticket)
        llticket1 = binding.root.findViewById(R.id.llticket1)
        llnumberofpersons1 = binding.root.findViewById(R.id.llnumberofpersons1)
        llprice = binding.root.findViewById(R.id.llprice)
        llprice1 = binding.root.findViewById(R.id.llprice1)
        lltotal1 = binding.root.findViewById(R.id.lltotal1)
        lltotal = binding.root.findViewById(R.id.lltotal)
        llpayment1 = binding.root.findViewById(R.id.llpayment1)
        llpayment = binding.root.findViewById(R.id.llpayment)
        lloffer1 = binding.root.findViewById(R.id.lloffer1)
        tvvat1 = binding.root.findViewById(R.id.tvvat1)
        tv_vat = binding.root.findViewById(R.id.tv_vat)
        tvNooftickets = binding.root.findViewById(R.id.tv_nooftickets)
        tvnumberofpersons = binding.root.findViewById(R.id.tvnumberofpersons)
        tvnumberofpersons1 = binding.root.findViewById(R.id.tvnumberofpersons1)
//      printMe = PrintMe(this)
        iv_barcode = binding.root.findViewById(R.id.iv_barcode)

        // second prints
        title = binding.root.findViewById(R.id.title2)
        tv_gamenames2nd = binding.root.findViewById(R.id.tv_gamenames2nd)
        tv_todaysdate2nd = binding.root.findViewById(R.id.tv_todaysdate2nd)
        tv_Customername2nd = binding.root.findViewById(R.id.tv_customername2nd)
        tvEmailaddrress2nd = binding.root.findViewById(R.id.tv_emailaddrress2nd)
        tv_mobilenumber2nd = binding.root.findViewById(R.id.tv_mobilenumber2nd)
        tv_paxtype2nd = binding.root.findViewById(R.id.tv_paxtype2nd)
        tv_entrytime2nd = binding.root.findViewById(R.id.tv_entrytime2nd)
        tv_price2nd = binding.root.findViewById(R.id.tv_price2nd)
        tv_nooftickets2nd = binding.root.findViewById(R.id.tv_nooftickets2nd)
        tv_payment2nd = binding.root.findViewById(R.id.tv_payment2nd)
        tv_total2nd = binding.root.findViewById(R.id.tv_total2nd)
        tv_bookingid2nd = binding.root.findViewById(R.id.tv_bookingid2nd)
        iv_barcode2nd = binding.root.findViewById(R.id.iv_barcode2nd)

        this.arguments?.let {

            if (it.getString("eventtype", "").equals("booking")) {
                income_status_option = 1
                binding.llCurrentime.visibility = View.GONE
                binding.llActualcost.visibility = View.VISIBLE
                binding.llAdditionalcost.visibility = View.GONE

                receivedAccessoryList =
                    (requireArguments().getSerializable("accessorylist") as ArrayList<AccessoryDataResponse>?)!!
                totalAccessoryTotal = it.getInt("totalaccessoryamount", 0)
                selected_additional_phonenumber = it.getString("additionalnumber", "")
                selected_gender = it.getString("selected_gender", "")
                barcode = it.getString("barcode", "")
                accessoryListData = it.getString("accessoryJsonData", "")
                selected_payment_mode = it.getString("payment_type", "")
                selected_age = it.getString("selected_age", "")
                selected_nationality = it.getString("selected_nationality", "")
                val numberoftickets = it.getString("numberoftickets", "")
                discountID = it.getInt("discountID", 0)
                discountValue = it.getInt("discountValue", 0)
                selectedGameId = it.getInt("selectedgameid", 0)
                cardName = it.getString("selectedCardName", "")
                val bookedBy = it.getString("bookedBy", "")
                game_options = it.getInt("game_options", 0)
                val selected_package_amount = it.getString("selected_package_amount", "")
                adultCount = it.getString("adults", "")
                kidsCount = it.getString("kids", "")
                gameId = it.getInt("checkGameId", 0)
                cardDetails = it.getString("carddetails", "")
                binding.btnStartnow.text = "CONFIRM & PRINT"
                binding.tvScreenstatus.text = "Start"

                if (selectedGameId == 0) {
                    //it means user have only selected accessories... hide the 5 layouts..
                    binding.llservicetype.visibility = View.GONE
                    binding.lloffers.visibility = View.GONE
                    binding.llduration.visibility = View.GONE
                    binding.lltickets.visibility = View.GONE
                    binding.lladults.visibility = View.GONE
                    binding.llkids.visibility = View.GONE
                    llservicetype2.visibility = View.GONE
                    llservicetype1.visibility = View.GONE
                } else {
                    binding.llservicetype.visibility = View.VISIBLE
                    binding.lloffers.visibility = View.VISIBLE
                    binding.llduration.visibility = View.VISIBLE
                    binding.lltickets.visibility = View.VISIBLE
                    binding.lladults.visibility = View.VISIBLE
                    binding.llkids.visibility = View.VISIBLE
                    llservicetype2.visibility = View.VISIBLE
                    llservicetype1.visibility = View.VISIBLE
                }

                if (booking == true && entery == true) {
/*
                    binding.btnStartandentergame.visibility = View.VISIBLE
*/
                    binding.btnStartandentergame.visibility = View.GONE

                } else {
                    //binding.btnStartandentergame.visibility = View.GONE
                }
                binding.btnPayviacard.visibility = View.GONE

                //hit api and show data from api only...

            } else if (it.getString("eventtype", "").equals("entry")) {
                binding.llpayment.visibility = View.GONE

                binding.llkids.visibility = View.GONE
                binding.lladults.visibility = View.GONE
                if (it.getString("viewstatus", "").equals("entry")) {
                    income_status_option = 2
                    binding.llCurrentime.visibility = View.VISIBLE
                    binding.llActualcost.visibility = View.VISIBLE
                    binding.llAdditionalcost.visibility = View.GONE
                    binding.btnStartnow.text = resources.getText(R.string.startnow)

                    // hide payment mode...

                } else {
                    binding.llCurrentime.visibility = View.GONE
                    binding.llActualcost.visibility = View.GONE
                    binding.llAdditionalcost.visibility = View.VISIBLE
                    binding.llsplitamt.visibility = View.GONE
                    binding.btnStartnow.text = "Close"

                }

                booking_id = it.getString("bookingID", "")
                hitApitogettheBookingIDDetails(booking_id)
                binding.tvScreenstatus.text = "Entry"
                //     binding.btnStartandentergame.visibility = View.GONE
                binding.btnPayviacard.visibility = View.GONE

            } else if (it.getString("eventtype", "").equals("exit")) {

                binding.llkids.visibility = View.GONE
                binding.lladults.visibility = View.GONE
                if (it.getString("viewstatus", "").equals("exit")) {
                    income_status_option = 3
                    binding.llCurrentime.visibility = View.GONE
                    binding.llActualcost.visibility = View.GONE
                    binding.llAdditionalcost.visibility = View.VISIBLE
                    binding.btnStartnow.text = "Exit"
                    binding.btnPayviacard.visibility = View.GONE
                    showPayment = true
                    if (additionalCost > 0) {
                        binding.llsplitamt.visibility = View.VISIBLE
                        binding.llpayment.visibility = View.VISIBLE
                    }

                } else {
                    binding.llCurrentime.visibility = View.GONE
                    binding.llActualcost.visibility = View.GONE
                    binding.llAdditionalcost.visibility = View.VISIBLE
                    binding.btnStartnow.text = "Close"
                    binding.btnPayviacard.visibility = View.GONE
                    binding.llsplitamt.visibility = View.GONE
                    binding.llpayment.visibility = View.GONE
                    showPayment = false

                }
                booking_id = it.getString("bookingID", "")
                hitApitogettheBookingIDDetails(booking_id)
                binding.tvScreenstatus.text = "Exit"
                //     binding.btnStartandentergame.visibility = View.GONE

            }

        }

        if (dType.equals("njx")) {

            size = 23.0F
            tvdate_ar.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvdate.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_todaysdate.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvname_ar.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvname.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvemail_ar.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvemail.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvphone_ar.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvphone.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvCustomername.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvEmailaddrress.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvMobilenumber.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvadditionalamount2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvadditionalamount.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvadditionalmin2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvadditionalmin.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_discounttype.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_additionalmins2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_additionalmins.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_additionalamt2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_additionalamt.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_cardetailsvalue.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvcardname.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvpass.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvPaxtype.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvetime.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_entry_time.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvexittime.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_exittime.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvprice.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_price.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvticket.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_vat.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvvat.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvNooftickets.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvpayment.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_payment.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvtotal.textSize = resources.getDimension(com.intuit.sdp.R.dimen._9sdp)
            tv_total.textSize = resources.getDimension(com.intuit.sdp.R.dimen._9sdp)
//          tv_bookingid.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_noted.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvcustomercopy.textSize = resources.getDimension(com.intuit.sdp.R.dimen._9sdp)
            tvpowered.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvdate2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_todaysdate2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvname2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_Customername2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvemail2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvEmailaddrress2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvphone2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_mobilenumber2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvpass2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_paxtype2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvetime2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_entry_time2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            exittime2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_exittime2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvprice2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_price2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvticket2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_nooftickets2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvpayment2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_payment2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvtotal2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._9sdp)
            tvtile.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_total2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._9sdp)
//            tv_bookingid2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tv_ridename.textSize = resources.getDimension(com.intuit.sdp.R.dimen._8sdp)
            tv_noted2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvalert2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvalert.textSize = resources.getDimension(com.intuit.sdp.R.dimen._7sdp)
            tvmerchant2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._8sdp)
            tvpowered2.textSize = resources.getDimension(com.intuit.sdp.R.dimen._8sdp)
            tv_gamenames2nd.textSize = resources.getDimension(com.intuit.sdp.R.dimen._8sdp)
            title.textSize = resources.getDimension(com.intuit.sdp.R.dimen._8sdp)


        }

    }

    private fun setAdapter(listAccessories: List<AccessoryResponse>) {

        val accesoriesGroupAdapter = AccesoriesAdapter(
            requireContext(), listAccessories, currency
        )
        binding.rvaccessories.setAdapter(accesoriesGroupAdapter)
        binding.rvaccessories.setLayoutManager(LinearLayoutManager(requireContext()))

    }

    private fun hitApitogettheBookingIDDetails(bookingID: String) {
        viewModel.getBookingDetails(bookingID)
    }


}