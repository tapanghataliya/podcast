package com.example.podcastapp.screens.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.podcastapp.R
import com.example.podcastapp.base.BaseActivity
import com.example.podcastapp.screens.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity: BaseActivity(), SplashNavigator {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.isNetworkConnectedLiveData.observe(this, Observer { isConnected ->
            if (isConnected) {
                navigateToDashboard()
            } else {
                Toast.makeText(this,"Please check your internet connection!", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.checkNetworkConnection(this)
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