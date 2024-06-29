package com.a360play.a360nautica.view.fragment.entry

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResultListener
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseFragment
import com.a360play.a360nautica.databinding.FragmentEntrypointBinding
import com.a360play.a360nautica.extension.addFragmentWitExtension
import com.a360play.a360nautica.view.activities.MainActivity
import com.a360play.a360nautica.view.activities.OpenBarcodeScanner
import com.a360play.a360nautica.view.fragment.book.UserDetailsFragment
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient


class EntryPointFragment : BaseFragment(), MainActivity.DataReceivedListener {


    lateinit var binding: FragmentEntrypointBinding
    val apiService = RetrofitClient.apiService
    val repository = Repository(apiService)


    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntrypointBinding.inflate(inflater, container, false)
        (activity as MainActivity?)?.setDataReceivedListener(this)

        return binding.root
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
            bundle.putString("viewstatus", "entry")
            bundle.putString("eventtype", "entry")
            fr.setArguments(bundle)
            addFragment(fr, true, "OpenBarcodeScanner")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        setFragmentResultListener("360Bytes") { reqKey, bundle ->
            if (reqKey == "360Bytes") {
                val result = bundle.getString("bookingID")
                binding.edBookingid.setText(result)
                binding.edBookingid.setSelection(binding.edBookingid.length())//placing cursor at the end of the text

            }
        }
    }

    private fun listeners() {

        binding.ivBarcodescanner.setOnClickListener(View.OnClickListener {
            checkPermission(
                Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE
            )
        })

        binding.btnScan.setOnClickListener(View.OnClickListener {
            if (binding.edBookingid.text.toString().equals("")) {
                Toast.makeText(requireContext(), "Please enter the booking id ", Toast.LENGTH_SHORT)
                    .show()
            }
            else if ((binding.edBookingid.text.toString().toInt())==0) {
                Toast.makeText(requireContext(), "Booking id must be geater than 0", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                val fr = UserDetailsFragment()
                val bundle = Bundle()
                bundle.putString("eventtype", "entry")
                bundle.putString("viewstatus", "entry")
                bundle.putString("bookingID", binding.edBookingid.text.toString())
                fr.setArguments(bundle)
                addFragment(fr, true, "UserDetailsFragment")
            }
        })

        binding.btnViewstatus.setOnClickListener(View.OnClickListener {
            if (binding.edBookingid.text.toString().equals("")) {
                Toast.makeText(requireContext(), "Please enter the booking id ", Toast.LENGTH_SHORT)
                    .show()
            }
            else if ((binding.edBookingid.text.toString().toInt())==0) {
                Toast.makeText(requireContext(), "Booking id must be geater than 0", Toast.LENGTH_SHORT)
                    .show()
            }else {
                val fr = UserDetailsFragment()
                val bundle = Bundle()
                bundle.putString("viewstatus", "status")
                bundle.putString("eventtype", "entry")
                bundle.putString("bookingID", binding.edBookingid.text.toString())
                fr.setArguments(bundle)
                addFragment(fr, true, "UserDetailsFragment")
            }
        })

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
                bundle.putString("viewstatus", "entry")
                bundle.putString("eventtype", "entry")
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

        binding.edBookingid?.setText(barcode)

    }


}