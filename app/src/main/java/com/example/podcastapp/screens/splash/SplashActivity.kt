package com.example.podcastapp.screens.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.podcastapp.R
import com.example.podcastapp.base.BaseActivity
import com.example.podcastapp.screens.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity: BaseActivity(), SplashNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        navigateToDashboard()
    }

    override fun navigateToDashboard() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
                finish()
            }
        }, 2000)
    }
}