package com.a360play.a360nautica.view.fragment.card

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseFragment
import com.a360play.a360nautica.data.card.PayViaCardRequest
import com.a360play.a360nautica.data.entrypoint.UserExitRequest
import com.a360play.a360nautica.databinding.FragmentPayviacardscannerBinding
import com.a360play.a360nautica.view.activities.MainActivity
import com.a360play.a360nautica.view.activities.OpenBarcodeScanner
import com.a360play.a360nautica.viewmodels.EntryViewModel
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient
import com.app.starterkit.utils.MyViewModelFactory
import com.app.starterkit.utils.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PayCardWithScanningFragment : BaseFragment() {

    lateinit var binding: FragmentPayviacardscannerBinding
    lateinit var payCardRequest: PayViaCardRequest
    val apiService = RetrofitClient.apiService
    val repository = Repository(apiService)
    lateinit var shared: SharedPreferences
    var additonalAMount = 0
    var booking_id = ""
    lateinit var userExitRequest: UserExitRequest

    companion object {
        private const val CAMERA_PERMISSION_CODE = 400
    }


    private val exit_viewModel: EntryViewModel by viewModels() {
        MyViewModelFactory(repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPayviacardscannerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        listeners()
        bindObservers()
    }

    private fun listeners() {
        setFragmentResultListener("360CardPayment") { reqKey, bundle ->
            if (reqKey == "360CardPayment") {
                val result = bundle.getString("bookingID")
                binding.edBarcodenumbers.setText(result)
            }
        }

        binding.ivScannerforpayment.setOnClickListener(View.OnClickListener {
            checkPermission(
                Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE
            )
        })
    }

    fun checkPermission(permission: String, requestCode: Int) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {

            val fr = OpenBarcodeScanner()
            val bundle = Bundle()
            bundle.putString("eventtype", "payment")
            fr.setArguments(bundle)
            addFragment(fr, true, "OpenBarcodeScanner")
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val fr = OpenBarcodeScanner()
                val bundle = Bundle()
                bundle.putString("viewstatus", "payment")
                fr.setArguments(bundle)
                addFragment(fr, true, "OpenBarcodeScanner")
            } else {
                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun initview() {

        this.arguments?.let {
            if (it.getInt("amount", 0) > 0) {
                additonalAMount = it.getInt("amount", 0)
                booking_id = it.getString("bookingID", "")
            }
        }

        shared = requireActivity().getSharedPreferences(
            resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
        binding.btnPayviacard.setOnClickListener(View.OnClickListener {
            if (binding.edBarcodenumbers.text.toString().equals("")) {
                Toast.makeText(requireActivity(), "Please enter the barcode", Toast.LENGTH_SHORT)
                    .show()
            } else {
                hitApitoPayFromCard()
            }
        })
    }


    private fun hitApitoPayFromCard() {
        val admin_id = shared.getString("userID", "")!!
        //    payCardRequest=PayViaCardRequest("dsfsd",3,43)
        //     payCardRequest= PayViaCardRequest(binding.edBarcodenumbers.text.toString(),additonalAMount)
        val map = HashMap<String, String>()
        map["barcode"] = binding.edBarcodenumbers.text.toString()
        map["amount"] = additonalAMount.toString()
        exit_viewModel.payviaCard(map)
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
                            "" + it.data.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        backtoHome()
                    }
                    if (it.data!!.statusCode == 400) {
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


        exit_viewModel.payviacardResponse.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(requireActivity(), "Processing.....", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    if (it.data!!.status == 200) {
                        Toast.makeText(
                            requireActivity(),
                            "" + it.data.status_message,
                            Toast.LENGTH_SHORT
                        ).show()
                        hitApitoExisttheGameZone()
                    }
                    if (it.data!!.status == 400) {
                        Toast.makeText(
                            requireActivity(),
                            "" + it.data.status_message.toString(),
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


    private fun backtoHome() {
        lifecycleScope.launch {
            delay(2000)
            val fm: FragmentManager = requireActivity().supportFragmentManager
            if (fm.getBackStackEntryCount() > 0) {
                for (i in 0 until fm.getBackStackEntryCount()) {
                    fm.popBackStack()
                }
            }
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun hitApitoExisttheGameZone() {
      /*  userExitRequest = UserExitRequest(
            booking_id.toInt(),
            shared.getString("userID", "")!!.toInt(),
            additonalAMount.toDouble()
        )*/
//        exit_viewModel.exitUser(userExitRequest)
    }

}