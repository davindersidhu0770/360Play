package com.a360play.a360nautica.view.fragment.exit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import com.a360play.a360nautica.base.BaseFragment
import com.a360play.a360nautica.databinding.FragmentExitpointBinding
import com.a360play.a360nautica.extension.addFragmentWitExtension
import com.a360play.a360nautica.view.activities.MainActivity
import com.a360play.a360nautica.view.activities.OpenBarcodeScanner
import com.a360play.a360nautica.view.fragment.book.UserDetailsFragment

class ExitGateFragment : BaseFragment(), MainActivity.DataReceivedListener {

    lateinit var binding: FragmentExitpointBinding

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExitpointBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
    }

    private fun initview() {
        (activity as MainActivity?)?.setDataReceivedListener(this)

        setFragmentResultListener("360Bytes") { reqKey, bundle ->
            if (reqKey == "360Bytes") {
                val result = bundle.getString("bookingID")
                binding.edBookingid.setText(result)
                binding.edBookingid.setSelection(binding.edBookingid.length())//placing cursor at the end of the text

            }
        }


        binding.btnViewstatus.setOnClickListener(View.OnClickListener {
            if (binding.edBookingid.text.toString().equals("")) {
                Toast.makeText(requireContext(), "please enter the booking id", Toast.LENGTH_SHORT)
                    .show()
            }
            else if ((binding.edBookingid.text.toString().toInt())==0) {
                Toast.makeText(requireContext(), "Booking id must be geater than 0", Toast.LENGTH_SHORT)
                    .show()
            }else {
                val fr = UserDetailsFragment()
                val bundle = Bundle()
                bundle.putString("viewstatus", "status")
                bundle.putString("eventtype", "exit")
                bundle.putString("bookingID", binding.edBookingid.text.toString())
                fr.setArguments(bundle)
                addFragment(fr, true, "UserDetailsFragment")
            }
        })


        binding.btnExist.setOnClickListener(View.OnClickListener {
            if (binding.edBookingid.text.toString().equals("")) {
                Toast.makeText(requireContext(), "please enter the booking id", Toast.LENGTH_SHORT)
                    .show()
            }
            else if ((binding.edBookingid.text.toString().toInt())==0) {
                Toast.makeText(requireContext(), "Booking id must be geater than 0", Toast.LENGTH_SHORT)
                    .show()
            }else {
                val fr = UserDetailsFragment()
                val bundle = Bundle()
                bundle.putString("viewstatus", "exit")
                bundle.putString("eventtype", "exit")
                bundle.putString("bookingID", binding.edBookingid.text.toString())
                fr.setArguments(bundle)
                addFragment(fr, true, "UserDetailsFragment")
            }
        })


        binding.ivExitBarcode.setOnClickListener(View.OnClickListener {
            checkPermission(
                Manifest.permission.CAMERA,
                ExitGateFragment.CAMERA_PERMISSION_CODE
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
            bundle.putString("eventtype", "exit")
            bundle.putString("viewstatus", "exit")
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
        if (requestCode == ExitGateFragment.CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val fr = OpenBarcodeScanner()
                val bundle = Bundle()
                bundle.putString("eventtype", "exit")
                bundle.putString("viewstatus", "exit")
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