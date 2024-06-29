package com.a360play.a360nautica.view.fragment.card

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseFragment
import com.a360play.a360nautica.data.booking.CustomSpinnerData
import com.a360play.a360nautica.data.booking.GamingListData
import com.a360play.a360nautica.data.booking.PaymentTypeData
import com.a360play.a360nautica.data.card.RegisterBarcodeRequest
import com.a360play.a360nautica.databinding.FragmentRegistercardBinding
import com.a360play.a360nautica.viewmodels.CardViewModel
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient
import com.app.starterkit.utils.MyViewModelFactory
import com.app.starterkit.utils.Status

class RegisterCardFragment : BaseFragment() {

    lateinit var binding: FragmentRegistercardBinding
    val apiService = RetrofitClient.apiService
    val repository = Repository(apiService)
    var barcode = ""
    lateinit var shared: SharedPreferences
    var mPaymentTypeList: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<CustomSpinnerData>

    private var payment_type = ""


    private val viewModel: CardViewModel by viewModels() {
        MyViewModelFactory(repository)
    }


    private fun bindObservers() {
        viewModel.registercardresponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireContext(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {

                    if (it.data!!.status == 200) {
                        Toast.makeText(
                            requireContext(),
                            "" + it.data.status_message,
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d("6feb", "Starttttttt")
                        val fr = AddBookingFragment()
                        Log.d("6feb", "AddBookingFragment")
                        Log.d("6feb", it.data.data.Response.toString())
                        val bundle = Bundle()
                        Log.d("6feb", "name:" + it.data.data.Response.name)
                        bundle.putString("username", it.data.data.Response.name)
                        Log.d("6feb", "email:" + it.data.data.Response.name)
                        bundle.putString("email", it.data.data.Response.email)
                        Log.d("6feb", "phone:" + it.data.data.Response.phone)

                        bundle.putString("phone", it.data.data.Response.phone)
                        Log.d("6feb", "barcode:" + it.data.data.Response.barcode)

                        bundle.putString("barcode", it.data.data.Response.barcode)
                        Log.d("6feb", "balance:" + it.data.data.Response.balance)

                        bundle.putInt("balance", it.data.data.Response.balance)
                        fr.setArguments(bundle)
                        addFragment(fr, true, "AddBookingFragment")


                    }
                    if (it.data!!.status == 404) {
                        Toast.makeText(
                            requireContext(),
                            "" + it.data.status_message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                Status.ERROR -> {

                }
            }
        }
    }

    private fun setSpinnerForPaymentType() {
        val customObjects = getCustomObjectsForPaymentGroup()
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_paymentype,
            R.id.text1223,
            customObjects
        )
        binding.spCardpaymenttype.adapter = adapter
        binding.spCardpaymenttype.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    val selectedObject = binding.spCardpaymenttype.selectedItem as CustomSpinnerData
                    payment_type = selectedObject.name
                }
            }


    }

    private fun getCustomObjectsForPaymentGroup(): ArrayList<CustomSpinnerData> {
        val customObjects = ArrayList<CustomSpinnerData>()
        customObjects.clear()
        customObjects.add(CustomSpinnerData(0, "Select Payment Type..."))
        customObjects.add(CustomSpinnerData(0, "Card"))
        customObjects.add(CustomSpinnerData(0, "Cash"))
        payment_type = "Select Payment Type..."
        return customObjects
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistercardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        bindObservers()
        setSpinnerForPaymentType()
    }

    private fun initview() {
        shared = requireActivity().getSharedPreferences(
            resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
        binding.btnRegistercard.setOnClickListener(View.OnClickListener {
            hitApitoRegisterCard()
        })
        this.arguments?.let {
            if (it.getString("barcode", "") != null) {
                barcode = it.getString("barcode", "")
            }
        }
    }

    private fun hitApitoRegisterCard() {
        if (binding.edCustomername.text.toString().equals("")) {
            Toast.makeText(requireContext(), "Please enter the name first", Toast.LENGTH_SHORT).show()
        }

//        else if(binding.edEmail.text.toString().equals("")){
//            Toast.makeText(requireContext(),"Please enter the email ", Toast.LENGTH_SHORT).show()
//        }D

        else if (!binding.edEmail.text.toString()
                .equals("") && !isValidEmail(binding.edEmail.text.toString())
        ) {
            Toast.makeText(
                requireContext(),
                "Please enter the valid email address",
                Toast.LENGTH_SHORT
            )
                .show()
        } else if (binding.edPhone.text.toString().equals("")) {
            Toast.makeText(requireContext(), "Please enter the phone", Toast.LENGTH_SHORT).show()
        } else if (binding.edAmount.text.toString().equals("")) {
            Toast.makeText(requireContext(), "Please enter the amount", Toast.LENGTH_SHORT).show()
        } else if (barcode.equals("")) {
            Toast.makeText(requireContext(), "Can'nt proceed with barcode", Toast.LENGTH_SHORT)
                .show()
        } else if (payment_type.equals("Select Payment Type...")) {
            Toast.makeText(requireContext(), "please select the payment type", Toast.LENGTH_SHORT)
                .show()
        } else if (payment_type.equals("")) {
            Toast.makeText(requireContext(), "please select the payment type", Toast.LENGTH_SHORT)
                .show()
        } else {
            var name = binding.edCustomername.text.toString()
            var email = binding.edEmail.text.toString()
            var phone = binding.edPhone.text.toString()
            var amount = binding.edAmount.text.toString()
            var adminid = shared.getString("userID", "")!!
            var register_request = RegisterBarcodeRequest(
                adminid.toInt(),
                barcode,
                name,
                email,
                phone,
                amount.toInt(),
                payment_type
            )
            viewModel.registerCard(register_request)
        }
    }


}