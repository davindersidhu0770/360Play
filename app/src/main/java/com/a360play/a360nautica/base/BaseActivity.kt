package com.a360play.a360nautica.base

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.a360play.a360nautica.R
import com.a360play.a360nautica.view.activities.MainActivity
import java.util.regex.Pattern


open class BaseActivity() : AppCompatActivity() {

    private var progressBar: ProgressBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgressBar()
    }

    fun clearStack() {
        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragmentManager.getBackStackEntryCount() == 0) {
            for (i in 0 until fragmentManager.backStackEntryCount) {
                fragmentManager.popBackStack()
            }
        }
        startActivity(Intent(this, MainActivity::class.java))

    }

    private fun initProgressBar() {
      /*  progressBar = ProgressBar(this, null, android.R.style.Widget_Material_Light_ProgressBar)
        progressBar!!.setIndeterminate(true)

        val relativeLayout = RelativeLayout(this)
        relativeLayout.setGravity(Gravity.CENTER)
        relativeLayout.addView(progressBar)

        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        progressBar!!.setVisibility(View.GONE)
        addContentView(relativeLayout, params)*/

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progressBar!!.max = 100
        progressBar!!.progress = 50
        linearLayout.addView(progressBar)
        setContentView(linearLayout)

    }


    protected open fun hideLoader() {
        progressBar!!.visibility = View.GONE
    }

    protected open fun displayLoader() {
        progressBar!!.visibility = View.VISIBLE


    }


    fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    open fun putFragment(fragment: Fragment?, container: View) {
        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(container.id, fragment!!)
        // ft.replace(R.id.DT410QeExd9KcRDM7n3OItQDo0jxpaT8uOE4n, fragment!!)
        ft.commit()
    }


    open fun checkForInternet(context: Context): Boolean {
        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    open fun addFragment(fragment: Fragment?, addToBackStack: Boolean, tag: String) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        if (addToBackStack) {
            ft.addToBackStack(null)
        }
        //  ft.replace(R.id.DT410QeExd9KcRDM7n3OItQDo0jxpaT8uOE4n, fragment!!)
        ft.add(R.id.ll_fragment, fragment!!, tag)
        ft.commit()
    }

    open fun replaceFragment(fragment: Fragment?) {
        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        ft.replace(R.id.ll_fragment, fragment!!)
        // ft.replace(R.id.DT410QeExd9KcRDM7n3OItQDo0jxpaT8uOE4n, fragment!!)
        ft.commit()
    }

    fun isValidEmail(email: CharSequence): Boolean {
        var isValid = true
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            isValid = false
        }
        return isValid
    }


    fun showAlertoExisttheApp() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id -> super@BaseActivity.onBackPressed() })
            .setNegativeButton("No", null)
            .show()

    }


}