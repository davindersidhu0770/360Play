package com.a360play.a360nautica.view.fragment.card

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseFragment
import com.a360play.a360nautica.data.booking.CustomSpinnerData
import com.a360play.a360nautica.databinding.FragmentAddcardbookingBinding
import com.a360play.a360nautica.databinding.FragmentRegistercardBinding
import com.a360play.a360nautica.view.activities.MainActivity
import com.a360play.a360nautica.viewmodels.CardViewModel
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient
import com.app.starterkit.utils.MyViewModelFactory
import com.app.starterkit.utils.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddBookingFragment : BaseFragment() {

    lateinit var binding: FragmentAddcardbookingBinding

    val apiService = RetrofitClient.apiService
    val repository = Repository(apiService)
    var balance = 0
    var barcode = ""
    var email = ""
    var currency = ""
    lateinit var shared: SharedPreferences
    private lateinit var adapter: ArrayAdapter<CustomSpinnerData>
    private var payment_type = ""

    private val viewModel: CardViewModel by viewModels() {
        MyViewModelFactory(repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddcardbookingBinding.inflate(inflater, container, false)
        currency = shared.getString("currency", "").toString()
        return binding.root
    }


    private fun bindObservers() {
        viewModel.rechargecardresponse.observe(requireActivity()) {
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
                        binding.tvBalance.setText(currency+" "+it.data.data.response.balance.toString())
                        binding.edEnteramount.text.clear()
                    }

                    if (it.data!!.status == 400) {
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
        binding.spCardpaymenttypeforrecharge.adapter = adapter
        binding.spCardpaymenttypeforrecharge.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    val selectedObject =
                        binding.spCardpaymenttypeforrecharge.selectedItem as CustomSpinnerData
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        bindObservers()
        setSpinnerForPaymentType()
        this.arguments?.let {
            if (it.getString("username", "") != null) {
                binding.tvCustomername.text = it.getString("username", "")
                binding.tvPhonenumber.text = it.getString("phone", "")
                email = it.getString("email", "")
                barcode = it.getString("barcode", "")
                balance = it.getInt("balance", 0)
                binding.tvBalance.text =currency+" "+balance.toString()
                binding.edEnteramount.text.clear()

            }
        }
    }

    private fun initview() {
        shared = requireActivity().getSharedPreferences(
            resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
        binding.btnBooking.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.putExtra("fromscreen", "recharge_card")
            intent.putExtra("username", binding.tvCustomername.text.toString())
            intent.putExtra("phone", binding.tvPhonenumber.text)
            intent.putExtra("barcode", barcode)
            intent.putExtra("email", email)
            startActivity(intent)
            requireActivity().finish()
        })



        binding.btnRechargecard.setOnClickListener(View.OnClickListener {
            if (binding.edEnteramount.text.toString().equals("")) {
                Toast.makeText(
                    requireContext(),
                    "Please enter the amount first",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (payment_type.equals("Select Payment Type...")) {
                Toast.makeText(
                    requireContext(),
                    "please select the payment type",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (payment_type.equals("")) {
                Toast.makeText(
                    requireContext(),
                    "please select the payment type",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val map = HashMap<String, String>()
                map["barcode"] = barcode
                map["amount"] = binding.edEnteramount.text.toString()
                map["admin_id"] = shared.getString("userID", "")!!
                map["payment_type"] = payment_type
                binding.edEnteramount.clearFocus()
                viewModel.rechargeCard(map)
            }
        })
    }

}