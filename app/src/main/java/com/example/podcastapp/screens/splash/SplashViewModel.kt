package com.example.podcastapp.screens.splash

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.podcastapp.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : ViewModel() {
    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()
    fun checkNetworkConnection(context: Context) {
        isNetworkConnectedLiveData.value = NetworkUtils.isInternetAvailable(context)
    }
}