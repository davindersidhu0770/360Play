package com.a360play.a360nautica.view.fragment.book

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor.open
import android.system.Os.open
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseFragment
import com.a360play.a360nautica.data.booking.BookRequestData
import com.a360play.a360nautica.data.booking.EnquiryRequestData
import com.a360play.a360nautica.databinding.LayoutEnquiryBinding
import com.a360play.a360nautica.utils.ColumnTilted
import com.a360play.a360nautica.utils.CustomSnackbar
import com.a360play.a360nautica.viewmodels.BookingViewModel
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient.apiService
import com.app.starterkit.utils.MyViewModelFactory
import com.app.starterkit.utils.Status
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream
import java.nio.channels.AsynchronousFileChannel.open
import java.nio.channels.DatagramChannel.open
import java.nio.channels.FileChannel.open
import java.nio.channels.Pipe.open
import java.nio.channels.SocketChannel.open
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

class EnquiryFragment : BaseFragment() {

    private var eventDate: String = ""

    /*
        private var selectedOption: String = ""
    */
    private var subOption: String = ""
    lateinit var binding: LayoutEnquiryBinding
    val repository = Repository(apiService)
    private lateinit var adapter: ArrayAdapter<String>
    var enquiryID = 0
    var enquiryType = ""
    var timeSlot = ""
    var nameArrayList: java.util.ArrayList<ColumnTilted> = java.util.ArrayList<ColumnTilted>()
    lateinit var mBookingRequest: EnquiryRequestData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutEnquiryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideStatusBar()
        setSpinnerofEnquiryType()
        setSpinnerofTimeSlot()
        listeners()
        bindObservers()

    }

    private fun bindObservers() {

        viewModel.enquiryResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireActivity(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {

                    if (it.data!!.status_message.equals("Success")) {
                        Toast.makeText(
                            requireActivity(),
                            "We have received your query. We will revert you soon",
                            Toast.LENGTH_LONG
                        ).show()

                        resetView()

                    } else {
                        Toast.makeText(
                            requireActivity(),
                            it.data.status_message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun resetView() {
        timeSlot = "Select Time slot..."

        enquiryType = "Select Enquiry Type..."
        eventDate = ""
        binding.spEnquirytype.setSelection(0)
        binding.spTimeslot.setSelection(0)
        binding.edUsername.setText("")
        binding.edMobilenumber.setText("")
        binding.etEventdate.setText("")
        binding.edKids.setText("")
        binding.edAdults.setText("")

    }

    private val viewModel: BookingViewModel by viewModels() {
        MyViewModelFactory(repository)
    }

    private fun sendQuiry() {
        if (checkForInternet(requireContext())) {
            //hit enquiry api...
            viewModel.sendEnquiry(mBookingRequest) //forbooking

        } else {
            CustomSnackbar.make(
                requireActivity().window.decorView.rootView as ViewGroup,
                "No Internet Connection..."
            ).show()
        }
    }

    private fun listeners() {

        binding.btnsubmit.setOnClickListener() {

            if (binding.minipack.isChecked)
                subOption = "Mini Pack"
            else if (binding.superpack.isChecked)
                subOption = "Super Pack"
            else if (binding.jumbopack.isChecked)
                subOption = "Jumbo Pack"
            else
                subOption = ""

            Log.d("Dataaa>>>", subOption)

            if (enquiryType.equals("Select Enquiry Type...")) {

                Toast.makeText(requireActivity(), "Please select Option first", Toast.LENGTH_SHORT)
                    .show()

            } else if (enquiryType.equals("Birthday Party") && subOption.isEmpty()) {


                Toast.makeText(
                    requireActivity(),
                    "Please select Party Type too",
                    Toast.LENGTH_SHORT
                ).show()


            } else if (binding.edUsername.text.toString().isNullOrEmpty()) {

                Toast.makeText(
                    requireActivity(),
                    "Please enter Customer Name first",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (binding.edMobilenumber.text.toString().isNullOrEmpty()) {

                Toast.makeText(
                    requireActivity(),
                    "Please enter Phone Number first",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (eventDate.isEmpty()) {

                Toast.makeText(
                    requireActivity(),
                    "Please enter Event Date first",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (binding.edKids.text.toString().isNullOrEmpty()) {

                Toast.makeText(
                    requireActivity(),
                    "Please enter Number of Kids first",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (binding.edAdults.text.toString().isNullOrEmpty()) {

                Toast.makeText(
                    requireActivity(),
                    "Please enter Number of Adults first",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (timeSlot.equals("Select Time slot...")) {
                Toast.makeText(
                    requireActivity(),
                    "Please select Time slot first",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                mBookingRequest = EnquiryRequestData(
                    enquiryType,
                    subOption,
                    binding.edUsername.text.toString(),
                    binding.edMobilenumber.text.toString(),
                    eventDate,
                    binding.edKids.text.toString().toInt(),
                    binding.edAdults.text.toString().toInt(),
                    timeSlot
                )

                sendQuiry()

            }


        }

        binding.etEventdate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Do something with the selected date
                    /*eventDate = "$dayOfMonth/${monthOfYear + 1}/$year"*/
                    eventDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                    binding.etEventdate.setText(eventDate)
                },
                year,
                month,
                day
            )

            datePicker.show()
        }
    }

    private fun setSpinnerofEnquiryType() {

        val customObjects = getCustomObjectsForEnqType()
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_game,
            R.id.text122,
            customObjects
        )
        binding.spEnquirytype.adapter = adapter
        binding.spEnquirytype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                enquiryType = binding.spEnquirytype.selectedItem.toString()
                Log.d("29March>>", enquiryType.toString())

                if (enquiryType.toString().equals("Birthday Party")) {

                    //enable radio group...
                    binding.radioGroup.visibility = View.VISIBLE
                    handleradioButtons()
                } else
                    binding.radioGroup.visibility = View.GONE

            }
        }


    }

    private fun handleradioButtons() {


    }

    private fun setSpinnerofTimeSlot() {

        val customObjects = getCustomObjectsForTimeSlot()
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_game,
            R.id.text122,
            customObjects
        )
        binding.spTimeslot.adapter = adapter
        binding.spTimeslot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val selectedObject = binding.spTimeslot.selectedItem
                Log.d("29March>>", selectedObject.toString())
                timeSlot = selectedObject.toString()
            }
        }

    }


    private fun getCustomObjectsForEnqType(): ArrayList<String> {
        val customObjects = ArrayList<String>()
        customObjects.clear()
        customObjects.add("Select Enquiry Type...")
        customObjects.add("Birthday Party")
        customObjects.add("School events")
        customObjects.add("Corporate events")
        enquiryID = 0
        enquiryType = "Select Enquiry Type..."

        return customObjects
    }

    private fun getCustomObjectsForTimeSlot(): ArrayList<String> {
        val customObjects = ArrayList<String>()
        customObjects.clear()
        customObjects.add("Select Time slot...")
        customObjects.add("10:00  am - 11:00 am")
        customObjects.add("11:00  am - 12:00 am")
        customObjects.add("12:00  am - 13:00 pm")
        customObjects.add("13:00  pm - 14:00 pm")
        customObjects.add("15:00  pm - 16:00 pm")
        customObjects.add("17:00  pm - 18:00 pm")
        customObjects.add("18:00  pm - 19:00 pm")
        customObjects.add("19:00  pm - 20:00 pm")
        customObjects.add("20:00  am - 21:00 am")

        timeSlot = "Select Time slot..."

        return customObjects
    }


}