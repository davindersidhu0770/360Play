package com.a360play.a360nautica.view.fragment.card

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseFragment
import com.a360play.a360nautica.utils.CustomSnackbar
import com.a360play.a360nautica.view.activities.MainActivity
import com.a360play.a360nautica.view.activities.OpenBarcodeScanner
import com.a360play.a360nautica.view.activities.WriteNFCActivity
import com.a360play.a360nautica.viewmodels.CardViewModel
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient
import com.app.starterkit.utils.MyViewModelFactory
import com.app.starterkit.utils.Status


class DemoFragmentCardSecond : BaseFragment(), MainActivity.DataReceivedListener {

    var ivsettings: ImageView? = null
    var btnCard: Button? = null
    var tag_data: TextView? = null
    var ivScanner: ImageView? = null
    var edBarcodenumber: EditText? = null
    val apiService = RetrofitClient.apiService
    val repository = Repository(apiService)

    private val viewModel: CardViewModel by viewModels() {
        MyViewModelFactory(repository)
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_democard,
            container, false
        )
        tag_data = view.findViewById(R.id.tag_data)!!
        btnCard = view.findViewById(R.id.btn_card)!!
        ivScanner = view.findViewById(R.id.iv_scanner)!!
        ivsettings = view.findViewById(R.id.ivsettings)!!
        edBarcodenumber = view.findViewById(R.id.ed_barcodenumber)!!
        (activity as MainActivity?)?.setDataReceivedListener(this)
        listeners()
        setFragmentResultListener("360Card") { reqKey, bundle ->
            if (reqKey == "360Card") {
                val result = bundle.getString("bookingID")
                edBarcodenumber?.setText(result)
            }
        }
        return view
    }

    private fun listeners() {

        ivsettings?.setOnClickListener(View.OnClickListener {

            //navigate to new screen where user can write on NFC band...

            startActivity(Intent(requireContext(), WriteNFCActivity::class.java))

        })
        ivScanner?.setOnClickListener(View.OnClickListener {
            checkPermission(
                Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE
            )
        })

        btnCard?.setOnClickListener(View.OnClickListener {
            if (edBarcodenumber?.text.toString().equals("")) {
                Toast.makeText(
                    requireContext(),
                    "Please enter the barcode number ",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                bindObservers()
                hitApitoScanCardBarcode(edBarcodenumber?.text.toString())

            }
        })
    }


    private fun bindObservers() {
        viewModel.cardDetailsResponse.observe(requireActivity()) {
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
                        if (it.data.data.is_active) {
                            val fr = AddBookingFragment()
                            val bundle = Bundle()
                            bundle.putString("username", it.data.data.name)
                            bundle.putString("email", it.data.data.email)
                            bundle.putString("phone", it.data.data.phone)
                            bundle.putString("barcode", it.data.data.barcode)
                            bundle.putInt("balance", it.data.data.balance)
                            fr.setArguments(bundle)
                            addFragment(fr, true, "AddBookingFragment")
                        } else {
                            val fr = RegisterCardFragment()
                            val bundle = Bundle()
                            bundle.putString("barcode", it.data.data.barcode)
                            fr.setArguments(bundle)
                            addFragment(fr, true, "RegisterCardFragment")
                        }
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

    fun hitApitoScanCardBarcode(barcode: String) {
        if (checkForInternet(requireContext())) {
            viewModel.getCardDetailsInfo(barcode)
        } else {
            CustomSnackbar.make(
                requireActivity().window.decorView.rootView as ViewGroup,
                "No Internet Connection..."
            ).show()
        }
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
            bundle.putString("eventtype", "card")
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
                bundle.putString("viewstatus", "card")
                fr.setArguments(bundle)
                addFragment(fr, true, "OpenBarcodeScanner")
            } else {
                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onReceived(barcode: String) {
        Log.d("Barcode: Fragggg", barcode)

        edBarcodenumber?.setText(barcode)

    }


}