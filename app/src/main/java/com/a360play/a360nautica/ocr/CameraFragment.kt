package com.a360play.a360nautica.ocr

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.a360play.a360nautica.R
import com.a360play.a360nautica.databinding.FragmentCameraBinding
import com.a360play.a360nautica.utils.Constants.Companion.RATIO_16_9_VALUE
import com.a360play.a360nautica.utils.Constants.Companion.RATIO_4_3_VALUE
import com.a360play.a360nautica.utils.TextAnalyser
import com.a360play.a360nautica.utils.snack
import com.a360play.a360nautica.view.activities.MainActivity
import com.google.android.datatransport.runtime.backends.BackendResponse.ok
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions
import com.snatik.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max

import kotlin.math.min


typealias CameraTextAnalyzerListener = (text: String) -> Unit
typealias languageChangeListener = (language : String ) -> Unit
class CameraFragment : Fragment(com.a360play.a360nautica.R.layout.fragment_camera) {

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraControl: CameraControl
    private lateinit var cameraInfo: CameraInfo
    private var currentLanguage = EntityExtractorOptions.ENGLISH
    private val executor by lazy {
        Executors.newSingleThreadExecutor()
    }
    private lateinit var progressDialog: ProgressDialog
    lateinit var binding: FragmentCameraBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCameraBinding.bind(view)

        progressDialog = ProgressDialog(requireContext())
        // Access the parent activity and cast it to the appropriate activity class
        val parentActivity = requireActivity() as MainActivity

        // Now you can call any public method of the parent activity
        parentActivity.hideBottomBar()

        binding.viewFinder.post {
            startCamera()
        }
        requestAllPermissions()

        binding.ivImageCapture.setOnClickListener {
            progressDialog.show()
            takePicture()
        }

    }

    private val multiPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if ( map.entries.size <3){
                Toast.makeText(context, "Please Accept all the permissions", Toast.LENGTH_SHORT).show()
            }
            else{
//                Toast.makeText(context, "SUCCESSSSSSSSSSS", Toast.LENGTH_SHORT).show()

            }

        }

    private fun requestAllPermissions(){
        multiPermissionCallback.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )
    }

    @Suppress("SameParameterValue")
    private fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension
        )

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
                                Log.d("4JUly:",result.toString())

                                // Set the result to be passed back to Fragment A
                                val bundle = Bundle().apply {
                                    putString("key_result", result)
                                }
                                setFragmentResult("request_key", bundle)

                                // Navigate back to Fragment A
                                requireActivity().supportFragmentManager.popBackStack()
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

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun startCamera() {


        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { binding.viewFinder.display.getRealMetrics(it) }

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)

        val rotation = binding.viewFinder.display.rotation


        // Bind the CameraProvider to the LifeCycleOwner
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({

            // CameraProvider
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)

                .build()

            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)

            // ImageCapture
            imageCapture = initializeImageCapture(screenAspectRatio, rotation)

            // ImageAnalysis


            cameraProvider.unbindAll()

            try {
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
                cameraControl = camera.cameraControl
                cameraInfo = camera.cameraInfo
                cameraControl.setLinearZoom(0.5f)


            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))


    }

    private fun getOutputDirectory(context: Context): File {

        val storage = Storage(context)
        val mediaDir = storage.internalCacheDirectory?.let {
            File(it, "Intelligible OCR").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }


    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }


    private fun initializeImageCapture(
        screenAspectRatio: Int,
        rotation: Int
    ): ImageCapture {
        return ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
    }

    override fun onResume() {
        super.onResume()
        binding.root.snack(getString(com.a360play.a360nautica.R.string.pointing_message), getString(R.string.ok))

    }
}