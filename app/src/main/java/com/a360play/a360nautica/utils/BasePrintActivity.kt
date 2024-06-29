package com.a360play.a360nautica.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.a360play.a360nautica.utils.SunmiPrintHelper
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.a360play.a360nautica.R
import com.a360play.a360nautica.view.activities.MainActivity
import sunmi.sunmiui.dialog.EditTextDialog
import sunmi.sunmiui.dialog.DialogCreater
import java.util.regex.Pattern

abstract class BasePrintActivity : AppCompatActivity() {
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()
        initPrinterStyle()
        initProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null)
        }
    }

    /**
     * Initialize the printer
     * All style settings will be restored to default
     */
    private fun initPrinterStyle() {
        if (BluetoothUtil.isBlueToothPrinter) {
            BluetoothUtil.sendData(ESCUtil.init_printer())
        } else {
            SunmiPrintHelper.getInstance().initPrinter()
        }
    }

    /**
     * set title
     * @param title title name
     */
    fun setMyTitle(title: String?) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = title
        }
    }

    /**
     * set title
     * @param title title res
     */
    fun setMyTitle(@StringRes title: Int) {
        val actionBar = supportActionBar
        actionBar?.setTitle(title)
        //        setSubTitle();
    }

    /**
     * set sub title
     */
    fun setSubTitle() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            if (BluetoothUtil.isBlueToothPrinter) {
                actionBar.setSubtitle("bluetooth®")
            } else {
                if (SunmiPrintHelper.getInstance().sunmiPrinter == SunmiPrintHelper.NoSunmiPrinter) {
                    actionBar.setSubtitle("no printer")
                } else if (SunmiPrintHelper.getInstance().sunmiPrinter == SunmiPrintHelper.CheckSunmiPrinter) {
                    actionBar.subtitle = "connecting"
                    handler!!.postDelayed({ setSubTitle() }, 2000)
                } else if (SunmiPrintHelper.getInstance().sunmiPrinter == SunmiPrintHelper.FoundSunmiPrinter) {
                    actionBar.subtitle = ""
                } else {
                    SunmiPrintHelper.getInstance().initSunmiPrinterService(this)
                }
            }
        }
    }

    /**
     * set back
     */
    fun setBack() {
        /*  ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.hexprint, menu)
        return true
    }

    var mEditTextDialog: EditTextDialog? = null
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_print -> {
                mEditTextDialog = DialogCreater.createEditTextDialog(
                    this,
                    resources.getString(R.string.cancel),
                    resources.getString(R.string.confirm),
                    resources.getString(
                        R.string.input_order
                    ),
                    { mEditTextDialog!!.cancel() },
                    {
                        val text = mEditTextDialog!!.editText.text.toString()
                        val data = BytesUtil.getBytesFromHexString(text)
                        if (BluetoothUtil.isBlueToothPrinter) {
                            BluetoothUtil.sendData(data)
                        } else {
                            SunmiPrintHelper.getInstance().sendRawData(data)
                        }
                        mEditTextDialog!!.cancel()
                    },
                    null
                )
                mEditTextDialog?.show()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private var progressBar: ProgressBar? = null


    /* override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)

     }
 */
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
        progressBar = ProgressBar(this, null, android.R.style.Widget_Material_Light_ProgressBar)
        progressBar!!.setIndeterminate(true)

        val relativeLayout = RelativeLayout(this)
        relativeLayout.setGravity(Gravity.CENTER)
        relativeLayout.addView(progressBar)

        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        progressBar!!.setVisibility(View.GONE)
        addContentView(relativeLayout, params)
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
                DialogInterface.OnClickListener { dialog, id -> super@BasePrintActivity.onBackPressed() })
            .setNegativeButton("No", null)
            .show()

    }

}