//package com.a360play.a360nautica.view.activities
//
///*import com.ahmedelsayed.sunmiprinterutill.PrintMe
//import com.ahmedelsayed.sunmiprinterutill.model.AidlUtil
//import com.ahmedelsayed.sunmiprinterutill.model.BluetoothUtil*/
///*import com.ngx.mp100sdk.Intefaces.INGXCallback
//import com.ngx.mp100sdk.NGXPrinter*/
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.drawable.Drawable
//import android.os.Bundle
//import android.view.Gravity
//import android.view.View
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.fragment.app.FragmentManager
//import androidx.lifecycle.lifecycleScope
//import com.a360play.a360nautica.R
//import com.a360play.a360nautica.utils.BasePrintActivity
//import com.a360play.a360nautica.utils.BluetoothUtil
//import com.a360play.a360nautica.utils.ESCUtil
//import com.a360play.a360nautica.utils.SunmiPrintHelper
//import com.google.zxing.BarcodeFormat
//import com.google.zxing.MultiFormatWriter
//import com.google.zxing.WriterException
//import com.google.zxing.common.BitMatrix
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import java.text.DateFormat
//import java.text.SimpleDateFormat
//import java.util.*
//
//class ShowQRCode : BasePrintActivity() {
//
//    var bitmap: Bitmap? = null
//    var iv_barcode: ImageView? = null
//
//    // first receipt
//    lateinit var tvCustomername: TextView
//    lateinit var tvEntrytime: TextView
//    lateinit var tvEmailaddrress: TextView
//    lateinit var tvMobilenumber: TextView
//    lateinit var tvNooftickets: TextView
//    lateinit var tvPaxtype: TextView
//    lateinit var tv_bookingid: TextView
//    lateinit var ll_print: LinearLayout
//    lateinit var ll_print2: LinearLayout
//    lateinit var tv_ridename: TextView
//    lateinit var tv_price: TextView
//    lateinit var tv_payment: TextView
//    lateinit var tv_total: TextView
//    lateinit var tv_todaysdate: TextView
//
//    var myorientation = 0
//
//    // second receipt
//
//    lateinit var tv_gamenames2nd: TextView
//    lateinit var tv_todaysdate2nd: TextView
//    lateinit var tv_Customername2nd: TextView
//    lateinit var tvEmailaddrress2nd: TextView
//    lateinit var tv_mobilenumber2nd: TextView
//    lateinit var tv_paxtype2nd: TextView
//    lateinit var tv_entrytime2nd: TextView
//    lateinit var tv_price2nd: TextView
//    lateinit var tv_nooftickets2nd: TextView
//    lateinit var tv_payment2nd: TextView
//    lateinit var tv_total2nd: TextView
//    lateinit var tv_bookingid2nd: TextView
//    lateinit var iv_barcode2nd: ImageView
//    lateinit var tv_entry_time: TextView
//    lateinit var tv_exittime: TextView
//    lateinit var tv_entry_time2nd: TextView
//    lateinit var tv_exittime2nd: TextView
//    lateinit var tv_noted2nd: TextView
//    lateinit var tv_noted: TextView
//    lateinit var ll_entry_time2nd: LinearLayout
//    lateinit var ll_exittime2nd: LinearLayout
//    lateinit var ll_entry_time: LinearLayout
//    lateinit var ll_exittime: LinearLayout
//    lateinit var llaccessory: LinearLayout
//    lateinit var llaccessory2: LinearLayout
//    lateinit var bnt_Print: Button
//
//    /*  // NGX variable setup
//      private var ingxCallback: INGXCallback? = null
//      var ngxPrinter = NGXPrinter.getNgxPrinterInstance()*/
//    private val isBold = false
//    private var isUnderLine: kotlin.Boolean = false
//    private val record = 0
//    private var testFont: String? = null
//
//    //    var size_width = 256
//    var size_width = 200
//    var size_height = 66
//    var currency = ""
//    lateinit var shared: SharedPreferences
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_showqr)
//        initview()
//    }
//
//
///*
//    fun SunmiPrintOutHttpAsyncTask() {
//        val myExecutor = Executors.newSingleThreadExecutor()
//        val myHandler = Handler(Looper.getMainLooper())
//
//        myExecutor.execute {
//            try {
//                if (printMe == null) {
//                    printMe = PrintMe(this)
//                } else {
//                    printMe.sendViewToPrinter(ll_print)
//                    printMe.sendViewToPrinter(ll_print2)
//                }
//            } catch (e: java.lang.Exception) {
//                Toast.makeText(
//                    applicationContext,
//                    "Exception 5: " + e.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//        myHandler.post {
//            backtoHome()
//        }
//
//    }
//*/
//
//
///*
//    fun HttpAsyncTask(view: View) {
//        val myExecutor = Executors.newSingleThreadExecutor()
//        val myHandler = Handler(Looper.getMainLooper())
//
//        myExecutor.execute {
//            try {
//                if (view != null) {
//                    ngxPrinter.setDefault()
//                    ngxPrinter.setStyleDoubleWidth()
//                    ngxPrinter.setStyleBold()
//                    ngxPrinter.lineFeed(1)
//                    ngxPrinter.setStyleNormal()
//                    HttpAsyncTaskForNPXPtinter(ll_print)
//                    HttpAsyncTaskForNPXPtinter(ll_print2)
//
//                }
//                //  backtoHome()
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//                backtoHome()
//            }
//        }
//
//        myHandler.post {
//            // Do something in UI (front-end process)
//        }
//
//    }
//*/
//
///*
//    fun HttpAsyncTaskForNPXPtinter(view: View) {
//        val myExecutor = Executors.newSingleThreadExecutor()
//        val myHandler = Handler(Looper.getMainLooper())
//
//        myExecutor.execute {
//            try {
//                if (view != null) {
//                    ngxPrinter.printImage(loadBitmapFromView(view))
//                }
//                backtoHome()
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//                backtoHome()
//            }
//        }
//
//        myHandler.post {
//            // Do something in UI (front-end process)
//        }
//
//    }
//*/
//
//    fun loadBitmapFromView(view: View): Bitmap? {
//        view.setDrawingCacheEnabled(true)
//        view.buildDrawingCache(true)
//
//        view.setDrawingCacheEnabled(true)
//
//// this is the important code :)
//// Without it the view will have a dimension of 0,0 and the bitmap will be null
//
//// this is the important code :)
//// Without it the view will have a dimension of 0,0 and the bitmap will be null
//        view.measure(
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        )
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight())
//
//        view.buildDrawingCache(true)
//        val b: Bitmap = Bitmap.createBitmap(view.getDrawingCache())
//
////      val bitmap: Bitmap = view.getDrawingCache()
//        return b
//    }
//
//
//    private fun backtoHome() {
//        lifecycleScope.launch {
//            delay(2000)
//            val fm: FragmentManager = supportFragmentManager
//            if (fm.getBackStackEntryCount() > 0) {
//                for (i in 0 until fm.getBackStackEntryCount()) {
//                    fm.popBackStack()
//                }
//            }
//            finish()
//            val intent = Intent(this@ShowQRCode, MainActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//
//    private fun initview() {
//        shared = getSharedPreferences(
//            resources.getString(R.string.app_name),
//            Context.MODE_PRIVATE
//        )
//
//        testFont = null
//        currency = shared.getString("currency", "").toString()
//        testFont = "test.ttf"
//        ll_entry_time = findViewById(R.id.ll_entrytime)
//        ll_exittime = findViewById(R.id.ll_exittime)
//        llaccessory = findViewById(R.id.llaccessory)
//        llaccessory2 = findViewById(R.id.llaccessory2)
//
//        ll_entry_time2nd = findViewById(R.id.ll_entry2nd)
//        ll_exittime2nd = findViewById(R.id.ll_exit2nd)
//
//
//        tv_noted2nd = findViewById(R.id.tv_noted2nd)
//        tv_noted = findViewById(R.id.tv_noted)
//
//        tv_entry_time = findViewById(R.id.tv_entry_time)
//        tv_exittime = findViewById(R.id.tv_exittime)
//        tv_entry_time2nd = findViewById(R.id.tv_entry_time2nd)
//        tv_exittime2nd = findViewById(R.id.tv_exittime2nd)
//        tv_ridename = findViewById(R.id.tv_gamenames)
//        tv_todaysdate = findViewById(R.id.tv_todaysdate)
//        tv_total = findViewById(R.id.tv_total)
//        tv_payment = findViewById(R.id.tv_payment)
//        tv_price = findViewById(R.id.tv_price)
//        tv_bookingid = findViewById(R.id.tv_bookingid)
//        bnt_Print = findViewById(R.id.bnt_Print)
//        ll_print = findViewById(R.id.ll_print)
//        ll_print2 = findViewById(R.id.ll_print2)
//        iv_barcode = findViewById(R.id.iv_barcode)
//        tvCustomername = findViewById(R.id.tv_customername)
//        tvEmailaddrress = findViewById(R.id.tv_emailaddrress)
//        tvMobilenumber = findViewById(R.id.tv_mobilenumber)
//        tvPaxtype = findViewById(R.id.tv_paxtype)
//        tvEntrytime = findViewById(R.id.tv_entrytime)
//        tvNooftickets = findViewById(R.id.tv_nooftickets)
////        printMe = PrintMe(this)
//        iv_barcode = findViewById(R.id.iv_barcode)
//
//
//        // second prints
//        tv_gamenames2nd = findViewById(R.id.tv_gamenames2nd)
//        tv_todaysdate2nd = findViewById(R.id.tv_todaysdate2nd)
//        tv_Customername2nd = findViewById(R.id.tv_customername2nd)
//        tvEmailaddrress2nd = findViewById(R.id.tv_emailaddrress2nd)
//        tv_mobilenumber2nd = findViewById(R.id.tv_mobilenumber2nd)
//        tv_paxtype2nd = findViewById(R.id.tv_paxtype2nd)
//        tv_entrytime2nd = findViewById(R.id.tv_entrytime2nd)
//        tv_price2nd = findViewById(R.id.tv_price2nd)
//        tv_nooftickets2nd = findViewById(R.id.tv_nooftickets2nd)
//        tv_payment2nd = findViewById(R.id.tv_payment2nd)
//        tv_total2nd = findViewById(R.id.tv_total2nd)
//        tv_bookingid2nd = findViewById(R.id.tv_bookingid2nd)
//        iv_barcode2nd = findViewById(R.id.iv_barcode2nd)
//
//        setUpFirstReceipt()
//        setUpSecondReceipt()
////      goforPrint()
////        printContent()
//        bnt_Print.setOnClickListener(View.OnClickListener {
////            printContent()
//        })
//
//
//    }
//
//    private fun printContent() {
//        var content = ""
//        val size: Float = 22.0F
//        if (!com.a360play.a360nautica.utils.BluetoothUtil.isBlueToothPrinter) {
//            if (!com.a360play.a360nautica.utils.BluetoothUtil.isBlueToothPrinter) {
//
//                if (intent.getIntExtra("BookingID", 0) != -1) {
//
//                    if (intent.getStringExtra("start_time") == null) {
//                        ll_entry_time.visibility = View.GONE
//                        ll_exittime.visibility = View.GONE
//                        ll_entry_time2nd.visibility = View.GONE
//                        ll_exittime2nd.visibility = View.GONE
//                        SunmiPrintHelper.getInstance().setAlign(1)
//                        content = "360 Play" + "\n"
//                        content = content + "TRAMPOLINE" + "\n"
//                        content = content + ""
//                        SunmiPrintHelper.getInstance()
//                            .printText(content, size, isBold, isUnderLine, testFont)
//
//                        SunmiPrintHelper.getInstance()
//                            .printBitmap(loadBitmapFromView(ll_print), myorientation)
//
//                        content = ""
//                        content = "360 Play" + "\n"
//                        content = content + "Spider Men Home Coming" + "\n"
//                        content = content + ""
//                        SunmiPrintHelper.getInstance()
//                            .printText(content, size, isBold, isUnderLine, testFont)
//
//                        SunmiPrintHelper.getInstance().feedPaper()
//
//                        SunmiPrintHelper.getInstance()
//                            .printBitmap(loadBitmapFromView(ll_print2), myorientation)
//
//                        SunmiPrintHelper.getInstance().feedPaper()
//
//                    }
//                }
//
//            }
//        } else {
//            printByBluTooth(content)
//        }
//    }
//
//    private fun printByBluTooth(content: String) {
//        if (isBold) {
//            BluetoothUtil.sendData(ESCUtil.boldOn())
//        } else {
//            BluetoothUtil.sendData(ESCUtil.boldOff())
//        }
//        if (isUnderLine) {
//            BluetoothUtil.sendData(ESCUtil.underlineWithOneDotWidthOn())
//        } else {
//            BluetoothUtil.sendData(ESCUtil.underlineOff())
//        }
//        if (record < 17) {
//            BluetoothUtil.sendData(ESCUtil.singleByte())
//            BluetoothUtil.sendData(ESCUtil.setCodeSystemSingle(codeParse(record)))
//        } else {
//            BluetoothUtil.sendData(ESCUtil.singleByteOff())
//            BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(record)))
//        }
//
////            BluetoothUtil.sendData(content.getBytes(mStrings[record]));
//        BluetoothUtil.sendData(ESCUtil.nextLine(3))
//    }
//
//    private fun codeParse(value: Int): Byte {
//        var res: Byte = 0x00
//        when (value) {
//            0 -> res = 0x00
//            1, 2, 3, 4 -> res = (value + 1).toByte()
//            5, 6, 7, 8, 9, 10, 11 -> res = (value + 8).toByte()
//            12 -> res = 21
//            13 -> res = 33
//            14 -> res = 34
//            15 -> res = 36
//            16 -> res = 37
//            17, 18, 19 -> res = (value - 17).toByte()
//            20 -> res = 0xff.toByte()
//            else -> {}
//        }
//        return res
//    }
//
///*
//    private fun goforPrint() {
//        if (AidlUtil.getInstance().isConnect) {
//            SunmiPrintOutHttpAsyncTask()
//        } else if (ngxPrinter != null) {
//            HttpAsyncTask(ll_print)
//            HttpAsyncTask(ll_print2)
//        } else if (ngxPrinter == null) {
//            try {
//                ingxCallback = object : INGXCallback {
//                    override fun onRunResult(isSuccess: Boolean) {
//                        HttpAsyncTask(ll_print)
//                        HttpAsyncTask(ll_print2)
//                    }
//
//                    override fun onReturnString(result: String) {
//                        Log.i("NGX", "onReturnString:$result")
//                        Toast.makeText(
//                            applicationContext,
//                            "Exception 1: " + result,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        backtoHome()
//                    }
//
//                    override fun onRaiseException(code: Int, msg: String) {
//                        Log.i("NGX", "onRaiseException:$code:$msg")
//                        Toast.makeText(
//                            applicationContext,
//                            "Exception 2: " + "onRaiseException:$code:$msg", Toast.LENGTH_SHORT
//                        ).show()
//                        backtoHome()
//                    }
//                }
//                ngxPrinter!!.initService(this, ingxCallback)
//            } catch (e: Exception) {
//                Log.i("NGX", "onRaiseException:$e")
//                backtoHome()
//                Toast.makeText(this, "Exception 3: " + "onRaiseException:$e", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        } else {
//            Toast.makeText(
//                this,
//                "Your device not connected with any printing device ",
//                Toast.LENGTH_SHORT
//            ).show()
//            backtoHome()
//        }
//    }
//*/
//
//
//    private fun setUpSecondReceipt() {
//        if (intent.getIntExtra("BookingID", 0) != -1) {
//            var bookingID = intent.getIntExtra("BookingID", 0)
//            bitmap = CreateImageBarcode(bookingID.toString(), "Barcode")
//            iv_barcode2nd!!.setImageBitmap(bitmap)
//            val params: LinearLayout.LayoutParams =
//                iv_barcode2nd!!.getLayoutParams() as LinearLayout.LayoutParams
//            params.gravity = Gravity.CENTER
//            iv_barcode2nd!!.setLayoutParams(params)
//            tv_bookingid2nd.text = bookingID.toString()
//
//            tv_gamenames2nd.text = intent.getStringExtra("ridename")
//
//            var total_payment = intent.getIntExtra("CustomerTiclkets", 0) * intent.getIntExtra(
//                "price_per_picket",
//                0
//            )
//            tv_total2nd.text = currency + " " + total_payment.toString()
//
//            //"created": "2022-05-21T11:43:26.6257474"
//
//            var created_at = intent.getStringExtra("created_on")
//            val original_format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
//            original_format.timeZone = TimeZone.getTimeZone("UTC")
//            val date = original_format.parse(created_at)
//            original_format.timeZone = TimeZone.getDefault()
//            val formattedDate = original_format.format(date)
//
//            val targetFormat: DateFormat = SimpleDateFormat("MMMM dd yyyy")
//            val actual_date_format = original_format.parse(formattedDate)
//            val real_date = targetFormat.format(actual_date_format) // 20120821
//            tv_todaysdate2nd.text = real_date.toString()
//
//
//            tv_payment2nd.text = intent.getStringExtra("payment_type")
//            tv_price2nd.text = currency + " " + intent.getIntExtra("price_per_picket", 0).toString()
//            tv_Customername2nd.text = intent.getStringExtra("CustomerName")
//            tvEmailaddrress2nd.text = intent.getStringExtra("CustomerEmail")
//            tv_mobilenumber2nd.text = intent.getStringExtra("CustomerMobile")
//            tv_paxtype2nd.text = intent.getStringExtra("CustomerPattype")
//            tv_entrytime2nd.text = intent.getIntExtra("CustomerEntryTime", 0).toString() + "minutes"
//            tv_nooftickets2nd.text = intent.getIntExtra("CustomerTiclkets", 0).toString()
//            printContent()
//        }
//    }
//
//
//    fun setStartDateAndTime(start_time: String, duration: Int) {
//        try {
//            val original_format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
//            original_format.timeZone = TimeZone.getTimeZone("UTC")
//            val date = original_format.parse(start_time)
//            original_format.timeZone = TimeZone.getDefault()
//            val formattedDate = original_format.format(date)
//
//            val targetFormat: DateFormat = SimpleDateFormat("hh:mm a")
//            val actual_date_format = original_format.parse(formattedDate)
//            val real_date = targetFormat.format(actual_date_format) // 20120821
//            tv_entry_time.text = real_date
//            tv_entry_time2nd.text = real_date
//
//            val parsedDate = targetFormat.parse(real_date)
//            val calendar = Calendar.getInstance()
//            calendar.setTime(parsedDate)
//            calendar.add(Calendar.MINUTE, duration)
//            val formattedEndDate = targetFormat.format(calendar.getTime())
//            tv_exittime.text = formattedEndDate.toString()
//            tv_exittime2nd.text = formattedEndDate.toString()
//        } catch (e: Exception) {
//
//        }
//    }
//
//
///*
//    private fun setUpFirstReceipt() {
//        if (intent.getIntExtra("BookingID", 0) != -1) {
//            if (intent.getStringExtra("start_time") == null) {
//                ll_entry_time.visibility = View.GONE
//                ll_exittime.visibility = View.GONE
//                ll_entry_time2nd.visibility = View.GONE
//                ll_exittime2nd.visibility = View.GONE
//                tv_noted.setText(
//                    "Please Note : \nThe ticket is only valid on the mentioned \n" +
//                            "date. The customer should retain \n" +
//                            "this slip during activity time."
//                )
//                tv_noted2nd.setText(
//                    "Please Note :\nThe ticket is only valid on the mentioned \n" +
//                            "date. The customer should retain \n" +
//                            "this slip during activity time."
//                )
//            } else {
//                ll_entry_time.visibility = View.VISIBLE
//                ll_exittime.visibility = View.VISIBLE
//                ll_entry_time2nd.visibility = View.VISIBLE
//                ll_exittime2nd.visibility = View.VISIBLE
//                var duration = intent.getIntExtra("CustomerEntryTime", 0)
//                setStartDateAndTime(intent.getStringExtra("start_time")!!, duration)
//                tv_noted.setText(
//                    "Please Note :\nThe ticket is only valid on the mentioned\n" +
//                            "date and time. The customer should\nretain this slip during activity time."
//                )
//                tv_noted2nd.setText(
//                    "Please Note :\nThe ticket is only valid on the mentioned\n" +
//                            "date and time. The customer should \nretain this slip during activity time."
//                )
//            }
//            var bookingID = intent.getIntExtra("BookingID", 0)
//            bitmap = printMe.CreateImageBarcode(bookingID.toString(), "Barcode")
//            iv_barcode!!.setImageBitmap(bitmap)
//            val params: LinearLayout.LayoutParams =
//                iv_barcode!!.getLayoutParams() as LinearLayout.LayoutParams
//            params.gravity = Gravity.CENTER
//            iv_barcode!!.setLayoutParams(params)
//            tv_bookingid.text = bookingID.toString()
//
//            tv_ridename.text = intent.getStringExtra("ridename")
//
//            var total_payment = intent.getIntExtra("CustomerTiclkets", 0) * intent.getIntExtra(
//                "price_per_picket",
//                0
//            )
//            tv_total.text = total_payment.toString() + "   QAR"
//
//            //      "created": "2022-05-21T11:43:26.6257474",
//
//            var created_at = intent.getStringExtra("created_on")
//            val original_format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
//            original_format.timeZone = TimeZone.getTimeZone("UTC")
//            val date = original_format.parse(created_at)
//            original_format.timeZone = TimeZone.getDefault()
//            val formattedDate = original_format.format(date)
//
//            val targetFormat: DateFormat = SimpleDateFormat("MMMM dd yyyy")
//            val actual_date_format = original_format.parse(formattedDate)
//            val real_date = targetFormat.format(actual_date_format) // 20120821
//            tv_todaysdate.text = real_date.toString()
//
//
//            tv_payment.text = intent.getStringExtra("payment_type")
//            tv_price.text = intent.getIntExtra("price_per_picket", 0).toString() + "   QAR"
//            tvCustomername.text = intent.getStringExtra("CustomerName")
//            tvEmailaddrress.text = intent.getStringExtra("CustomerEmail")
//            tvMobilenumber.text = intent.getStringExtra("CustomerMobile")
//            tvPaxtype.text = intent.getStringExtra("CustomerPattype")
//            tvEntrytime.text = intent.getIntExtra("CustomerEntryTime", 0).toString() + "minutes"
//            tvNooftickets.text = intent.getIntExtra("CustomerTiclkets", 0).toString()
//
//        }
//    }
//*/
//
//    fun getBitmapFromView(view: View): Bitmap? {
//        //Define a bitmap with the same size as the view
//        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//        //Bind a canvas to it
//        val canvas = Canvas(returnedBitmap)
//        //Get the view's background
//        val bgDrawable: Drawable? = view.background
//        if (bgDrawable != null) //has background drawable, then draw it on the canvas
//            bgDrawable.draw(canvas) else  //does not have background drawable, then draw white background on the canvas
//            canvas.drawColor(Color.WHITE)
//        // draw the view on the canvas
//        view.draw(canvas)
//        //return the bitmap
//        return returnedBitmap
//    }
//
//    private fun setUpFirstReceipt() {
//        if (intent.getIntExtra("BookingID", 0) != -1) {
//            if (intent.getStringExtra("start_time") == null) {
//                ll_entry_time.visibility = View.GONE
//                ll_exittime.visibility = View.GONE
//                ll_entry_time2nd.visibility = View.GONE
//                ll_exittime2nd.visibility = View.GONE
//                tv_noted.setText(
//                    "Please Note : \nThe ticket is only valid on the mentioned \n" +
//                            "date. The customer should retain \n" +
//                            "this slip during activity time."
//                )
//                tv_noted2nd.setText(
//                    "Please Note :\nThe ticket is only valid on the mentioned \n" +
//                            "date. The customer should retain \n" +
//                            "this slip during activity time."
//                )
//            } else {
//                ll_entry_time.visibility = View.VISIBLE
//                ll_exittime.visibility = View.VISIBLE
//                ll_entry_time2nd.visibility = View.VISIBLE
//                ll_exittime2nd.visibility = View.VISIBLE
//                var duration = intent.getIntExtra("CustomerEntryTime", 0)
//                setStartDateAndTime(intent.getStringExtra("start_time")!!, duration)
//                tv_noted.setText(
//                    "Please Note :\nThe ticket is only valid on the mentioned\n" +
//                            "date and time. The customer should\nretain this slip during activity time."
//                )
//                tv_noted2nd.setText(
//                    "Please Note :\nThe ticket is only valid on the mentioned\n" +
//                            "date and time. The customer should \nretain this slip during activity time."
//                )
//            }
//            var bookingID = intent.getIntExtra("BookingID", 0)
//            val bitmap1: Bitmap =
//                CreateImageBarcode(bookingID.toString(), "Barcode")!!
//
////            bitmap = printMe.CreateImageBarcode(bookingID.toString(), "Barcode")
//            iv_barcode!!.setImageBitmap(bitmap1)
//            val params: LinearLayout.LayoutParams =
//                iv_barcode!!.getLayoutParams() as LinearLayout.LayoutParams
//            params.gravity = Gravity.CENTER
//            iv_barcode!!.setLayoutParams(params)
//            tv_bookingid.text = bookingID.toString()
//
//            tv_ridename.text = intent.getStringExtra("ridename")
//
//            var total_payment = intent.getIntExtra("CustomerTiclkets", 0) * intent.getIntExtra(
//                "price_per_picket",
//                0
//            )
//            tv_total.text = currency + " " + total_payment.toString()
//
//            //      "created": "2022-05-21T11:43:26.6257474",
//
//            var created_at = intent.getStringExtra("created_on")
//            val original_format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
//            original_format.timeZone = TimeZone.getTimeZone("UTC")
//            val date = original_format.parse(created_at)
//            original_format.timeZone = TimeZone.getDefault()
//            val formattedDate = original_format.format(date)
//
//            val targetFormat: DateFormat = SimpleDateFormat("MMMM dd yyyy")
//            val actual_date_format = original_format.parse(formattedDate)
//            val real_date = targetFormat.format(actual_date_format) // 20120821
//            tv_todaysdate.text = real_date.toString()
//
//
//            tv_payment.text = intent.getStringExtra("payment_type")
//            tv_price.text = currency + " " + intent.getIntExtra("price_per_picket", 0).toString()
//            tvCustomername.text = intent.getStringExtra("CustomerName")
//            tvEmailaddrress.text = intent.getStringExtra("CustomerEmail")
//            tvMobilenumber.text = intent.getStringExtra("CustomerMobile")
//            tvPaxtype.text = intent.getStringExtra("CustomerPattype")
//            tvEntrytime.text = intent.getIntExtra("CustomerEntryTime", 0).toString() + "minutes"
//            tvNooftickets.text = intent.getIntExtra("CustomerTiclkets", 0).toString()
//
//            /*printContent()*/
//        }
//    }
//
//    fun CreateImageBarcode(message: String?, type: String?): Bitmap? {
//        var bitMatrix: BitMatrix? = null
//        when (type) {
//            "Barcode" -> try {
//                bitMatrix = MultiFormatWriter().encode(
//                    message,
//                    BarcodeFormat.CODE_128,
//                    size_width,
//                    size_height
//                )
//            } catch (e: WriterException) {
//                e.printStackTrace()
//            }
//        }
//        val width = bitMatrix!!.width
//        val height = bitMatrix.height
//        val pixels = IntArray(width * height)
//        for (i in 0 until height) {
//            for (j in 0 until width) {
//                if (bitMatrix[j, i]) {
//                    pixels[i * width + j] = -0x1000000
//                } else {
//                    pixels[i * width + j] = -0x1
//                }
//            }
//        }
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
//        return bitmap
//    }
//
//}