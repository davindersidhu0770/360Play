package com.a360play.a360nautica.view.activities

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.a360play.a360nautica.base.BaseFragment
import com.a360play.a360nautica.databinding.ActivityOpenbarcodeBinding
import com.a360play.a360nautica.view.fragment.book.UserDetailsFragment
import com.a360play.a360nautica.view.fragment.entry.EntryPointFragment
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class OpenBarcodeScanner : BaseFragment() {

    lateinit var binding:ActivityOpenbarcodeBinding
    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null
    private val REQUEST_CAMERA_PERMISSION = 201

    var intentData = ""
    var isFirstTime=false
    var event_type=""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityOpenbarcodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.arguments?.let {
            if(it.getString("eventtype","")!=null){
                event_type=it.getString("eventtype","")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        cameraSource!!.release()
    }

    override fun onResume() {
        super.onResume()
        initialiseDetectorsAndSources()
    }


    private fun initialiseDetectorsAndSources() {
        barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.CODE_128)
            .build()
        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()
        binding.surfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource!!.start(binding.surfaceView.getHolder())
                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource!!.stop()
            }
        })
        barcodeDetector!!.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
            }

            override fun receiveDetections(detections: Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                    binding.txtBarcodeValue.post(Runnable {
                        intentData = barcodes.valueAt(0).displayValue
                        if(!intentData.equals("") && !isFirstTime){
                            isFirstTime=true
                            navTOGetScannerID(intentData)
                        }

                    })
                }
            }
        })
    }


    fun navTOGetScannerID(bookingID:String) {

        if(event_type.equals("card")){
            val resultBundle = Bundle().apply { putString("bookingID", bookingID) }
            setFragmentResult("360Card", resultBundle)
            requireFragmentManager().popBackStack()
        }

        if(event_type.equals("payment")){
            val resultBundle = Bundle().apply { putString("bookingID", bookingID) }
            setFragmentResult("360CardPayment", resultBundle)
            requireFragmentManager().popBackStack()
        }

        else{
            val resultBundle = Bundle().apply { putString("bookingID", bookingID) }
            setFragmentResult("360Bytes", resultBundle)
            requireFragmentManager().popBackStack()
        }

        }

}