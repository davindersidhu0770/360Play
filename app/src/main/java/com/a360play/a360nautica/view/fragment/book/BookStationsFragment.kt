package com.a360play.a360nautica.view.fragment.book


/*import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody*/

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.*
import android.text.style.StyleSpan
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseFragment
import com.a360play.a360nautica.data.booking.*
import com.a360play.a360nautica.databinding.FragmentBookstationsBinding
import com.a360play.a360nautica.ocr.CameraFragment
import com.a360play.a360nautica.utils.*
import com.a360play.a360nautica.view.activities.MainActivity
import com.a360play.a360nautica.view.adapter.*
import com.a360play.a360nautica.viewmodels.BookingViewModel
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient
import com.app.starterkit.utils.MyViewModelFactory
import com.app.starterkit.utils.Status
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.snatik.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

typealias CameraTextAnalyzerListener = (text: String) -> Unit

class BookStationsFragment : BaseFragment(), View.OnClickListener,
    GameAdapter.OnItemClickListener,
    PassesAdapter.OnItemClickListener,
    MultiSelPassesAdapter.OnItemClickListener,
    TypeOfGameAdapter.OnItemClickListener,
    TimesAdapter.OnItemClickListener,
    PassTimeAdapter.OnItemClickListener,
    GameDiscountsAdapter.OnItemClickListener,
    OnAccessoryClickListener,
    OnlyAccessoryClickListener {

    /*  private var timeIdString: String = ""
      private var passIdString: String = ""*/
    private lateinit var onlyAccessoryAdapter: OnlyAccessoryAdapter
    private lateinit var accessoryAdapter: AccessoryAdapter
    private lateinit var discountsAdapter: GameDiscountsAdapter
    private lateinit var timesAdapter: TimesAdapter
    private lateinit var passesAdapter: PassesAdapter
    private lateinit var multiPassesAdapter: MultiSelPassesAdapter
    private lateinit var typeOfGameAdapter: TypeOfGameAdapter
    private lateinit var passTimeAdapter: PassTimeAdapter
    private lateinit var gameAdapter: GameAdapter
    private var docFileToSend: File? = null
    private lateinit var nationalityList: ArrayList<NationalityDataResponse>
    private lateinit var prepaidList: ArrayList<PrepaidModeResponse>
    private lateinit var printData: Data
    private var gameClicked: Int = 0
    private var promocodeId: Int = 0
    private var gameId: Int = 0
    private var selectedGame: String = ""
    private lateinit var bodyChild: MultipartBody.Part
    private lateinit var body: MultipartBody.Part
    private var passClicked: Int = 0
    private var passSelected: String = ""
    private var ticketCount: Int = 1
    private var vatamount: Double = 0.0
    private var basePrice: Double = 0.0
    private var baseAdultPrice: Double = 0.0
    private var totalAdultAmount: Double = 0.0
    private var paymentMode: String = ""
    private var prepaidMode: String = ""
    private var typeOfGameClicked: Int = 0
    private var typeOfGameSelected: String = ""
    private var selectedTime: String = ""
    private var timeSelected: Int = 0
    private var numberOfPersons: Int = 0
    private var selectedAccessory: Int = 0
    private var selectedOnlyAccessory: Int = 0
    private var discountId: Int = 0
    private var isComplementarySelected: Boolean = false
    private var complementarySelected: Boolean = false
    private var ismonthlyPass: Boolean = false
    private var bogoselected: Boolean = false
    private var discountImage: String = ""
    private var compImage: String = ""
    private lateinit var progressDialog: ProgressDialog
    lateinit var binding: FragmentBookstationsBinding
    val apiService = RetrofitClient.apiService
    val repository = Repository(apiService)
    var tickets_count = 0
    var gameLlist: ArrayList<GamingListData> = ArrayList()
    var onlyAccessoryList: ArrayList<GameAccessoriesItem> = ArrayList()

    //    var onlyAccessoryList: ArrayList<GameAccessoriesItem> = ArrayList()
    var discountList: ArrayList<GameDiscounts> = ArrayList()
    lateinit var shared: SharedPreferences
    var selected_nationality = ""
    var selected_prepaidMode = ""
    var gameAccessoryBoth = true
    var confirmPrintButtonClicked = false
    var addMoreClicked = false
    var monthlyOrder = false

    //  var isMultiSelectedgame = false
    var childComp = false
    var alreadyClicked = false
    var cashCreditHandled = true
    var selected_code = ""
    var errMessage = ""
    var gender = ""
    var orderId = 0
    var countryId = 0
    var prepaidId = 0
    var Promocode = ""
    var mindate = "0001-01-01"
    var timeId = 0
    var barcode = ""
    var accessories_id = ""
    var currency = ""
    var country = ""
    var fieldUserName = ""

    //    var countrySelected = ""
    var count = 0
    var size_width = 200
    var size_height = 66
    var size: Float = 22.0F
    var myorientation = 0
    var forCameraImage: Boolean = true
    val isoCodesList: ArrayList<String> = ArrayList<String>()
    val countryList: ArrayList<Int> = ArrayList<Int>()
    lateinit var mBookingRequest: BookRequestData
    var accessoryJsonData = ""
    var value = 1
    var valid = true

    //    var viewClickable = true
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_CAPTURE_CHILD = 2
//  private val CAMERA_PERMISSION_REQUEST_CODE = 2

    private val STORAGE_PERMISSION_CODE = 1
    private val STORAGE_PERMISSION_CODE1 = 11
    private val CAMERA_PERMISSION_CODE = 2
    private val CAMERA_PERMISSION_CODE1 = 22
    private var startDate: Date? = null
    private var endDate: Date? = null
    private lateinit var suggestionsAdapter: ArrayAdapter<String>
    private var suggestionSelected = false
    var totalAmount: Double = 0.0
    var ticketTotalAmount: Double = 0.0
    var accessoryTotalAmount: Double = 0.0
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraControl: CameraControl
    private lateinit var cameraInfo: CameraInfo
    val suggestions = ArrayList<String>()
    private val selectedTimes = mutableListOf<Int>()
    private var selectedPasses = mutableListOf<Int>()


    // last changes 03-10-2023


    var passTimeArrayList: ArrayList<PassTimeResponse> = ArrayList<PassTimeResponse>()

    private val executor by lazy {
        Executors.newSingleThreadExecutor()
    }

    /*data class TypeOfgameresponse(val game: String, val times: List<TimesArray>)
    data class PassesResponse(val pass: String, val typeOfGames: List<TypeOfgameresponse>)
    data class Activity(val activity: String, val passes: List<PassesResponse>)
    data class TypeOfActivities(val typeOfActivities: List<Activity>)
   */

    private val viewModel: BookingViewModel by viewModels {
        MyViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookstationsBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(requireContext())
        binding.tvaddmore.setOnClickListener(View.OnClickListener {

            setRvSelectedPassAndTime(passTimeArrayList)
            addMoreClicked = true

            Log.d("22Aug:", "tvaddmore :" + timeId)

        })

        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the listener to receive the result from Fragment B
        parentFragmentManager.setFragmentResultListener("request_key", this) { _, result ->
            val receivedResult = result.getString("key_result")
            // Handle the received result here

            Log.d("4July: ", "receivedResult :" + receivedResult)
            // Access the parent activity and cast it to the appropriate activity class
            val parentActivity = requireActivity() as MainActivity

            // Now you can call any public method of the parent activity
            parentActivity.showBottomBar()

            if (forCameraImage)
                binding.etadditionalcomments.setText(receivedResult)
            else {
                binding.childcomp.visibility = View.VISIBLE
                binding.etchildcomp.setText(receivedResult)

            }

        }

    }

    private fun setRvSelectedPassAndTime(
        typeOfActivities: List<PassTimeResponse>
    ) {

        passTimeAdapter = PassTimeAdapter(typeOfActivities, requireContext(), this)
        val layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvselectedItems.layoutManager = layoutManager
        binding.rvselectedItems.visibility = View.VISIBLE
        binding.rvselectedItems.adapter = passTimeAdapter

    }

    private fun setRvTypeOfgame(typeOfActivities: List<GameTypeResponse>, selectedPos: Int) {
        typeOfGameAdapter = TypeOfGameAdapter(typeOfActivities, requireContext(), selectedPos, this)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvtypeofgame.layoutManager = layoutManager
        binding.rvtypeofgame.visibility = View.VISIBLE
        binding.rvtypeofgame.adapter = typeOfGameAdapter
        onTypeOfGameClicked(selectedPos, 0)

    }

    private fun setgameDiscounts(typeOfActivities: List<GameDiscounts>, selectedpos: Int) {
        discountsAdapter =
            GameDiscountsAdapter(typeOfActivities, selectedpos, requireContext(), this)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvdisocunts.layoutManager = layoutManager
        binding.rvdisocunts.visibility = View.VISIBLE
//      binding.lldiscount.visibility = View.VISIBLE
        binding.tvdisocunts.visibility = View.VISIBLE
        binding.rvdisocunts.adapter = discountsAdapter

    }

    private fun setRvGame(typeOfActivities: List<GamingListData>) {
        gameAdapter = GameAdapter(typeOfActivities, requireContext(), this)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvgame.layoutManager = layoutManager
        binding.rvgame.adapter = gameAdapter
        onGameSelected(0)//auto selecting first game and fruther..

    }

    private fun setRvGameAccessories(typeOfActivities: List<GameAccessoriesItem>) {
        accessoryAdapter = AccessoryAdapter(typeOfActivities, requireContext(), this)
        val layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvgameaccessories.layoutManager = layoutManager
        binding.tvgameaccessories.visibility = View.VISIBLE
        binding.titleaccessory.visibility = View.VISIBLE
        binding.rvgameaccessories.visibility = View.VISIBLE
        binding.rvgameaccessories.adapter = accessoryAdapter

    }

    private fun setRvOnlyAccessories(typeOfActivities: List<GameAccessoriesItem>) {
        onlyAccessoryAdapter = OnlyAccessoryAdapter(typeOfActivities, requireContext(), this)
        val layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvgameaccessories.layoutManager = layoutManager
        binding.tvgameaccessories.visibility = View.GONE
        binding.titleaccessory.visibility = View.GONE
        binding.rvgameaccessories.visibility = View.VISIBLE
        binding.rvgameaccessories.adapter = onlyAccessoryAdapter

    }

    private fun setRvPrintAccessory(typeOfActivities: List<PrintAccessory>) {
        val adapter = AccesoriesAdapterForPrint(
            requireContext(),
            typeOfActivities as ArrayList<PrintAccessory>?, currency
        )
        val layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvaccessories1.layoutManager = layoutManager
        binding.rvaccessories1.adapter = adapter

    }

    private fun setRecycelrviewforPass(typeOfActivities: List<GameTypePasses>, selectedpos: Int) {
        passesAdapter = PassesAdapter(typeOfActivities, selectedpos, requireContext(), this)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvpasses.layoutManager = layoutManager
        binding.rvpasses.visibility = View.VISIBLE
        binding.tvpassTitle.visibility = View.VISIBLE
        binding.rvpasses.adapter = passesAdapter

    }

    private fun setRecycelrviewforMultiSelPass(
        typeOfActivities: List<GameTypePasses>,
        selectedpos: Int
    ) {
        multiPassesAdapter =
            MultiSelPassesAdapter(typeOfActivities, selectedpos, requireContext(), this)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvpasses.layoutManager = layoutManager
        binding.rvpasses.visibility = View.VISIBLE
        binding.tvpassTitle.visibility = View.VISIBLE
        binding.rvpasses.adapter = multiPassesAdapter

    }

    private fun setRecycelrviewforTimes(typeOfActivities: List<GamePassTime>, selectedpos: Int) {
        timesAdapter = TimesAdapter(typeOfActivities, selectedpos, requireContext(), this, currency)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvtimes.layoutManager = layoutManager
        binding.rvtimes.visibility = View.VISIBLE
        binding.tvtimes.visibility = View.VISIBLE
        binding.rvtimes.adapter = timesAdapter

    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
        /* val test: Intent = Intent(requireActivity(), CaptureActivity::class.java)
         startActivity(test)*/
    }

    private fun dispatchTakePictureIntent1() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_CHILD)
        }
        /* val test: Intent = Intent(requireActivity(), CaptureActivity::class.java)
         startActivity(test)*/
    }


/*
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                // Permission denied, handle it accordingly
            }
        }
    }
*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // The image capture was successful, you can retrieve the image here
            val imageBitmap = data?.extras?.get("data") as Bitmap

            saveBitmapToFile(imageBitmap)
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE_CHILD && resultCode == Activity.RESULT_OK) {
            // The image capture was successful, you can retrieve the image here
            val imageBitmap = data?.extras?.get("data") as Bitmap
//            binding.childcomp.setImageBitmap(imageBitmap)
            // Do something with the captured image

            saveBitmapToFileChild(imageBitmap)
        }

    }

    private fun setDataFields(data: CustromerData) {
        orderId = data.orderId.toInt()
        binding.autoCompleteTextView.setText(data.name)
        binding.cbchildcomplementary.isChecked = false
        binding.childcomp.visibility = View.GONE

        if (!data.notes.isEmpty())
            binding.etnotes.setText(data.notes)
        else
            binding.etnotes.setText("")

        if (data.gender.equals("Male")) {
            gender = "Male"
            binding.llmale.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_yellow_selector
            )
            binding.llfemale.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )
        }
        if (data.gender.equals("Female")) {
            gender = "Female"
            binding.llfemale.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_yellow_selector
            )
            binding.llmale.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )
        }

        if (data.payType.equals("Cash")) {
            paymentMode = "Cash"
            prepaidMode = ""

            binding.llcash.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_yellow_selector
            )
            binding.llcredit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

            binding.llsplit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

        } else if (data.payType.equals("Card")) {
            paymentMode = "Card"
            prepaidMode = ""

            binding.llsplit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_yellow_selector
            )
            binding.llcash.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )
            binding.llcredit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

        } else if (data.payType.equals("Split")) {
            paymentMode = "Split"
            prepaidMode = ""

            binding.llsplit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_yellow_selector
            )
            binding.llcash.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )
            binding.llcredit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

        }

        //set type of game... 1june

        gameLlist.get(gameClicked).gameTypes.forEachIndexed { index, person ->
            println("Index: $index, Name: ${person.id}, Age: ${person.type}")
            if (data.typeId == person.id) {
                //also show selected pass too...
                setRvTypeOfgame(gameLlist.get(gameClicked).gameTypes, index)
                onTypeOfGameClickedOld(
                    index,
                    data.passId,
                    data.timeId
                )//to show selected passes list
                //set index to select pre selected pass...

            }

            val editable1 = Editable.Factory.getInstance().newEditable(data.noOfPax.toString())

            binding.edPax.text = editable1

        }

        if (!data.discountDetails.isEmpty()) {

            gameLlist.get(gameClicked).gameDiscounts.forEachIndexed { index, person ->
                if (data.discountDetails.equals(person.offerDetails)) {
                    //also show selected pass too...
                    setgameDiscounts(gameLlist.get(gameClicked).gameDiscounts, index)
                    onSelectedDiscountIdOld(
                        index
                    )
                }
            }

        }

        setSpinnerforNationality(nationalityList, true, data.countryId)

        if (!data.discountComment.isEmpty()) {
            binding.etadditionalcomments.setText(data.discountComment)

        }
        /*   if (!data.discountDetails.isEmpty()){
               binding.llpercentage.visibility=View.VISIBLE
               binding.edpercentage.setText(data.discountDetails)

           }
           else
           {
               binding.llpercentage.visibility=View.GONE

           }*/
        binding.tvpersons.text = "No. of Person : " + data.noOfPerson

        if (data.isMonthlyPass) {
            binding.llmonthlypass.visibility = View.GONE
            binding.startDateEditText.setText(convertDateFormat(data.passFromDate))
            binding.endDateEditText.setText(convertDateFormat(data.passToDate))

        } else
            binding.llmonthlypass.visibility = View.GONE

    }

    fun convertDateFormat(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.parse(inputDate, inputFormatter)
        return date.format(outputFormatter)
    }

    private fun bindObservers() {

        viewModel.customerDataResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    if (it.data!!.statusCode == 200) {
                        setDataFields(it.data.data)

                        ismonthlyPass = it.data.data.isMonthlyPass
                        if (it.data.data.isMonthlyPass) {
                            //disable view ....
                            disableAllViews()
                        } else {
                            enableAllViews()
                        }
                    } else {
                        Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                        orderId = 0
                        enableAllViews()

                    }

                }
                Status.ERROR -> {
                    orderId = 0

                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.booksucessResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Processing...", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    if (it.data!!.statusCode == 200) {
//                      Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                        printData = it.data.data

                        printContent(
                            resources.getString(R.string.customercopy_ar),
                            it.data.data,
                            "cust"
                        )
                    } else {

                        alreadyClicked = false
                        binding.btnContinue.isClickable = true
                        binding.btnContinue.isEnabled = true
                        binding.btnContinue.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_curve_green_button))


                        refreshAccessoryList(
                            it.data.message,
                            it.data.data.id,
                            it.data.data.mapId,
                            it.data.data.quantity
                        )
                        //show popup here...
//                      Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_LONG).show()

                    }

                }
                Status.ERROR -> {
                    alreadyClicked = false
                    binding.btnContinue.isClickable = true
                    binding.btnContinue.isEnabled = true
                    binding.btnContinue.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_curve_green_button))

                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.preiewResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    if (it.data!!.statusCode == 200) {
//                      Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        //show information with preview...
                        promocodeId = it.data.data.promocodeId
                        showPreviewDialog(it.data.data)

                    } else {
                        showError(requireContext(), it.data.message)
//                        Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                        confirmPrintButtonClicked = false
                    }

                }
                Status.ERROR -> {
                    confirmPrintButtonClicked = false
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.imageuploadResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    if (it.data!!.statusCode == 200) {
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        if (it.data.data.uniqueFileName != null)
                            discountImage = it.data.data.uniqueFileName
                        if (it.data.data.uniqueCompName != null)
                            compImage = it.data.data.uniqueCompName

                        Log.d("1June:", discountImage)
                    } else {

                    }
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                }
                Status.ERROR -> {
//                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.slotsResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    if (it.data!!.statusCode == 200) {

                        gameLlist.clear()

                        gameLlist = it.data.data.games as ArrayList<GamingListData>
                        onlyAccessoryList =
                            it.data.data.gameAccessories as ArrayList<GameAccessoriesItem>

                        nationalityList =
                            it.data.data.countries as ArrayList<NationalityDataResponse>
                        prepaidList = it.data.data.prepaidModes as ArrayList<PrepaidModeResponse>


                        setSpinnerforIsoCode(nationalityList)
                        setSpinnerforNationality(
                            nationalityList,
                            false,
                            0
                        )
                        setSpinnerforPrepaid(
                            prepaidList,
                            false,
                            0
                        )

                        if (gameLlist.size > 0) {
                            binding.tvtitle.text =
                                gameLlist.get(0).mall + ", " + gameLlist.get(0).game
                            gameId = gameLlist.get(0).id
                            setRvGame(gameLlist)

                        }

                    } else
                        Toast.makeText(
                            requireContext(), it.data
                                .message, Toast.LENGTH_SHORT
                        ).show()

                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.suggestionlistresponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
//                    Toast.makeText(requireContext(), "Loading.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    if (it.data!!.statusCode == 200) {

                        suggestions.clear()

                        for (i in 0 until it.data.data.size) {
                            val name = it.data.data.get(i).name
                            suggestions.add(name)
                            Log.d("11July:", "Size: " + suggestions.size)
                        }

                        suggestionsAdapter.clear()
                        suggestionsAdapter.addAll(suggestions)
                        suggestionsAdapter.notifyDataSetChanged()

                    } else {
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    }

                }
                Status.ERROR -> {
//                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

/*
        viewModel.nationalityResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    if (it.data!!.statusCode == 200) {

                        //sort arraylist on behalf of country coming in the login api response...
                        nationalityList = it.data.data


                        setSpinnerforIsoCode(nationalityList as ArrayList<NationalityDataResponse>)
                        setSpinnerforNationality(nationalityList as ArrayList<NationalityDataResponse>)
                    }

                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
*/

    }

    private fun refreshAccessoryList(message: String, id: Long, mapId: Int, quantity: Int) {

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                alertDialog.create().dismiss()

                for (item in onlyAccessoryList) {
                    if (id == item.id) {
                        for (item1 in item.listGameAccessory) {
                            if (mapId == item1.mapId)
                                item1.quantity = quantity
                        }
                    }
                }

//                onlyAccessoryAdapter.notifyDataSetChanged()

            }
        })
        alertDialog.show()

        /*  for (item in onlyAccessoryList) {
              for (item1 in item.listGameAccessory) {
                  item1.selectedQuantity = 0
              }

          }

          if (onlyAccessoryList.size > 0) {
              setRvOnlyAccessories(onlyAccessoryList)
              setRvGameAccessories(onlyAccessoryList)
          }*/
    }

    override fun onResume() {
        super.onResume()

        val parentActivity = requireActivity() as MainActivity

        // Now you can call any public method of the parent activity
        parentActivity.showBottomBar()

    }

    private fun disableAllViews() {

        binding.llenableDisable.isEnabled = false
        binding.llenableDisable.alpha = 0.5f
        binding.tvgender.setAlpha(0.5f) // Set alpha to 0.5 (semi-transparent)

        val viewsToDisable = listOf(
            binding.llmale,
            binding.llfemale,
            binding.llcash,
            binding.llcredit,
            binding.spNationality,
            binding.spPrepaid,
            binding.radioButton1,
            binding.radioButton2,
            binding.llgame,
            binding.rvgame,
            binding.rvtypeofgame,
            binding.rvpasses,
            binding.lldecrement,
            binding.lldecrement1,
            binding.llincrement,
            binding.llincrement1,
            binding.edpercentage,
            binding.lldiscount,
            binding.tvreset,
            binding.cbchildcomplementary,
            binding.etnotes,
            binding.childcomp,
            binding.startDateEditText,
            binding.endDateEditText,
            binding.etadditionalcomments
        )
        for (view in viewsToDisable) {
            view.isClickable = false
            view.isEnabled = false
        }
        typeOfGameAdapter.isClickable = false
        passesAdapter.isClickable = false
        timesAdapter.isClickable = false
        discountsAdapter.isClickable = false
        accessoryAdapter.isClickable = false
    }

    private fun enableAllViews() {

        binding.llenableDisable.isEnabled = true
        binding.llenableDisable.isClickable = true
        binding.llenableDisable.alpha = 1.0f  // Optional: Set alpha to indicate disabled state
        binding.tvgender.setAlpha(1.0f) // Set alpha to 0.5 (semi-transparent)

        val viewsToDisable = listOf(
            binding.llmale,
            binding.llfemale,
            binding.llcash,
            binding.llcredit,
            binding.spNationality,
            binding.spPrepaid,
            binding.radioButton1,
            binding.radioButton2,
            binding.llgame,
            binding.rvgame,
            binding.rvtypeofgame,
            binding.rvpasses,
            binding.lldecrement,
            binding.lldecrement1,
            binding.llincrement,
            binding.llincrement1,
            binding.edpercentage,
            binding.lldiscount,
            binding.tvreset,
            binding.cbchildcomplementary,
            binding.etnotes,
            binding.childcomp,
            binding.startDateEditText,
            binding.endDateEditText,
            binding.etadditionalcomments
        )
        for (view in viewsToDisable) {
            view.isClickable = true
            view.isEnabled = true
//          view.setOnClickListener(null) // Set empty OnClickListener to disable click events
        }
        typeOfGameAdapter.isClickable = true
        passesAdapter.isClickable = true
        timesAdapter.isClickable = true
        discountsAdapter.isClickable = true
        accessoryAdapter.isClickable = true
    }

    private fun showPreviewDialog(data: DataPreview) {

        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog_layout, null)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(dialogView)
        alertDialogBuilder.setCancelable(false)
//      alertDialogBuilder.setTitle("Confirm Ticket")
        val tvdaysleft = dialogView.findViewById<TextView>(R.id.tvdaysleft)
        val tvdiscount = dialogView.findViewById<TextView>(R.id.tvdiscount)
        val tvvat = dialogView.findViewById<TextView>(R.id.tvvat)
        val tvdiscountamt = dialogView.findViewById<TextView>(R.id.tvdiscountamt)
        val tvtotalAmt = dialogView.findViewById<TextView>(R.id.tvtotalAmt)
        val btncancel = dialogView.findViewById<TextView>(R.id.btncancel)
        val btnconfirm = dialogView.findViewById<TextView>(R.id.btnconfirm)
        val tvbaseprice = dialogView.findViewById<TextView>(R.id.tvbaseprice)
        val tvpax = dialogView.findViewById<TextView>(R.id.tvpax)
        val tvadultcount = dialogView.findViewById<TextView>(R.id.tvadultcount)
        val tvpaymode = dialogView.findViewById<TextView>(R.id.tvpaymode)
        val tvnoofpersons = dialogView.findViewById<TextView>(R.id.tvnoofpersons)
        val tvaccessoryAmt = dialogView.findViewById<TextView>(R.id.tvaccessoryAmt)
        val lloffer = dialogView.findViewById<LinearLayout>(R.id.lloffer)
        val llvat = dialogView.findViewById<LinearLayout>(R.id.llvat)
        val lldiscount = dialogView.findViewById<LinearLayout>(R.id.lldiscount)
        val lltotalamount = dialogView.findViewById<LinearLayout>(R.id.lltotalamount)
        val llaccessoryAmt = dialogView.findViewById<LinearLayout>(R.id.llaccessoryAmt)
        val lladult = dialogView.findViewById<LinearLayout>(R.id.lladult)
        val llpersons = dialogView.findViewById<LinearLayout>(R.id.llpersons)
        val llpax = dialogView.findViewById<LinearLayout>(R.id.llpax)
        val llchildcomp = dialogView.findViewById<LinearLayout>(R.id.llchildcomp)
        val llbaseprice = dialogView.findViewById<LinearLayout>(R.id.llbaseprice)
        val lldaysleft = dialogView.findViewById<LinearLayout>(R.id.lldaysleft)
        val llprepaid = dialogView.findViewById<LinearLayout>(R.id.llprepaid)
        val llsplitamt = dialogView.findViewById<LinearLayout>(R.id.llsplitamt)
        val cardAmountTextView = dialogView.findViewById<EditText>(R.id.cardAmountTextView)
        val cashAmountString = dialogView.findViewById<EditText>(R.id.cashAmountString)
        val cbispaymentcollected = dialogView.findViewById<CheckBox>(R.id.cbispaymentcollected)

        btnconfirm.isClickable = false
        btnconfirm.isEnabled = false
        btnconfirm.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_curve_grey_button))

        cbispaymentcollected.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {

                btnconfirm.isClickable = true
                btnconfirm.isEnabled = true
                btnconfirm.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_curve_green_button))

            } else {
                //disable button...
                btnconfirm.isClickable = false
                btnconfirm.isEnabled = false
                btnconfirm.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_curve_grey_button))

            }
        }

        if (paymentMode.contentEquals("Prepaid")) {

            tvpaymode.text = prepaidMode

        } else {
            tvpaymode.text = paymentMode

        }
        if (childComp)
            llchildcomp.visibility = View.VISIBLE
        else
            llchildcomp.visibility = View.GONE

        if (data.isMonthlyPass)
            lldaysleft.visibility = View.VISIBLE
        else
            lldaysleft.visibility = View.GONE

        if (data.discount.equals("NA"))
            lloffer.visibility = View.GONE
        else
            lloffer.visibility = View.VISIBLE

        if (data.noOfPax.equals("NA"))
            llpax.visibility = View.GONE
        else
            llpax.visibility = View.VISIBLE

        if (data.basePrice > 0)
            llbaseprice.visibility = View.VISIBLE
        else
            llbaseprice.visibility = View.GONE

        if (data.totalAccessoryAmount > 0)
            llaccessoryAmt.visibility = View.VISIBLE
        else
            llaccessoryAmt.visibility = View.GONE

        if (data.persons == 0)
            llpersons.visibility = View.GONE
        else
            llpersons.visibility = View.VISIBLE

        if (data.totalVATAmount > 0)
            llvat.visibility = View.VISIBLE
        else
            llvat.visibility = View.GONE

        if (data.discountAmount > 0)
            lldiscount.visibility = View.VISIBLE
        else
            lldiscount.visibility = View.GONE

        if (data.personEntryCount > 0)
            lladult.visibility = View.VISIBLE
        else
            lladult.visibility = View.GONE

        tvdiscount.text = data.discount
        tvvat.text = currency + " " + data.totalVATAmount.toString()
        tvdiscountamt.text = currency + " " + data.discountAmount.toString()
        tvtotalAmt.text = currency + " " + data.totalAmount.toString()
        tvdaysleft.text = data.gamesLeft.toString() + " day(s)"
        tvbaseprice.text = currency + " " + data.basePrice.toString()
        tvaccessoryAmt.text = currency + " " + data.totalAccessoryAmount.toString()
        tvpax.text = data.noOfPax.toString()
        tvadultcount.text = data.personEntryCount.toString()
        tvnoofpersons.text = data.persons.toString()
        var cashAmount = 0.0

        // if payment mode is split show cash and credit option here only...
        if (paymentMode.equals("Split")) {

            if (data.totalAmount > 0) {
                llsplitamt.visibility = View.VISIBLE

                //handle case where user have selected Accessories and Game both...

                if (gameAccessoryBoth && accessoryTotalAmount > 0) {
//                  cardAmountTextView.setText((data.totalAmount - accessoryTotalAmount).toString())

                    cardAmountTextView.setText(
                        String.format(
                            "%.2f",
                            (data.totalAmount - accessoryTotalAmount)
                        )
                    )

                    Log.d("22august", "accessoryTotalAmount " + accessoryTotalAmount)
                    Log.d("22august", "lentgh " + accessoryTotalAmount)

                    val formattedValue = String.format("%.1f", accessoryTotalAmount)
                    val cursorPosition = formattedValue.indexOf('.')
                    if (cursorPosition != -1) {
                        cashAmountString.setText(formattedValue)
                        cashAmountString.setSelection(cursorPosition)
                    } else {
                        cashAmountString.setText(formattedValue)
                        cashAmountString.setSelection(formattedValue.length)
                    }

                    val imm =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

//                  cashAmountString.setText(accessoryTotalAmount.toString())

                } else {
//                  cardAmountTextView.setText(data.totalAmount.toString())

                    cardAmountTextView.setText(String.format("%.2f", data.totalAmount))

                    cashAmountString.setText("0")
                    cashAmountString.setSelection(1)

                }

            } else
                llsplitamt.visibility = View.GONE
        } else {
            llsplitamt.visibility = View.GONE

            /*  if (data.totalAmount > 0) {
                  lltotalamount.visibility = View.VISIBLE
              } else
                  lltotalamount.visibility = View.GONE*/

        }

        cashAmountString.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                // Get the cash amount entered by the user
                val cashAmountString1 = cashAmountString.text.toString()
                cashAmount = cashAmountString1.toDoubleOrNull() ?: 0.0
                // Validate cash amount not exceeding total amount
                /*if (cashAmount > data.totalAmount) {
                    cashAmountString.error = "Cash amount cannot exceed total amount"
                    cashCreditHandled = false
                    return
                } else {
                    // Calculate the card amount
                    val cardAmount = data.totalAmount - cashAmount
                    cashCreditHandled = true
                    cardAmountTextView.setText(cardAmount.toString())

                }*/

                // Validate cash amount not exceeding total amount
                if (cashAmount > data.totalAmount) {
                    cashAmountString.error = "Cash amount cannot exceed total amount"
                    errMessage = "Cash amount cannot exceed total amount"
                    cashCreditHandled = false
                    return
                } else {
                    if (gameAccessoryBoth) {
                        if (cashAmount < accessoryTotalAmount) {
                            cashAmountString.error =
                                "Minimum cash amount is " + accessoryTotalAmount
                            cashCreditHandled = false
                            errMessage = "Minimum cash amount is " + accessoryTotalAmount

                            return
                        } else {
                            // Calculate the card amount
                            val cardAmount = data.totalAmount - cashAmount
                            cashCreditHandled = true

                            cardAmountTextView.setText(String.format("%.2f", cardAmount))

                        }
                    } else {
                        val cardAmount = data.totalAmount - cashAmount
                        cashCreditHandled = true
                        cardAmountTextView.setText(String.format("%.2f", cardAmount))

                    }

                }


            }
        })

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        btncancel.setOnClickListener(View.OnClickListener {

            confirmPrintButtonClicked = false
            alertDialog.dismiss()
            binding.btnContinue.isClickable = true
            binding.btnContinue.isEnabled = true
            binding.btnContinue.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_curve_green_button))

        })

        btnconfirm.setOnClickListener(View.OnClickListener {

            //confirm and hit booking api...

            //set a boolean here if already clicked don't send another request...
            binding.btnContinue.isClickable = false
            binding.btnContinue.isEnabled = false
            binding.btnContinue.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_curve_grey_button))

            Log.d("16oct", "btnconfirm alreadyClicked :" + alreadyClicked)

            if (!alreadyClicked) {

                alreadyClicked = true
                Log.d("16oct", "btnconfirm alreadyClicked :" + alreadyClicked)

                if (cashCreditHandled) {
                    if (paymentMode.equals("Split")) {

//                  Log.d("5Julyy:",cashAmountString.text.toString())
                        if (cashAmountString.text.toString().isEmpty())
                            hitPrintApi(0.0)
                        else
                            hitPrintApi(cashAmountString.text.toString().toDouble())

                    } else {

                        // in case of case send cashamount otherwise 0
                        if (paymentMode.equals("Cash"))
                            hitPrintApi(data.totalAmount)
                        else
                            hitPrintApi(0.0)
                    }


                    alertDialog.dismiss()

                } else {

                    alreadyClicked = false

                    Log.d("16oct", "ERROR alreadyClicked :" + alreadyClicked)

                    Toast.makeText(
                        requireContext(),
                        errMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }


        })

        /* alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
             // Handle OK button click here
         }
         alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
             // Handle Cancel button click here
         }*/

    }

    fun setDecimalInputFilter(editText: EditText) {
        val decimalFilter = object : InputFilter {
            private val pattern = Regex("[0-9]*\\.?[0-9]*")

            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                val input = dest.toString().substring(0, dstart) + source.toString()
                    .substring(start, end) + dest.toString().substring(dend)
                return if (pattern.matches(input)) {
                    null // Accept the input as it matches the decimal pattern
                } else {
                    "" // Reject the input
                }
            }
        }

        val filters = editText.filters.toMutableList()
        filters.add(decimalFilter)
        editText.filters = filters.toTypedArray()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shared = requireActivity().getSharedPreferences(
            resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
        currency = shared.getString("currency", "").toString()
        fieldUserName = shared.getString("username", "").toString()
        binding.tvfieldusername.text = "Welcome " + fieldUserName
        accessories_id = ""

        /* binding.edMobilenumber.inputType = InputType.TYPE_CLASS_NUMBER
         // Set an input filter to restrict the input to numbers only
         val numberFilter = InputFilter { source, _, _, _, _, _ ->
             if (source != null && !source.toString().matches(Regex("[0-9]*"))) {
                 // Filter out non-numeric characters
                 return@InputFilter ""
             }
             null
         }
         binding.edMobilenumber.filters = arrayOf(numberFilter)*/

        binding.llincrement.setOnClickListener {
            if (!binding.edPax.text.toString().isEmpty())
                value = binding.edPax.text.toString().toInt()
            value++
            val editable = Editable.Factory.getInstance().newEditable(value.toString())

            binding.edPax.text = editable
            updatecashCreditAmount()
        }

        binding.llincrement1.setOnClickListener {
            if (!binding.edAdult.text.toString().isEmpty())
                value = binding.edAdult.text.toString().toInt()
            value++
            val editable = Editable.Factory.getInstance().newEditable(value.toString())

            binding.edAdult.text = editable
            updateAdultAmount()
        }

        binding.lldecrement.setOnClickListener {
            if (!binding.edPax.text.toString().isEmpty())
                value = binding.edPax.text.toString().toInt()
            if (value > 1)
                value--
            val editable = Editable.Factory.getInstance().newEditable(value.toString())

            binding.edPax.text = editable
            updatecashCreditAmount()
        }

        binding.lldecrement1.setOnClickListener {
            if (!binding.edAdult.text.toString().isEmpty())
                value = binding.edAdult.text.toString().toInt()
            if (value >= 1)
                value--
            val editable = Editable.Factory.getInstance().newEditable(value.toString())

            binding.edAdult.text = editable
            updateAdultAmount()
        }

        initview()
        hideStatusBar()
        /*setSpinnerforNationality()*/
        listeners()
        bindObservers()
        if (checkForInternet(requireContext())) {
//          hitApitogetNationality()
            viewModel.getGamesByUserId(shared.getString("userID", "")!!.toInt())

        } else {
            CustomSnackbar.make(
                requireActivity().window.decorView.rootView as ViewGroup,
                "No Internet Connection..."
            ).show()
        }

    }

    private fun updatecashCreditAmount() {

        totalAmount =
            (Integer.parseInt(binding.edPax.text.toString()) * basePrice) + accessoryTotalAmount

        binding.cardAmountTextView.setText(totalAmount.toString())
        binding.cashAmountString.setText("0")
    }

    private fun updateAdultAmount() {

        totalAdultAmount = (Integer.parseInt(binding.edAdult.text.toString()) * baseAdultPrice)

    }

    private fun updatecashOnlyAccesoryAmount() {

        totalAmount = (0 * basePrice) + accessoryTotalAmount

        binding.cardAmountTextView.setText(totalAmount.toString())
        binding.cashAmountString.setText("0")
    }

    fun validateEditTextNumber(number: String): Boolean {

        /*  val allowedPrefixes = listOf("50", "52", "54", "55", "56", "58")
          val input = binding.edMobilenumber.text.toString()

          val isValid = allowedPrefixes.any { input.startsWith(it) }

          if (isValid) {
              return true
              // The input starts with one of the allowed prefixes
              // Proceed with your desired logic
          } else {
              return false
              // The input does not match the required pattern
              // Display an error message or take appropriate action
          }
  */

        // val pattern = Regex("^(050|052|054|055|056|058)\\d{6}$")
        val pattern = Regex("^(50|52|54|55|56|58)\\d{7}$")
        return pattern.matches(number)
    }

    // Apply the input filter with maximum length to an EditText
    fun setNumericInputFilterWithMaxLength(editText: EditText, maxLength: Int) {
        val filter = object : InputFilter {
            private val pattern = "\\d+".toRegex()

            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                val input = dest?.subSequence(0, dstart).toString() +
                        source?.subSequence(start, end) +
                        dest?.subSequence(dend, dest.length).toString()

                if (input.isEmpty()) {
                    return null
                }

                if (input.matches(pattern) && input.length <= maxLength) {
                    return null // Accept the input as it is
                }


                return "" // Reject the input by returning an empty string
            }
        }

        editText.filters = arrayOf(filter, InputFilter.LengthFilter(maxLength))
    }

    fun calculateEndDate(startDate: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(startDate)
        calendar.add(Calendar.DAY_OF_YEAR, 30)
        val endDate = calendar.time
        return dateFormat.format(endDate)
    }

    fun onTextReceivedFromActivity(text: String) {
        // Handle the received text here
        Log.d("23June: Received", text)
    }

    private fun initview() {
        suggestionsAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)

        binding.autoCompleteTextView.setAdapter(suggestionsAdapter)
        binding.autoCompleteTextView.threshold = 2

        binding.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Not used in this example
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used in this example
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 in 1..3) {

                    Log.d("11July:", "Lentgh :" + s?.length)
                    // Make API call and update suggestions
                    viewModel.getNamesList((s.toString()))
                }
            }
        })

        val titleText: String = binding.autoCompleteTextView.text.toString()

        val spannableStringBuilder = SpannableStringBuilder(titleText)
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            titleText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.autoCompleteTextView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE)

        binding.checkbox.setOnCheckedChangeListener { compoundButton, b ->
            monthlyOrder = b
        }

        binding.cbchildcomplementary.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                childComp = true
                binding.rlchildcomp.visibility = View.VISIBLE
                binding.childcomp.visibility = View.VISIBLE
            } else {
                childComp = false
                binding.rlchildcomp.visibility = View.GONE
                binding.childcomp.visibility = View.GONE
                binding.etchildcomp.setText("")
            }
        }
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton1 -> {
                    gameAccessoryBoth = true
                    accessoryTotalAmount = 0.0
                    childComp = false
                    binding.rlchildcomp.visibility = View.GONE
                    binding.childcomp.visibility = View.GONE
                    binding.cbchildcomplementary.isChecked = false
                    binding.etchildcomp.setText("")

                    // Option 1 selected
//                  Toast.makeText(requireContext(), "Option 1 selected", Toast.LENGTH_SHORT).show()
                    //hide only accessories...
//                  binding.rvgame.visibility = View.VISIBLE
                    binding.llpax.visibility = View.VISIBLE
                    binding.rvgameaccessories.visibility = View.GONE
                    binding.llgame.visibility = View.VISIBLE
                    binding.llnotes.visibility = View.VISIBLE
                    binding.tvbogo.visibility = View.GONE
                    binding.llsplitamt.visibility = View.GONE
                    binding.tvgameaccessories.text = ""
                    binding.etnotes.setText("")
                    binding.llpercentage.visibility = View.GONE
                    binding.lldiscount.visibility = View.GONE
                    refreshAllLists()
                }
                R.id.radioButton2 -> {
                    binding.llsplitamt.visibility = View.GONE
                    accessoryTotalAmount = 0.0
                    totalAmount = accessoryTotalAmount
                    updatecashOnlyAccesoryAmount()
                    gameAccessoryBoth = false
                    binding.etadditionalcomments.setText("")
                    discountId = 0 //aaaaaa
                    timeId = 0 //aaaaaa
                    childComp = false
                    binding.etchildcomp.setText("")
                    complementarySelected = false
                    Promocode = ""
                    binding.edpercentage.setText("")
                    binding.rlchildcomp.visibility = View.GONE
                    binding.childcomp.visibility = View.GONE
                    binding.cbchildcomplementary.isChecked = false

                    // Option 2 selected
//                  Toast.makeText(requireContext(), "Option 2 selected", Toast.LENGTH_SHORT).show()
                    binding.llmonthlypass.visibility = View.GONE
                    binding.llgame.visibility = View.GONE
                    binding.rvgame.visibility = View.GONE
                    binding.llpax.visibility = View.GONE
                    binding.llnotes.visibility = View.GONE

                    for (item in onlyAccessoryList) {
                        for (item1 in item.listGameAccessory) {
                            item1.selectedQuantity = 0
                        }

                    }

                    if (onlyAccessoryList.size > 0) {
//                      binding.tvgameaccessories.text = onlyAccessoryList.get(0).accessory
                        setRvOnlyAccessories(onlyAccessoryList)
                    } else {

                        Toast.makeText(requireContext(), "No Accessories", Toast.LENGTH_SHORT)
                            .show()

                    }

                }
            }
        }

/*
        binding.rldiscountimage.setOnClickListener(View.OnClickListener {
            // Request camera permission
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                // Permission already granted, proceed with capturing image
                dispatchTakePictureIntent()
            }
        })
*/

        binding.rldiscountimage.setOnClickListener(View.OnClickListener {
            // Request camera permission
//            requestPermissions() //modified here...

            // navigate to next screen and fetch results of ocr...
            navigateToFragmentCamera()
            forCameraImage = true

        })

        binding.rlchildcomp.setOnClickListener(View.OnClickListener {
            // Request camera permission
            // navigate to next screen and fetch results of ocr...
            navigateToFragmentCamera()
            forCameraImage = false

        })

    }

    // Method to navigate from Fragment A to Fragment B
    private fun navigateToFragmentCamera() {
        // Perform any necessary operations before navigating

        val fragmentB = CameraFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .hide(this)
            .add(R.id.ll_fragment, fragmentB) // Add Fragment B on top of Fragment A
            .addToBackStack(null)
            .commit()
    }

    @Suppress("SameParameterValue")
    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension
        )

    private fun getOutputDirectory(context: Context): File {

        val storage = Storage(context)
        val mediaDir = storage.internalCacheDirectory?.let {
            File(it, "Intelligible OCR").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

    private fun takePicture() {

        val file = createFile(
            getOutputDirectory(
                requireContext()
            ),
            "yyyy-MM-dd-HH-mm-ss-SSS",
            ".png"
        )
        val outputFileOptions =
            ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(
            outputFileOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {


                    // sending the captured image for analysis
                    GlobalScope.launch(Dispatchers.IO) {
                        TextAnalyser({ result ->
                            if (result.isEmpty()) {

                                progressDialog.dismiss()
                                Toast.makeText(
                                    requireContext(),
                                    "No Text Detected",
                                    Toast.LENGTH_SHORT
                                ).show()


                            } else {

                                progressDialog.dismiss()
                                binding.etadditionalcomments.setText(result)
/*
                                findNavController().navigate(
                                    R.id.action_cameraFragment_to_infoDisplayFragment,
                                    Bundle().apply {
                                        putString("text", result)
                                        putString("language", currentLanguage)
                                    })
*/
                            }

                        }, requireContext(), Uri.fromFile(file)).analyseImage()

                    }
                }

                override fun onError(exception: ImageCaptureException) {

                    progressDialog.dismiss()
                    Log.e("error", exception.localizedMessage!!)
                }
            })
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        val permissionToRequest = ArrayList<String>()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionToRequest.add(permission)
            }
        }

        if (permissionToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionToRequest.toTypedArray(),
                if (permissionToRequest.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    STORAGE_PERMISSION_CODE
                else
                    CAMERA_PERMISSION_CODE
            )
        } else {
            // All permissions already granted, handle the operation
            dispatchTakePictureIntent()


        }
    }

    private fun requestPermissions1() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        val permissionToRequest = ArrayList<String>()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionToRequest.add(permission)
            }
        }

        if (permissionToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionToRequest.toTypedArray(),
                if (permissionToRequest.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    STORAGE_PERMISSION_CODE1
                else
                    CAMERA_PERMISSION_CODE1
            )
        } else {
            // All permissions already granted, handle the operation
            dispatchTakePictureIntent1()


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Storage permission granted, now request the camera permission
                requestPermissions()
            } else {
                // Storage permission denied, handle lack of permissions
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Storage permission granted, now request the camera permission
                requestPermissions1()
            } else {
                // Storage permission denied, handle lack of permissions
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, handle the operation
                dispatchTakePictureIntent()

            } else {
                // Camera permission denied, handle lack of permissions
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, handle the operation
                dispatchTakePictureIntent1()

            } else {
                // Camera permission denied, handle lack of permissions
            }
        }
    }

    private fun refreshAllLists() {

        selectedOnlyAccessory = 0
        discountId = 0
        selectedAccessory = 0
        typeOfGameSelected = ""
        passSelected = ""
        selectedTime = ""
        binding.tvbaseprice.visibility = View.GONE
        binding.tvpersons.visibility = View.GONE
        binding.tvgameaccessories.visibility = View.GONE
        binding.tvtypeofgame.visibility = View.GONE
        binding.tvtimes.visibility = View.GONE
        binding.rvtimes.visibility = View.GONE
        setRvGame(gameLlist)

        for (item in onlyAccessoryList) {
            for (item1 in item.listGameAccessory) {
                item1.selectedQuantity = 0
            }

        }

        setRvGameAccessories(onlyAccessoryList)

    }

    private fun setSpinnerforPrepaid(
        prepaidModeResponse: ArrayList<PrepaidModeResponse>,
        comingFromOld: Boolean,
        countryId1: Int
    ) {

        prepaidModeResponse.add(0, PrepaidModeResponse(0, "Select Prepaid Mode"))

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_game,
            R.id.text122,
            prepaidModeResponse
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_game)
        binding.spPrepaid.adapter = adapter

        binding.spPrepaid.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                selected_prepaidMode = ""
                prepaidMode = ""
                paymentMode = ""
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selected_prepaidMode = binding.spPrepaid.selectedItem.toString()
                prepaidId = prepaidList.get(position).id


                if (position != 0) {

                    binding.radioButton2.isEnabled = false
                    binding.radioButton2.isClickable = false
                    binding.radioButton1.isChecked = true

                    //hiding the accessory list and making selected item count to 0..
                    for (item in onlyAccessoryList) {
                        for (item1 in item.listGameAccessory) {
                            item1.selectedQuantity = 0
                        }

                    }

                    setRvGameAccessories(onlyAccessoryList)
                    binding.tvgameaccessories.visibility = View.GONE
                    binding.titleaccessory.visibility = View.GONE
                    binding.rvgameaccessories.visibility = View.GONE

                    prepaidMode = prepaidList.get(position).mode
                    paymentMode = "Prepaid"
                    Log.d("16oct:", "paymentMode :" + paymentMode)
                    Log.d("17oct:", "position :" + position)

                    binding.llsplit.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.corner_curve_normal_selector
                    )
                    binding.llcredit.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.corner_curve_normal_selector
                    )

                    binding.llcash.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.corner_curve_normal_selector
                    )
                } else {

                    prepaidMode = ""
//                  paymentMode = ""
                    binding.radioButton2.isEnabled = true
                    binding.radioButton2.isClickable = true


                    binding.tvgameaccessories.visibility = View.VISIBLE
                    binding.titleaccessory.visibility = View.VISIBLE
                    binding.rvgameaccessories.visibility = View.VISIBLE

                    Log.d("17oct:", "else :" + paymentMode)

                }

            }
        }

        if (comingFromOld) {
            prepaidModeResponse.forEachIndexed { index, person ->
                if (countryId1 == person.id) {
                    //also show selected pass too...
                    binding.spPrepaid.setSelection(index)

                }
            }
        }
    }

    private fun setSpinnerforNationality(
        nationalityData: ArrayList<NationalityDataResponse>,
        comingFromOld: Boolean,
        countryId1: Int
    ) {

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_game,
            R.id.text122,
            nationalityData
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_game)
        binding.spNationality.adapter = adapter

        binding.spNationality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                selected_nationality = ""
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selected_nationality = binding.spNationality.selectedItem.toString()
                Log.d("SelectedN>>>", selected_nationality)
                countryId = countryList.get(position)
                Log.d("23May:", "countryId :" + countryList.get(position))

            }
        }

        if (comingFromOld) {
            nationalityData.forEachIndexed { index, person ->
                if (countryId1 == person.id) {
                    //also show selected pass too...
                    binding.spNationality.setSelection(index)

                }
            }
        }
    }

    private fun setSpinnerforIsoCode(isocode: ArrayList<NationalityDataResponse>) {

        isoCodesList.clear()
        countryList.clear()
        for (i in 0 until isocode.size) {
            isoCodesList.add(isocode.get(i).isdCode + "(" + isocode.get(i).countryName + ")")
        }

        for (i in 0 until isocode.size) {
            countryList.add(isocode.get(i).id)
        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.layout_isdcode,
            R.id.text122,
            isoCodesList
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_game)
        binding.spcodes.adapter = adapter

        binding.spcodes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                selected_code = ""
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selected_code = binding.spcodes.selectedItem.toString()
                country = isocode.get(position).countryName
                Log.d("SelectedN>>>", selected_code.substringBefore("("))
                Log.d("SelectedN>>>", "country " + country)
                updateCount()
            }
        }
    }

    private fun updateCount() {
        if (country.equals("UAE")) {
            count = 9
            setNumericInputFilterWithMaxLength(binding.edMobilenumber, count)

            binding.edMobilenumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Do nothing
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    // Do nothing
                }

                override fun afterTextChanged(s: Editable?) {
                    val number = s.toString()
                    if (number.length == count) {
                        if (validateEditTextNumber(number)) {
                            valid = true

                        } else {

                            valid = false
                            Toast.makeText(
                                requireContext(),
                                "Number is not valid",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            })

        } else {
            // no validation...
            /*count = 8
            setNumericInputFilterWithMaxLength(binding.edMobilenumber, count)*/
            count = 0
        }

    }

    private fun hitApitogetNationality() {
        if (checkForInternet(requireContext())) {
//            viewModel.getNatinality()
        } else {
            CustomSnackbar.make(
                requireActivity().window.decorView.rootView as ViewGroup,
                "No Internet Connection..."
            ).show()
        }
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(year, monthOfYear, dayOfMonth)
                val selectedDate = calendar.time
                if (isStartDate) {
                    if (endDate == null) {
//                        startDate = selectedDate
                        binding.startDateEditText.setText(getCurrentDate())
                        binding.endDateEditText.setText(calculateEndDate(binding.startDateEditText.text.toString()))
                    } else if (endDate != null && selectedDate.before(endDate)) {
                        // Start and end dates are valid
                        // Perform any necessary operations with the selected start and end dates
                        startDate = selectedDate
                        binding.startDateEditText.setText(formatDate(selectedDate))
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Start date must be before end date.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    if (startDate == null) {
                        endDate = selectedDate
                        binding.endDateEditText.setText(formatDate(selectedDate))
                    } else if (startDate != null && selectedDate.after(startDate)) {
                        // Start and end dates are valid
                        // Perform any necessary operations with the selected start and end dates
                        endDate = selectedDate
                        binding.endDateEditText.setText(formatDate(selectedDate))
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "End date must be after start date.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                /*  if (startDate != null && endDate != null && endDate!!.after(startDate)) {
                     // Start and end dates are valid
                     // Perform any necessary operations with the selected start and end dates

                 }
                 else {
                     // Invalid date range
                     // Display an error message or handle the invalid range accordingly
                     Toast.makeText(
                         requireContext(),
                         "Invalid date range.",
                         Toast.LENGTH_SHORT
                     ).show()
                 }*/
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set the maximum date to today's date
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

//        datePickerDialog.show()
    }

    private fun formatDate(date: Date): String {
//      val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)

    }

    private fun listeners() {

        binding.startDateEditText.setOnClickListener {
            showDatePickerDialog(true)
        }

        binding.endDateEditText.setOnClickListener {
            showDatePickerDialog(false)
        }

        binding.ivSearchdata.setOnClickListener(View.OnClickListener {
            if (binding.edMobilenumber.text.toString().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter the phone number ",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.edMobilenumber.text.toString().length < count)
                Toast.makeText(
                    requireContext(),
                    "Please enter correct mobile number length.",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                if (country.equals("UAE")) {
                    if (validateEditTextNumber(binding.edMobilenumber.text.toString())) {
                        valid = true
                        viewModel.getCustomerDetails(
                            binding.edMobilenumber.text.toString(),
                            monthlyOrder
                        )

                    } else {

                        valid = false
                        Toast.makeText(
                            requireContext(),
                            "Number is not valid",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    viewModel.getCustomerDetails(
                        binding.edMobilenumber.text.toString(),
                        monthlyOrder
                    )

                }


                Log.d("9June: ", "IsMonthlyPass " + monthlyOrder.toString())
            }
        })
        binding.btnContinue.setOnClickListener(this)

        binding.llmale.setOnClickListener(View.OnClickListener {

            gender = "Male"
            binding.llmale.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_yellow_selector
            )
            binding.llfemale.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

        })

        binding.llfemale.setOnClickListener(View.OnClickListener {
            gender = "Female"

            binding.llfemale.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_yellow_selector
            )
            binding.llmale.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

        })

        binding.tvreset.setOnClickListener(View.OnClickListener {
            discountId = 0
            isComplementarySelected = false
            setgameDiscounts(gameLlist.get(0).gameDiscounts, -1)
            val editable1 = Editable.Factory.getInstance().newEditable("")
            binding.etadditionalcomments.text = editable1
            binding.llpercentage.visibility = View.GONE
            if (!bogoselected)
                binding.lldiscount.visibility = View.GONE
            val promo = Editable.Factory.getInstance().newEditable("")
            binding.edpercentage.text = promo
            complementarySelected = false
            Promocode = ""
            binding.edpercentage.setText("")

        })

        binding.llcash.setOnClickListener(View.OnClickListener {


            paymentMode = "Cash"
            prepaidMode = ""
            Log.d("17oct:", "paymentMode :" + "Cash")
            binding.spPrepaid.setSelection(0)

            binding.llcash.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_yellow_selector
            )
            binding.llcredit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

            binding.llsplit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

        })

        binding.llcredit.setOnClickListener(View.OnClickListener {


            paymentMode = "Card"
            prepaidMode = ""
            Log.d("17oct:", "paymentMode :" + "Card")
            binding.spPrepaid.setSelection(0)

            binding.llcredit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_yellow_selector
            )
            binding.llcash.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

            binding.llsplit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

        })

        binding.llsplit.setOnClickListener(View.OnClickListener {

            paymentMode = "Split"
            prepaidMode = ""
            Log.d("17oct:", "paymentMode :" + "Split")
            binding.spPrepaid.setSelection(0)

            binding.llsplit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_yellow_selector
            )
            binding.llcredit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

            binding.llcash.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.corner_curve_normal_selector
            )

        })

    }

    override fun onClick(view: View?) {
        when (view?.id) {

            binding.btnContinue.id -> {

                if (!confirmPrintButtonClicked) {

                    if (binding.edpercentage.text.toString().isEmpty())
                        Promocode = ""
                    else
                        Promocode = binding.edpercentage.text.toString()

                    if (!country.equals("UAE")) {
                        valid = true
                    }

                    Log.d("11July:", "Valid Value :" + valid.toString())

                    if (!binding.edPax.text.toString().isEmpty())
                        ticketCount = Integer.parseInt(binding.edPax.text.toString())

                    //validate and print ticket...
                    if (binding.autoCompleteTextView.text.toString().isEmpty())
                        Toast.makeText(requireContext(), "Please enter Name.", Toast.LENGTH_SHORT)
                            .show()
                    else if (binding.edMobilenumber.text.toString().isEmpty())
                        Toast.makeText(
                            requireContext(),
                            "Please enter Mobile Number.",
                            Toast.LENGTH_SHORT
                        ).show()
                    else if (binding.edMobilenumber.text.toString().length < count)
                        Toast.makeText(
                            requireContext(),
                            "Please enter correct mobile number length.",
                            Toast.LENGTH_SHORT
                        ).show()
                    else if (!valid) {

                        Toast.makeText(
                            requireContext(),
                            "Please enter valid Mobile Number.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (gender.isEmpty())
                        Toast.makeText(
                            requireContext(),
                            "Please select Gender.",
                            Toast.LENGTH_SHORT
                        ).show()
                    else if (paymentMode.isEmpty())
                        Toast.makeText(
                            requireContext(),
                            "Please enter Payment Mode.",
                            Toast.LENGTH_SHORT
                        ).show()
                    else if (paymentMode.contentEquals("Prepaid") && prepaidMode.isEmpty())
                        Toast.makeText(
                            requireContext(),
                            "Please enter Payment Mode.",
                            Toast.LENGTH_SHORT
                        ).show()
                    else if (countryId == 0)
                        Toast.makeText(
                            requireContext(),
                            "Please select Nationality.",
                            Toast.LENGTH_SHORT
                        ).show()
                    else if (gameAccessoryBoth) {
                        if (selectedGame.isEmpty())
                            Toast.makeText(
                                requireContext(),
                                "Please select Game.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        else if (typeOfGameSelected.isEmpty())
                            Toast.makeText(
                                requireContext(),
                                "Please select Type Of Game too.",
                                Toast.LENGTH_SHORT
                            ).show()
                        else if (passSelected.isEmpty())
                            Toast.makeText(
                                requireContext(),
                                "Please select Pass too.",
                                Toast.LENGTH_SHORT
                            ).show()
                        else if (timeId == 0)
                            Toast.makeText(
                                requireContext(),
                                "Please select Time too.",
                                Toast.LENGTH_SHORT
                            ).show()
                        else if (ticketCount == 0)
                            Toast.makeText(
                                requireContext(),
                                "Please select kid too.",
                                Toast.LENGTH_SHORT
                            ).show()
                        else if (complementarySelected && Promocode.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Please enter promo code",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if ((discountId > 0 || complementarySelected || bogoselected)
                            && binding.etadditionalcomments.text.toString()
                                .isEmpty()
                        ) {
                            Toast.makeText(
                                requireContext(),
                                "Please enter discount comments",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if ((bogoselected) && binding.etadditionalcomments.text.toString()
                                .isEmpty()

                        ) {
                            Toast.makeText(
                                requireContext(),
                                "Please enter discount comments",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (childComp && binding.etchildcomp.text.toString().isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Please enter child complementary comment",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            if (accessorySelected(onlyAccessoryList)) {

                                val accessoryJsonArray = JSONArray()

                                //prep data for accessory data...
                                for (accessories in onlyAccessoryList) {

                                    for (accessories1 in accessories.listGameAccessory) {
                                        val personJsonObject = JSONObject()
                                        personJsonObject.put("MapId", accessories1.mapId)
                                        personJsonObject.put("Id", accessories.id)
                                        personJsonObject.put(
                                            "Quantity",
                                            accessories1.selectedQuantity
                                        )
                                        accessoryJsonArray.put(personJsonObject)

                                    }

                                }
                                // Convert the JsonObject to a JSON string and print it out
                                accessoryJsonData = accessoryJsonArray.toString()
                                println(accessoryJsonData)
                                Log.d("25May:AccessoryData", accessoryJsonData)

                            } else
                                accessoryJsonData = ""

                            confirmPrintButtonClicked = true

                            hitPreviewApi()
                        }
                    } else {


                        if (onlyAccessorySelected(onlyAccessoryList)) {

                            val accessoryJsonArray = JSONArray()

                            //prep data for accessory data...
                            for (accessories in onlyAccessoryList) {

                                for (accessories1 in accessories.listGameAccessory) {
                                    val personJsonObject = JSONObject()
                                    personJsonObject.put("MapId", accessories1.mapId)
                                    personJsonObject.put("Id", accessories.id)
                                    personJsonObject.put("Quantity", accessories1.selectedQuantity)
                                    accessoryJsonArray.put(personJsonObject)

                                }

                            }
                            // Convert the JsonObject to a JSON string and print it out
                            accessoryJsonData = accessoryJsonArray.toString()
                            println(accessoryJsonData)
                            Log.d("23May:AccessoryDataOnly", accessoryJsonData)
                            hitPreviewApi()
                            confirmPrintButtonClicked = true

                        } else {

                            Toast.makeText(
                                requireContext(),
                                "Please select Accessory item.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }

                }
            }
        }

    }

    private fun validateAddMoreContent(): Boolean {

        var isValid: Boolean = false

        // Assuming you're using a LinearLayoutManager
        val layoutManager = binding.rvselectedItems.layoutManager as LinearLayoutManager

// Get the first and last visible positions
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

// Calculate the number of visible items
        val visibleItemCount = lastVisiblePosition - firstVisiblePosition + 1

// Now you can compare the visible item count with the size of passTimeArrayList
        val passTimeArrayListSize = passTimeArrayList.size

        Log.d("22Aug:", "visibleItemCount :" + visibleItemCount)

        if (visibleItemCount == passTimeArrayListSize) {
            // The number of visible items in the RecyclerView matches the size of passTimeArrayList
            // Do something here
        } else {
            // The number of visible items does not match the size of passTimeArrayList
            // Do something else if needed
        }


        return isValid
    }

    private fun onlyAccessorySelected(accessoryList1: ArrayList<GameAccessoriesItem>): Boolean {

        var accSelected = false

        for (accessories in accessoryList1) {

            for (accessories1 in accessories.listGameAccessory) {

                if (accessories1.selectedQuantity > 0) {
                    accSelected = true
                    break
                }

            }

        }
        return accSelected
    }

    private fun accessorySelected(accessoryList1: ArrayList<GameAccessoriesItem>): Boolean {

        var accSelected = false

        for (accessories in accessoryList1) {

            for (accessories1 in accessories.listGameAccessory) {
                if (accessories1.selectedQuantity > 0) {
                    accSelected = true
                    break
                }

            }

        }
        return accSelected
    }

    private fun hitPrintApi(cashAmount: Double) {

        if (ismonthlyPass) {
            //print and reload fragment...
            mBookingRequest = BookRequestData(
                gameId,
                binding.autoCompleteTextView.text.toString(),
                selected_code.substringBefore("("),
                binding.edMobilenumber.text.toString(),
                gender,
                countryId,
                paymentMode,
                shared.getString("userID", "")!!.toInt(),
                timeId,
                binding.edPax.text.toString().toInt(),
                discountId,
                accessoryJsonData,
                binding.etadditionalcomments.text.toString(),
                binding.etchildcomp.text.toString(),
                discountImage,
                compImage,
                Promocode,
                promocodeId,
                orderId,
                ismonthlyPass,
                binding.etnotes.text.toString(),
                cashAmount,
                childComp,
                binding.edAdult.text.toString().toInt(),
                prepaidMode
            )

        } else {
            //print and reload fragment...
            mBookingRequest = BookRequestData(
                gameId,
                binding.autoCompleteTextView.text.toString(),
                selected_code.substringBefore("("),
                binding.edMobilenumber.text.toString(),
                gender,
                countryId,
                paymentMode,
                shared.getString("userID", "")!!.toInt(),
                timeId,
                binding.edPax.text.toString().toInt(),
                discountId,
                accessoryJsonData,
                binding.etadditionalcomments.text.toString(),
                binding.etchildcomp.text.toString(),
                discountImage,
                compImage,
                Promocode,
                promocodeId,
                orderId,
                ismonthlyPass,
                binding.etnotes.text.toString(),
                cashAmount,
                childComp,
                binding.edAdult.text.toString().toInt(),
                prepaidMode
            )

        }

        val gson = Gson()
        val json = gson.toJson(mBookingRequest)
        viewModel.bookAndPrint(mBookingRequest)

        Log.d("23May:", "mBookingRequest :" + mBookingRequest.toString())

    }

    private fun hitPreviewApi() {
        Log.d("23May:", "hitPrintApi :" + timeId)
        mBookingRequest = BookRequestData(
            gameId,
            binding.autoCompleteTextView.text.toString(),
            selected_code.substringBefore("("),
            binding.edMobilenumber.text.toString(),
            gender,
            countryId,
            paymentMode,
            shared.getString("userID", "")!!.toInt(),
            timeId,
            binding.edPax.text.toString().toInt(),
            discountId,
            accessoryJsonData,
            binding.etadditionalcomments.text.toString(),
            binding.etchildcomp.text.toString(),
            "",
            "",
            Promocode,
            0,
            orderId,
            ismonthlyPass,
            binding.etnotes.text.toString(),
            0.0,
            childComp,
            binding.edAdult.text.toString().toInt(),
            prepaidMode
        )

        val gson = Gson()
        val json = gson.toJson(mBookingRequest)
        viewModel.preview(mBookingRequest)

//      Log.d("23May:", "mBookingRequest :" + mBookingRequest.toString())

    }

    fun saveBitmapToFile(bitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val destination =
//            File(Environment.getExternalStorageDirectory(), "360play" + System.currentTimeMillis())
            File(Environment.getExternalStorageDirectory(), "360play")
        val fo: FileOutputStream
        try {
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val path = destination.absolutePath
        uploadProfileImage(path)
    }

    fun saveBitmapToFileChild(bitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val destination =
            File(Environment.getExternalStorageDirectory(), "360playChild")
        val fo: FileOutputStream
        try {
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val path = destination.absolutePath
        uploadProfileImageChild(path)
    }

    fun uploadProfileImage(filePath: String?) {
        val file = File(filePath)
        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        body = createFormData("file", file.name, reqFile)
        val name = "file".toRequestBody("application/text; charset=utf-8".toMediaTypeOrNull())
//      viewModel.uploadAttachment(body)
    }

    fun uploadProfileImageChild(filePath: String?) {
        val file = File(filePath)
        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        bodyChild = createFormData("file", file.name, reqFile)
        val name = "file".toRequestBody("application/text; charset=utf-8".toMediaTypeOrNull())
//      viewModel.uploadAttachment(body)
    }


    private fun printContent(slipType: String, data: Data, recType: String) {

        if (paymentMode.equals("Split")) {
            //height ...

            // Set the desired height in sp
            val desiredHeightSp = 30

            val desiredHeightPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                desiredHeightSp.toFloat(),
                resources.displayMetrics
            ).toInt()

            val layoutParams = binding.tvPayment.layoutParams
            layoutParams.height = desiredHeightPx
            binding.tvPayment.layoutParams = layoutParams

        } else {

            // Set the desired height in sp
            val desiredHeightSp = 15

            val desiredHeightPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                desiredHeightSp.toFloat(),
                resources.displayMetrics
            ).toInt()

            val layoutParams = binding.tvPayment.layoutParams
            layoutParams.height = desiredHeightPx
            binding.tvPayment.layoutParams = layoutParams
        }

        Log.d("Issueeee:", slipType)
        binding.tvcopytype.text = slipType

        if (childComp) {
            binding.llchildcomp.visibility = View.VISIBLE
        } else {

            binding.llchildcomp.visibility = View.GONE

        }

        if (data.isMonthlyPass) {
            binding.lldaysleft.visibility = View.VISIBLE
            binding.llenddate.visibility = View.VISIBLE
            binding.tvdaysleft.text = data.gamesLeft.toString() + " Day(s)"
            binding.tvenddate.text = data.passEndDate.toString()
        } else {
            binding.lldaysleft.visibility = View.GONE
            binding.llenddate.visibility = View.GONE
        }

        if (data.personEntryCount > 0) {
            binding.tvadultcount.text = data.personEntryCount.toString()
            binding.lladultcount.visibility = View.VISIBLE
            binding.tventryFee.text = currency + " " + data.entryFee.toString()
            binding.llentryfee.visibility = View.VISIBLE

        } else {
            binding.lladultcount.visibility = View.GONE
            binding.llentryfee.visibility = View.GONE

        }

        /*  if (data.entryFee > 0) {
              binding.tventryFee.text = currency + " " + data.entryFee.toString()
              binding.llentryfee.visibility = View.VISIBLE

          } else
              binding.llentryfee.visibility = View.GONE
  */

        binding.tvpowered.text = context?.resources?.getString(R.string.poweredbyqtickets_ar)

        var bookingID = data.id
//      var totalP = 0.0
        val bitmap1: Bitmap = CreateImageBarcode(bookingID.toString(), "Barcode")!!
        binding.ivBarcode.setImageBitmap(bitmap1)
        val params: LinearLayout.LayoutParams =
            binding.ivBarcode.layoutParams as LinearLayout.LayoutParams
        params.gravity = Gravity.CENTER
        binding.ivBarcode.layoutParams = params
        binding.tvBookingid.text = bookingID.toString()

        binding.tvGamenames.text = data.passTime//P
//      binding.tvbookingtime.text = data.time + "(play time begins at entry)"
        binding.tvbookingtime.text = data.time
        binding.tvtime.text = data.time
        binding.tvnumberofpersons.text = data.persons.toString()


        val original_format = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

        val targetFormat: DateFormat = SimpleDateFormat("hh:mm a")
        val actual_date_format = original_format.parse(data.time)
        val real_date = targetFormat.format(actual_date_format)

        val parsedDate = targetFormat.parse(real_date)
        val calendar = Calendar.getInstance()
        calendar.time = parsedDate

        calendar.add(Calendar.MINUTE, data.duration)
        val formattedEndDate = targetFormat.format(calendar.time)
        binding.tvendtime.text = formattedEndDate.toString()

        binding.tvTodaysdate.text = getCurrentDate()
        binding.tvMobilenumber.text = data.mobile
        binding.tvcustomername.text = data.name
        binding.tvNooftickets.text = data.quantity.toString()
        binding.tvPayment.text = data.payType
        binding.tvPrice.text = currency + " " + data.price.toString()
        binding.tvTotal.text = currency + " " + data.totalPrice.toString()
        binding.tvVat.text = currency + " " + data.totalVATAmount.toString()
        if (data.vatPercentage > 0)
            binding.tvvattitle.text =
                "VAT(ضريبة القيمة المضافة " + "%)" + " " + data.vatPercentage.toString()
        else
            binding.tvvattitle.text = resources.getString(R.string.vat)

        if (data.vatNo.equals("")) {
            binding.llvatpricePreview.visibility = View.GONE
        } else {
            binding.llvatpricePreview.visibility = View.VISIBLE
            binding.tvVatpricetittle.text = "VAT No. "
            binding.tvVatvalueprice.text = data.vatNo
        }

        if (data.crNo.isNullOrEmpty())
            binding.llcrnumber.visibility = View.GONE
        else {
            binding.tvCrnumber.text = "CR No."
            binding.tvCrnumbervalue.text = data.crNo
            binding.llcrnumber.visibility = View.VISIBLE

        }

        if (!data.address.equals("")) {
            binding.lladddressPreview.visibility = View.VISIBLE
//              binding.tvAddresstittle.text = "Address(عنوان) :"
            binding.tvAddressvalue.text = data.address

        } else {
            binding.lladddressPreview.visibility = View.GONE
        }


//        if(data.address.equals("")){
//            binding.lladddressPreview.visibility=View.GONE
//        }else{
//            binding.lladddressPreview.visibility=View.GONE
//        }

        if (data.bogoApplied) {
            //show offer view....

            binding.lloffer.visibility = View.VISIBLE
            binding.tvOffer.text = "BOGO"
            binding.lldiscount.visibility = View.VISIBLE

        } else {
            binding.lloffer.visibility = View.GONE

        }

        if (data.discountAmount > 0) {
            binding.llDiscounttype.visibility = View.VISIBLE
            binding.tvDiscountvalue.text = currency + " " + data.discountAmount.toString()
            binding.tvdiscountdetail.text = data.discountDetails

        } else
            binding.llDiscounttype.visibility = View.GONE

        if (!gameAccessoryBoth) {
            //means only accessories...
            binding.llprice.visibility = View.GONE
            binding.llticket.visibility = View.GONE
            binding.llnumberofpersons.visibility = View.GONE
            binding.lltotal.visibility = View.GONE
            binding.llvatPreview.visibility = View.GONE
            binding.llonlytime.visibility = View.VISIBLE
            binding.llstarttime.visibility = View.GONE
            binding.llendtime.visibility = View.GONE
        }
        binding.llaccessoryreceipt.visibility = View.GONE

        if (data.accessories != null && data.accessories.size > 0) {
            binding.llaccessoryreceipt.visibility = View.VISIBLE
            setRvPrintAccessory(data.accessories)
            //          totalP = data.accessories.get(0).totalPrice
        } else
            binding.llaccessoryreceipt.visibility = View.GONE

        binding.tvFinalamount.text = currency + " " + data.totalAmount.toString()

        SunmiPrintHelper.getInstance().setAlign(1)

        val originalBitmap = loadBitmapFromView(binding.llprint)
        val printableWidth = binding.llprint.width// Get the printable width of the paper
        val printableHeight = binding.llprint.height// Get the printable height of the paper

        // Calculate the scale factor based on the printable area
        val scaleFactor = minOf(
            printableWidth / originalBitmap?.width!!.toFloat(),
            printableHeight / originalBitmap.height.toFloat()
        )

        // Scale down the bitmap
        val scaledBitmap = originalBitmap.let {
            Bitmap.createScaledBitmap(
                it,
                (originalBitmap.width * scaleFactor).toInt(),
                (originalBitmap.height * scaleFactor).toInt(),
                true
            )
        }

        // Print the scaled bitmap
        SunmiPrintHelper.getInstance().printBitmap(scaledBitmap, myorientation)

        SunmiPrintHelper.getInstance().feedPaper()

        if (recType.contentEquals("cust")) {
            disableButtons()

            Log.d("16oct", "cust ")

        }


    }

    private fun disableButtons() {

        alreadyClicked = false

        //show dialog with printmerchantcopy and cancel...
        showDialog(requireContext())

    }

    fun showError(mContext: Context, message: String) {

        val alertDialog = AlertDialog.Builder(mContext)
        alertDialog.setMessage(message)
        alertDialog.setCancelable(false)
        alertDialog.setNegativeButton("Ok", object : DialogInterface.OnClickListener {

            override fun onClick(p0: DialogInterface?, p1: Int) {
                alertDialog.create().dismiss()

            }
        })

        alertDialog.show()
    }

    fun showDialog(mContext: Context) {
        Log.d("16oct", "showDialog alreadyClicked :" + alreadyClicked)

        val alertDialog = AlertDialog.Builder(mContext)
        alertDialog.setMessage("Print Merchant Copy?")
        alertDialog.setCancelable(false)
        alertDialog.setNegativeButton("NO", object : DialogInterface.OnClickListener {

            override fun onClick(p0: DialogInterface?, p1: Int) {
                alertDialog.create().dismiss()
                refreshFragment()
            }
        })

        alertDialog.setPositiveButton("YES",
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                    alertDialog.create().dismiss()

                    if (!alreadyClicked) {

                        alreadyClicked = true
                        Log.d("16oct", "DialogInterface alreadyClicked :" + alreadyClicked)

                        printContent(resources.getString(R.string.merchant_ar), printData, "merch")
                    }

                    refreshFragment()
                }
            })
        alertDialog.show()
    }

    fun loadBitmapFromView(view: View): Bitmap? {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache(true)
        view.isDrawingCacheEnabled = true
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache(true)
        val b: Bitmap = Bitmap.createBitmap(view.drawingCache)

        return b
    }

    fun returnFinalAmt(a: Double, b: Double, c: Double): Double {
        return a + b + c
    }

    private fun refreshFragment() {

        lifecycleScope.launch {
            delay(1000)
            val fm: FragmentManager = requireActivity().supportFragmentManager
            if (fm.backStackEntryCount > 0) {
                for (i in 0 until fm.backStackEntryCount) {
                    fm.popBackStack()
                }
            }
            requireActivity().finish()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }
    }


    fun getEndDateAfterOneMonth(startDate: String, dateFormat: String): String {
        val dateFormatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = dateFormatter.parse(startDate) ?: Date()
        calendar.add(Calendar.MONTH, 1)
        return dateFormatter.format(calendar.time)
    }

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
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

    override fun onGameSelected(position: Int) {
        Log.d("Position:", "onGameSelected :" + position)
        gameClicked = position
        selectedGame = gameLlist.get(position).game

        setRvTypeOfgame(gameLlist.get(position).gameTypes, 0)

        discountList = gameLlist.get(position).gameDiscounts as ArrayList<GameDiscounts>
        setgameDiscounts(gameLlist.get(position).gameDiscounts, -1)

        binding.llsplitamt.visibility = View.GONE
        setRvGameAccessories(onlyAccessoryList)
        // accessory list has become independent...

        /* if (onlyAccessoryList.get(gameClicked).gameAccessories.size > 0) {
             accessoryList = gameLlist.get(gameClicked).gameAccessories as ArrayList<GameAccessoriesItem>
             binding.tvgameaccessories.visibility = View.VISIBLE

             binding.tvgameaccessories.text = gameLlist.get(gameClicked).gameAccessories.get(0).accessory
             setRvGameAccessories(accessoryList)
         } else {
             binding.tvgameaccessories.visibility = View.GONE
             binding.titleaccessory.visibility = View.GONE
             binding.rvgameaccessories.visibility = View.GONE

         }*/

        binding.tvtimes.visibility = View.GONE
        binding.rvtimes.visibility = View.GONE
        /*  binding.tvpassTitle.visibility = View.GONE
          binding.rvpasses.visibility = View.GONE*/
        binding.tvbaseprice.visibility = View.GONE
        binding.tvpersons.visibility = View.GONE

    }

    override fun onTypeOfGameClicked(position: Int, passId: Int) {
        timeId = 0
        typeOfGameClicked = position

        typeOfGameSelected = gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).type
        binding.llsplitamt.visibility = View.GONE

        Log.d("6June:", "typeOfGameSelected :" + typeOfGameSelected)
        binding.tvbogo.visibility = View.GONE

        setRecycelrviewforPass(
            gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses,
            -1
        )

        /*   if (isMultiSelectedgame)
               setRecycelrviewforMultiSelPass(
                   gameLlist.get(gameClicked).gameTypes.get(
                       typeOfGameClicked
                   ).gameTypePasses, -1
               )
           else
               setRecycelrviewforPass(
                   gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses,
                   -1
               )
   */
        binding.tvtimes.visibility = View.GONE
        binding.rvtimes.visibility = View.GONE
        binding.tvbaseprice.visibility = View.GONE
        binding.tvpersons.visibility = View.GONE

        passSelected = ""

    }

    private fun resetAddMoreFun() {

        passTimeArrayList.clear()
        binding.rvselectedItems.visibility = View.GONE
        setRvSelectedPassAndTime(passTimeArrayList)

    }

    fun onTypeOfGameClickedOld(position: Int, passId: Int, timeId: Int) {
        this.timeId = 0
        passSelected = ""
        Log.d("Position:", "onTypeOfGameClicked :" + position)
        typeOfGameClicked = position
        binding.llsplitamt.visibility = View.GONE

        typeOfGameSelected = gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).type

        gameLlist.get(gameClicked).gameTypes.get(position).gameTypePasses.forEachIndexed { index, person ->
            if (passId == person.id) {
                //also show selected pass too...
                onPassClickedOld(index, timeId)//to show selected passes list

                setRecycelrviewforPass(
                    gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses,
                    index
                )

            }
        }

        binding.tvtimes.visibility = View.VISIBLE
        binding.rvtimes.visibility = View.VISIBLE
        binding.tvbaseprice.visibility = View.GONE
        binding.tvpersons.visibility = View.VISIBLE


    }

    override fun onPassClicked(position: Int, selectedPositions: MutableList<Int>) {
        Log.d("21aug:", "selectedPositions :" + selectedPositions.toString())

        timeId = 0
        passClicked = position
        selectedPasses.add(position)

        setRecycelrviewforTimes(
            gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).gamePassTimes, -1
        )

        passSelected =
            gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).pass

        binding.btnContinue.visibility = View.VISIBLE
        binding.tvbaseprice.visibility = View.GONE
        binding.tvpersons.visibility = View.GONE
        binding.lloffer.visibility = View.GONE
        binding.tvbogo.visibility = View.GONE
        bogoselected = false
        binding.llsplitamt.visibility = View.GONE

        if (discountId == 0) {
            val editable1 = Editable.Factory.getInstance().newEditable("")
            binding.etadditionalcomments.text = editable1
//          binding.lldiscount.visibility = View.GONE

        }

    }

    fun onPassClickedOld(position: Int, timeId: Int) {
        Log.d("Position:", "onPassClicked :" + position)
        this.timeId = 0
        passClicked = position
        passSelected =
            gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).pass

        binding.btnContinue.visibility = View.VISIBLE
        binding.llsplitamt.visibility = View.GONE
        binding.tvbaseprice.visibility = View.GONE
        binding.tvpersons.visibility = View.GONE
        binding.tvbogo.visibility = View.GONE
        binding.lldiscount.visibility = View.GONE
        bogoselected = false

        Log.d("26June:", "onPassClickedOld :" + "GONEEE")

        if (discountId == 0) {
//          binding.lldiscount.visibility = View.GONE

            val editable1 = Editable.Factory.getInstance().newEditable("")
            binding.etadditionalcomments.text = editable1

        }
        resetMonthlyPass()

        gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
            passClicked
        ).gamePassTimes.forEachIndexed { index, person ->
            if (timeId == person.id) {
                //also show selected pass too...
                onTimesClickedOld(index, timeId)//to show selected passes list

                setRecycelrviewforTimes(
                    gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                        passClicked
                    ).gamePassTimes, index
                )
            }
        }

    }

    override fun onTimesClicked(position: Int, persons: Int) {
        timeSelected = position
        numberOfPersons = persons

        timeId = gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
            passClicked
        ).gamePassTimes.get(position).id

        selectedTimes.add(position)

        Log.d("22Aug:", "timeId :" + timeId)

        selectedTime =
            gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).gamePassTimes.get(position).timeDetails

        basePrice = gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
            passClicked
        ).gamePassTimes.get(position).price

        baseAdultPrice =
            gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).gamePassTimes.get(position).entryFee

        totalAmount = basePrice

        binding.tvbaseprice.visibility = View.GONE
        binding.tvpersons.visibility = View.VISIBLE
        if (gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).gamePassTimes.get(position).bogoAvailable
        ) {
            binding.tvbaseprice.text = "Price : " + currency + " " + basePrice
            binding.tvbogo.visibility = View.VISIBLE
            binding.lldiscount.visibility = View.VISIBLE
            bogoselected = true
        } else {
            binding.tvbaseprice.text = "Price : " + currency + " " + basePrice
            binding.tvbogo.visibility = View.GONE
            bogoselected = false

            // in case of complementary also don't hide discount... 1sep
            // reset the discount comment and image...
            if (discountId == 0 && !isComplementarySelected) {
                binding.lldiscount.visibility = View.GONE
                val editable1 = Editable.Factory.getInstance().newEditable("")
                binding.etadditionalcomments.text = editable1

            }

        }
        binding.tvpersons.text = "No. of Person: " + persons
        vatamount = gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
            passClicked
        ).gamePassTimes.get(position).vatAmount

        //In case monthly pass tru show start and end date....

        if (gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).gamePassTimes.get(position).isMonthlyPass
        ) {

            ismonthlyPass = true
            //show start end date...
            binding.llmonthlypass.visibility = View.GONE
            binding.startDateEditText.setText(getCurrentDate())
            binding.endDateEditText.setText(
                getEndDateAfterOneMonth(
                    binding.startDateEditText.text.toString(),
                    "yyyy-MM-dd"
                )
            )
            Log.d("12June:", ismonthlyPass.toString())
        } else {
            // hide start and end date...
            resetMonthlyPass()
            orderId = 0
            Log.d("12June:", ismonthlyPass.toString())

        }
    }

    private fun resetMonthlyPass() {

        ismonthlyPass = false
        startDate = null
        endDate = null
        binding.startDateEditText.setText("")
        binding.endDateEditText.setText("")
        binding.llmonthlypass.visibility = View.GONE
    }

    fun onTimesClickedOld(position: Int, persons: Int) {
        timeSelected = position
        timeId = gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
            passClicked
        ).gamePassTimes.get(position).id
        Log.d("26June:", "timeId :" + timeId)

        selectedTime =
            gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).gamePassTimes.get(position).timeDetails

        basePrice = gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
            passClicked
        ).gamePassTimes.get(position).price

        totalAmount = basePrice
//      splitAmount()

        binding.tvbaseprice.visibility = View.GONE
        binding.tvpersons.visibility = View.VISIBLE
        if (gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).gamePassTimes.get(position).bogoAvailable
        ) {

            Log.d("26June:", "bogoAvailable :" + "YESSSSS")

            binding.tvbaseprice.text = "Price : " + currency + " " + basePrice
            binding.tvbogo.visibility = View.VISIBLE
            binding.lldiscount.visibility = View.VISIBLE
            bogoselected = true

        } else {
            Log.d("26June:", "bogoAvailable :" + "NOOOOOO")

            binding.tvbaseprice.text = "Price : " + currency + " " + basePrice
            binding.tvbogo.visibility = View.GONE
            bogoselected = false
            // in case of complementary also don't hide discount... 1sep
            // reset the discount comment and image...
            if (discountId == 0 && !isComplementarySelected) {
                binding.lldiscount.visibility = View.GONE
                val editable1 = Editable.Factory.getInstance().newEditable("")
                binding.etadditionalcomments.text = editable1

            }
        }

        vatamount = gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
            passClicked
        ).gamePassTimes.get(position).vatAmount

        if (gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).gamePassTimes.get(position).isMonthlyPass
        ) {
            ismonthlyPass = true
            Log.d("12June:", ismonthlyPass.toString())

            //show start end date...
            binding.llmonthlypass.visibility = View.VISIBLE
            binding.startDateEditText.setText(getCurrentDate())
            binding.endDateEditText.setText(
                getEndDateAfterOneMonth(
                    binding.startDateEditText.text.toString(),
                    "dd-MM-yyyy"
                )
            )

        } else {
            // hide start and end date...
            resetMonthlyPass()
            orderId = 0
            Log.d("12June:", ismonthlyPass.toString())

        }
    }

    override fun onAccessorySelected(parentPos: Int, childPos: Int) {
        Log.d(
            "27July:",
            "accessorySelected :" + onlyAccessoryList.get(parentPos).listGameAccessory.get(childPos).selectedQuantity
        )
//      Log.d("27July:", "accessorySelected :" + onlyAccessoryList.get(position).id)
        selectedAccessory = onlyAccessoryList.get(parentPos).listGameAccessory.get(childPos).mapId

        Log.d(
            "27July:",
            "selectedAccessory :" + onlyAccessoryList.get(parentPos).listGameAccessory.get(childPos).mapId
        )

        updateAccessoryAmount()

    }

    private fun updateAccessoryAmount() {

        accessoryTotalAmount = 0.0

        for (item in onlyAccessoryList) {
            for (item1 in item.listGameAccessory) {
                val amount = item1.price * item1.selectedQuantity
                accessoryTotalAmount += amount
            }

        }

        updatecashCreditAmount()

        Log.d("27July:", "accessoryTotalAmount:" + accessoryTotalAmount)

    }

    override fun onlyAccessorySelected(parentPos: Int, childPos: Int) {

        selectedOnlyAccessory =
            onlyAccessoryList.get(parentPos).listGameAccessory.get(childPos).mapId

        accessoryTotalAmount = 0.0

        for (item in onlyAccessoryList) {
            for (item1 in item.listGameAccessory) {

                val amount = item1.price * item1.selectedQuantity
                accessoryTotalAmount += amount

            }

        }

        updatecashOnlyAccesoryAmount()
    }

    override fun onSelectedDiscountId(position: Int) {
        Log.d("1sep:", "onSelectedDiscountId :" + discountList.get(position).id)
        Log.d("1sep:", "Name :" + discountList.get(position).offerDetails)

        isComplementarySelected = discountList.get(position).offerDetails == "Complementary"

        discountId = discountList.get(position).id
        binding.lldiscount.visibility = View.VISIBLE
        //add check for complementary...enable Percentage edit text..
        if (discountList.get(position).percentage == 0) {
            binding.llpercentage.visibility = View.VISIBLE
            complementarySelected = true
        } else {
            binding.llpercentage.visibility = View.GONE
            complementarySelected = false
            Promocode = ""
            binding.edpercentage.setText("")
        }

    }

    fun onSelectedDiscountIdOld(position: Int) {
        Log.d("23May:", "onSelectedDiscountIdOld :" + discountList.get(position).id)
        discountId = discountList.get(position).id
        if (discountList.get(position).percentage == 0) {
            binding.llpercentage.visibility = View.VISIBLE
            complementarySelected = true

        } else {
            binding.llpercentage.visibility = View.GONE
            complementarySelected = false
            Promocode = ""
            binding.edpercentage.setText("")
        }

    }

    override fun removePass(position: Int) {

        passTimeArrayList.removeAt(position)
        passTimeAdapter.notifyDataSetChanged()
        Log.d("22Aug:", "removePass :" + passTimeArrayList.size)
        Log.d("22Aug:", "removePass :" + timeId)
        setRecycelrviewforTimes(
            gameLlist.get(gameClicked).gameTypes.get(typeOfGameClicked).gameTypePasses.get(
                passClicked
            ).gamePassTimes, -1
        )

    }

}