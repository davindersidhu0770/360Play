package com.a360play.a360nautica.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseActivity
import com.a360play.a360nautica.data.booking.NationalityDataResponse
import com.a360play.a360nautica.databinding.ActivityLoginBinding
import com.a360play.a360nautica.utils.ConnectivityLiveData
import com.a360play.a360nautica.utils.CustomSnackbar
import com.a360play.a360nautica.viewmodels.LoginViewModel
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient
import com.app.starterkit.utils.MyViewModelFactory
import com.app.starterkit.utils.Status

class LoginActivity : BaseActivity() {

    private var versionFromApi: String = ""
    lateinit var binding: ActivityLoginBinding
    val apiService = RetrofitClient.apiService
    val repository = Repository(apiService)
    lateinit var shared: SharedPreferences

    private val viewModel: LoginViewModel by viewModels() {
        MyViewModelFactory(repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
        hideStatusBar()
        bindObservers()
        listeners()
    }

    private fun initview() {
        shared = getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    private fun bindObservers() {
        viewModel.loginResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    Toast.makeText(this, "Processing...", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCEESS -> {
                    hideLoader()
                    if (it.data!!.isSuccess) {

                        val edit = shared.edit()
                        edit.putString("userID", it.data.data.id.toString())
                        if (it.data.data.currency != null)
                            edit.putString("currency", it.data.data.currency.toString())
                        else
                            edit.putString("currency", "")

                        if (it.data.data.country != null)
                            edit.putString("countryId", it.data.data.country.toString())
                        else
                            edit.putString("countryId", "")

                        if (it.data.data.name != null)
                            edit.putString("username", it.data.data.name.toString())
                        else
                            edit.putString("username", "")

                        if (it.data.data.timeZone != null) {
                            val strs = it.data.data.timeZone.split("|").toTypedArray()
                            Log.d("14ap:", strs[1])
                            edit.putString("ctimezone", strs[1])

                        } else
                            edit.putString("ctimezone", "")

                        edit.putBoolean("book_status", it.data.data.book)
                        edit.putBoolean("entry_status", it.data.data.entry)
                        edit.putBoolean("exit_status", it.data.data.exit)
                        edit.putBoolean("exit_status", it.data.data.exit)
                        edit.apply()
                        Toast.makeText(this, "Logged In Successfully...", Toast.LENGTH_SHORT)
                            .show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                    }

                }
                Status.ERROR -> {
                    hideLoader()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun listeners() {
        binding.bntLogincontinue.setOnClickListener({
            validationsAndhit()
        })


        Log.d("24July:", getCurrentAppVersion(this))
    }

/*
    private fun checkAppVersion(): Boolean {
  //    Get the current version of the app from the package manager
//      val currentVersion = getCurrentAppVersion(this)

        var boolean: Boolean
        boolean = true
        val currentVersion = resources.getString(R.string.appversion)

        // Compare the two versions
        if (currentVersion != versionFromApi) {
            boolean = false
            showUpdateDialog()
        }

        return boolean
    }
*/

/*
    private fun showUpdateDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Update Required")
        dialogBuilder.setMessage("A new version of the app "+
                versionFromApi +" is available. Please update to the latest version. You are using "+resources.getString(R.string.appversion))
       */
/* dialogBuilder.setPositiveButton("Update") { _, _ ->
            // Redirect the user to the app's page on the Play Store for update
            // Example: You can use the following code to redirect to the Play Store.
            // val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            // startActivity(intent)

            // NOTE: The above code is commented out as it may not work directly in the model.
            // In a real Android app, uncomment the code above to redirect the user to the Play Store.
        }*//*

        dialogBuilder.setNegativeButton("Okay") { dialog, _ ->
            dialog.dismiss()
            // You can choose to exit the app or allow the user to continue with the current version.
        }

        dialogBuilder.setCancelable(false)
        val dialog = dialogBuilder.create()
        dialog.show()
    }
*/

    fun getCurrentAppVersion(context: Context): String {
        val packageManager = context.packageManager
        val packageName = context.packageName
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName


        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }


    private fun validationsAndhit() {
        if (binding.edEmail.text.toString().equals("")) {
            Toast.makeText(this, "Please enter the email address first", Toast.LENGTH_SHORT).show()
        } else if (!isValidEmail(binding.edEmail.text.toString())) {
            Toast.makeText(this, "Please enter the valid email address", Toast.LENGTH_SHORT)
                .show()
        } else if (binding.edPassword.text.toString().equals("")) {
            Toast.makeText(this, "Please enter the password first", Toast.LENGTH_SHORT).show()
        } else {
            val map = HashMap<String, String>()
            map["Email"] = binding.edEmail.text.toString()
            map["Password"] = binding.edPassword.text.toString()
            if (checkForInternet(this)) {
                viewModel.login(map)
            } else {
                CustomSnackbar.make(
                    window.decorView.rootView as ViewGroup,
                    "No Internet Connection..."
                ).show()
            }

        }
    }
}