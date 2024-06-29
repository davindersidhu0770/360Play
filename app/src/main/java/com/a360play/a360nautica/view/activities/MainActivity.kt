package com.a360play.a360nautica.view.activities

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseActivity
import com.a360play.a360nautica.databinding.ActivityMainBinding
import com.a360play.a360nautica.view.fragment.book.BookStationsFragment
import com.a360play.a360nautica.view.fragment.book.EnquiryFragment
import com.a360play.a360nautica.view.fragment.card.DemoFragmentCardSecond
import com.a360play.a360nautica.view.fragment.entry.EntryPointFragment
import com.a360play.a360nautica.view.fragment.exit.ExitGateFragment
import com.a360play.a360nautica.viewmodels.BookingViewModel
import com.app.starterkit.network.Repository
import com.app.starterkit.network.RetrofitClient
import com.app.starterkit.utils.MyViewModelFactory
import com.app.starterkit.utils.Status
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.experimental.and
import com.a360play.a360nautica.BuildConfig


class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var appVersion: String
    private var forceUpdate: Boolean = false
    lateinit var binding: ActivityMainBinding
    lateinit var shared: SharedPreferences
    var writeMode = false
    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    lateinit var writeTagFilters: Array<IntentFilter>
    var myTag: Tag? = null
    private lateinit var tvNFCContent: TextView
    var listener: DataReceivedListener? = null
    val repository = Repository(RetrofitClient.apiService)

    fun setDataReceivedListener(listener: DataReceivedListener?) {
        this.listener = listener
    }

    public interface DataReceivedListener {
        open fun onReceived(barcode: String) // pass any parameter in your onCallBack which you want to return

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listeners()
        initview()
        bindObservers()

    }

    private fun checkForUpdate() {
        // Send a request to your server to get the latest version
        val latestVersion = appVersion

        Log.d("Version:","latestVersion "+latestVersion)
        // Compare with the current version of the app
        val currentVersion = BuildConfig.VERSION_NAME
        Log.d("Version:","currentVersion "+currentVersion)

        if (latestVersion > currentVersion) {
            // Show a dialog or screen prompting the user to update
            showUpdateDialog(latestVersion,currentVersion)
        }
    }

    private fun showUpdateDialog(latestVersion: String, currentVersion: String) {
        // Show a dialog to prompt the user to update
        val dialog = AlertDialog.Builder(this)
            .setTitle("Update Available")
            .setMessage("A new version of the app "+ latestVersion +" is available. Please update to the latest version. You are using an old version"+currentVersion + "\n"+"        "+" ")
           /* .setPositiveButton("Update") { _, _ ->
                val internalTestUrl = "https://play.google.com/store/apps/details?id=com.a360play.a360nautica&hl=en-US&ah=spv2vu8LV50Uvh3Izv1Wc-UxOaQ&pli=1"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(internalTestUrl)

                val activities = packageManager.queryIntentActivities(intent, 0)
                val isIntentSafe = activities.isNotEmpty()

                if (isIntentSafe) {
                    // Play Store app is available, launch it
                    startActivity(intent)
                } else {
                    // No Play Store app available, open in a web browser
                    val webIntent = Intent(Intent.ACTION_VIEW)
                    webIntent.data = Uri.parse(internalTestUrl)

                    val webActivities = packageManager.queryIntentActivities(webIntent, 0)
                    val isWebIntentSafe = webActivities.isNotEmpty()

                    if (isWebIntentSafe) {
                        startActivity(webIntent)
                    } else {
                        // Handle the case where no suitable app is available to handle the intent
                        // Display an error message to the user

                        Toast.makeText(this,"App not available for this account",Toast.LENGTH_LONG).show()
                    }
                }
            }*/

            .create()

        dialog.setCancelable(false)
        dialog.show()
    }

    /*private fun getLatestVersionFromServer(): String {
        // Simulate fetching the latest version from your server
        return "1.17"
    }*/

    public override fun onPause() {
        super.onPause()
        WriteModeOff()
    }

    override fun onResume() {
        super.onResume()

        Log.d("Version","onResume")

        getLatestVersionFromServer()
        WriteModeOn()
    }

    private val viewModel: BookingViewModel by viewModels() {
        MyViewModelFactory(repository)
    }

    private fun getLatestVersionFromServer(){

        if (checkForInternet(this)) {
            viewModel.getCurrentVersion()
        } else {

            Toast.makeText(this,"No Internet Connection...",Toast.LENGTH_LONG).show()

        }

    }
    private fun bindObservers() {
        viewModel.versionCheckResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    displayLoader()
                    Toast.makeText(
                        this@MainActivity,
                        "Processing.....",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                Status.SUCCEESS -> {
                    hideLoader()
                    if (it.data!!.isSuccess) {

                        appVersion= it.data.data.appVersion
                        forceUpdate= it.data.data.forceUpdate
                        checkForUpdate()
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

    private fun WriteModeOn() {
        writeMode = true
        if (nfcAdapter != null)
            nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null)
    }

    public fun showBottomBar() {
        binding.llbottomtab.visibility = View.VISIBLE

    }

    public fun hideBottomBar() {

        binding.llbottomtab.visibility = View.GONE
    }

    private fun WriteModeOff() {
        writeMode = false
        if (nfcAdapter != null)
            nfcAdapter!!.disableForegroundDispatch(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("7April:", "onActivityResult Acti")
    }

    fun changeBackground(selected_view: View, firstView: View, second: View, third: View) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            selected_view.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_circular_shape_yellow))
            firstView.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_circular_shape_grey))
            second.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_circular_shape_grey))
            third.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_circular_shape_grey))
        } else {
            selected_view.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_circular_shape_yellow
                )
            )
            firstView.setBackground(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_circular_shape_grey
                )
            )
            second.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_circular_shape_grey))
            third.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_circular_shape_grey))
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initview() {
        shared = getSharedPreferences(
            resources.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
        tvNFCContent = binding.nfcContents

        var booking = shared.getBoolean("book_status", false)
        var entery = shared.getBoolean("entry_status", false)
        var exit = shared.getBoolean("exit_status", false)

        setBottomView(booking, entery, exit)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
//          finish()
            val edit = shared.edit()
            edit.putString("devicetype", "njx")
            edit.apply()

            Log.d("DType", "njx")
        } else {
            val edit = shared.edit()
            edit.putString("devicetype", "sunmi")
            edit.apply()
            Log.d("DType", "sunmi")

        }

        //For when the activity is launched by the intent-filter for android.nfc.action.NDEF_DISCOVERE
        readFromIntent(getIntent())
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE
        )
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT)
        writeTagFilters = arrayOf(tagDetected)
    }

    private fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            myTag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            assert(myTag != null)
            /*val payload1: String = myTag?.let { detectTagData(it) }!!
            //set text here....

            //set text here....
            tag_data.setText(payload1)*/

            var msgs = mutableListOf<NdefMessage>()
            if (rawMsgs != null) {
                for (i in rawMsgs.indices) {
                    msgs.add(i, rawMsgs[i] as NdefMessage)
                }
                buildTagViews(msgs.toTypedArray())
            }
        }
    }

    private fun buildTagViews(msgs: Array<NdefMessage>) {
        if (msgs == null || msgs.isEmpty()) return
        var text = ""
        val payload = msgs[0].records[0].payload
        val textEncoding: Charset =
            if ((payload[0] and 128.toByte()).toInt() == 0) Charsets.UTF_8 else Charsets.UTF_16 // Get the Text Encoding
        val languageCodeLength: Int =
            (payload[0] and 51).toInt() // Get the Language Code, e.g. "en"
        try {
            // Get the Text
            text = String(
                payload,
                languageCodeLength + 1,
                payload.size - languageCodeLength - 1,
                textEncoding
            )
        } catch (e: UnsupportedEncodingException) {
            Log.e("UnsupportedEncoding", e.toString())
        }

        if (listener != null) {
            listener?.onReceived(text)
        }

//      tvNFCContent.text = "Message read from NFC Tag:\n $text"
    }

    public fun setBottomView(booking: Boolean, entery: Boolean, exit: Boolean) {
        val fr = BookStationsFragment()//change me
        val bundle = Bundle()
        fr.setArguments(bundle)
        /*val fr = EnquiryFragment()*/
        if (intent.getStringExtra("fromscreen").equals("recharge_card")) {
            val bundle = Bundle()
            bundle.putString("fromscreen", intent.getStringExtra("recharge_card"))
            bundle.putString("username", intent.getStringExtra("username"))
            bundle.putString("email", intent.getStringExtra("email"))
            bundle.putString("phone", intent.getStringExtra("phone"))
            bundle.putString("barcode", intent.getStringExtra("barcode"))

            fr.setArguments(bundle)
        }
        if (booking && entery && exit) {

            replaceFragment(fr)
            changeBackground(binding.llBooking, binding.llEntry, binding.llExit, binding.llCard)
            binding.llBookparent.visibility = View.VISIBLE
            binding.llEnterparent.visibility = View.VISIBLE
            binding.llExitparent.visibility = View.VISIBLE
            binding.llView1.visibility = View.VISIBLE
            binding.llView2.visibility = View.VISIBLE
            return
        } else if (booking && !entery && !exit) {
            replaceFragment(fr)
            changeBackground(binding.llBooking, binding.llEntry, binding.llExit, binding.llCard)
            binding.llBookparent.visibility = View.VISIBLE
            return
        } else if (booking && entery && !exit) {
            replaceFragment(fr)
            changeBackground(binding.llBooking, binding.llEntry, binding.llExit, binding.llCard)
            binding.llView1.visibility = View.VISIBLE
            binding.llBookparent.visibility = View.VISIBLE
            binding.llEnterparent.visibility = View.VISIBLE
            return
        } else if (booking && !entery && exit) {
            replaceFragment(fr)
            changeBackground(binding.llBooking, binding.llEntry, binding.llExit, binding.llCard)
            binding.llView1.visibility = View.VISIBLE
            binding.llBookparent.visibility = View.VISIBLE
            binding.llExitparent.visibility = View.VISIBLE
            return
        } else if (!booking && entery && !exit) {
            replaceFragment(fr)
            changeBackground(binding.llEntry, binding.llBooking, binding.llExit, binding.llCard)
            binding.llEnterparent.visibility = View.VISIBLE
            return
        } else if (!booking && entery && exit) {
            replaceFragment(fr)
            changeBackground(binding.llEntry, binding.llBooking, binding.llExit, binding.llCard)
            binding.llView2.visibility = View.VISIBLE
            binding.llEnterparent.visibility = View.VISIBLE
            binding.llExitparent.visibility = View.VISIBLE
            return
        } else if (!booking && !entery && exit) {
            replaceFragment(fr)
            changeBackground(binding.llExit, binding.llBooking, binding.llEntry, binding.llCard)
            binding.llExitparent.visibility = View.VISIBLE
            return
        } else if (!booking && !entery && !exit) {
            replaceFragment(fr)
            changeBackground(binding.llCard, binding.llBooking, binding.llEntry, binding.llExit)
//            binding.llCardparent.visibility = View.VISIBLE
            return
        } else {
            replaceFragment(fr)
            changeBackground(binding.llCard, binding.llBooking, binding.llEntry, binding.llExit)
//            binding.llCardparent.visibility = View.VISIBLE
            return
        }
    }

    private fun listeners() {
        binding.llBooking.setOnClickListener(this)
        /*
           binding.llCard.setOnClickListener(this)
   */   binding.llEntry.setOnClickListener(this)
        binding.llExit.setOnClickListener(this)
        binding.ivenquiry.setOnClickListener() {

            replaceFragment(EnquiryFragment())
            binding.ivenquiry.visibility = View.GONE
        }
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            binding.llBooking.id -> {
                changeBackground(binding.llBooking, binding.llEntry, binding.llExit, binding.llCard)
                replaceFragment(BookStationsFragment())
                binding.ivenquiry.visibility = View.VISIBLE
            }

            binding.llEntry.id -> {
                changeBackground(binding.llEntry, binding.llBooking, binding.llExit, binding.llCard)
                replaceFragment(EntryPointFragment())
                binding.ivenquiry.visibility = View.VISIBLE

            }

            binding.llExit.id -> {
                changeBackground(binding.llExit, binding.llBooking, binding.llEntry, binding.llCard)
                replaceFragment(ExitGateFragment())
                binding.ivenquiry.visibility = View.VISIBLE

            }

            binding.llCard.id -> {
                changeBackground(binding.llCard, binding.llExit, binding.llBooking, binding.llEntry)
                replaceFragment(DemoFragmentCardSecond())
                binding.ivenquiry.visibility = View.VISIBLE

            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
//        Toast.makeText(this, "OnNewIntent.....", Toast.LENGTH_SHORT).show()

        readFromIntent(intent)

    }

    override fun onBackPressed() {
        showBottomBar()
        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragmentManager.getBackStackEntryCount() == 0) {
            showAlertoExisttheApp()
        } else {
            fragmentManager.popBackStack()
        }
    }


    fun getFragmentCount(): Int {
        val fragmentManager: FragmentManager = supportFragmentManager
        var fragmentCount = fragmentManager.getBackStackEntryCount()
        return fragmentCount

    }



}