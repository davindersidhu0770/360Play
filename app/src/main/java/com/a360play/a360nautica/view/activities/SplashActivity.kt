package com.a360play.a360nautica.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.a360play.a360nautica.BuildConfig
import com.a360play.a360nautica.R
import com.a360play.a360nautica.base.BaseActivity
import com.a360play.a360nautica.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
    }

    private fun initview() {
        val versionCode = BuildConfig.VERSION_NAME
        binding.tvVersioncode.text="Version : "+versionCode

//        binding.tvVersioncode.text="Version : "+resources.getString(R.string.appversion)

        hideStatusBar()
        lifecycleScope.launch {
            delay(3500)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }
}