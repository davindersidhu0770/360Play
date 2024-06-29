//package com.a360play.a360nautica.view.activities
//
//import android.app.Activity
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.content.res.AssetManager
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.util.Log
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.a360play.a360nautica.R
//import com.googlecode.tesseract.android.TessBaseAPI
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//import java.io.InputStream
//
//
//class OCRActivity : AppCompatActivity() {
//
//    private lateinit var imageView: ImageView
//    private lateinit var captureButton: Button
//    private lateinit var tvtext: TextView
//
//    private val CAMERA_REQUEST_CODE = 123
//    private val STORAGE_PERMISSION_REQUEST_CODE = 456
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.ocr_activity)
//
//        imageView = findViewById(R.id.imageView)
//        captureButton = findViewById(R.id.captureButton)
//        tvtext = findViewById(R.id.tvtext)
//
//        captureButton.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    this, arrayOf(android.Manifest.permission.CAMERA),
//                    CAMERA_REQUEST_CODE
//                )
//            } else {
//                captureImageFromCamera()
//
//
//            }
//        }
//    }
//
//    private fun captureImageFromCamera() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, CAMERA_REQUEST_CODE)
//    }
//
//    private fun processImage(bitmap: Bitmap) {
//        val recognizedText = performOCR(bitmap)
//        tvtext.setText(recognizedText)
//        println("Recognized text: $recognizedText")
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val imageBitmap: Bitmap? = data?.extras?.get("data") as Bitmap?
//            imageBitmap?.let {
//                imageView.setImageBitmap(imageBitmap)
////              processImage(imageBitmap)
//
///*
//                TextScanner.getInstance(this)
//                    .init()
//                    .load(imageBitmap) // uri or bitmap
//                    .getCallback(object : TextExtractCallback {
//                        override fun onGetExtractText(textList: List<String>) {
//                            // Here you will get a list of text
//
//                            tvtext.setText(textList.toString())
//
//                        }
//                    })
//*/
//            }
//        }
//    }
//
//    fun performOCR(bitmap: Bitmap): String {
//        val dataPath = Environment.getExternalStorageDirectory().toString() + "/tesseract/"
//        val tessdataPath = dataPath + "tessdata/"
//        val languageDataFileName = "eng.traineddata"
//
//        // Create the tessdata subfolder if it doesn't exist
//        val tessdataDir = File(tessdataPath)
//        if (!tessdataDir.exists()) {
//            tessdataDir.mkdirs()
//        }
//
//        // Copy the language data file from assets to the tessdata subfolder
//        val languageDataFile = File(tessdataPath, languageDataFileName)
//        if (!languageDataFile.exists()) {
//            copyLanguageDataFromAssets(languageDataFileName, languageDataFile)
//        }
//
//        // Initialize Tesseract API
//        val tess = TessBaseAPI()
//        tess.init(dataPath, "eng") // Specify the language here (e.g., "eng" for English)
//
//        // Set the image to be recognized
//        tess.setImage(bitmap)
//
//        // Perform OCR
//        val recognizedText = tess.utF8Text
//
//        // End the OCR process
//        tess.end()
//
//
//        Log.d("3July:", recognizedText)
//
//        return recognizedText
//    }
//
///*
//    @Throws(IOException::class)
//    private fun copyLanguageDataFromAssets(languageDataFileName: String, outputFile: File) {
//        val assetManager: AssetManager = assets
//        val inputStream: InputStream = assetManager.open("tessdata/$languageDataFileName")
//        val outputStream = FileOutputStream(outputFile)
//        val buffer = ByteArray(1024)
//        var read: Int = inputStream.read(buffer)
//        while (read != -1) {
//            outputStream.write(buffer, 0, read)
//            read = inputStream.read(buffer)
//        }
//        outputStream.flush()
//        outputStream.close()
//        inputStream.close()
//    }
//*/
//
//    @Throws(IOException::class)
//    private fun copyLanguageDataFromAssets(languageDataFileName: String, outputFile: File) {
//        val assetManager: AssetManager = this.assets
//        val inputStream: InputStream = assetManager.open(languageDataFileName)
//        val outputStream = FileOutputStream(outputFile)
//        val buffer = ByteArray(1024)
//        var read: Int = inputStream.read(buffer)
//        while (read != -1) {
//            outputStream.write(buffer, 0, read)
//            read = inputStream.read(buffer)
//        }
//        outputStream.flush()
//        outputStream.close()
//        inputStream.close()
//    }
//
//}