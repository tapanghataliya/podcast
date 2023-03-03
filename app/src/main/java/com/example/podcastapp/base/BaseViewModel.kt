package com.example.podcastapp.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>:ViewModel() {
    private lateinit var navigator: WeakReference<N>
    val alertMessage: MutableLiveData<Pair<String, String>> =
        MutableLiveData<Pair<String, String>>()
    val progressMessage: MutableLiveData<Pair<String, String>> =
        MutableLiveData<Pair<String, String>>()

    fun getNavigator(): N? = navigator.get()

    fun setNavigator(navigator: N) {
        this.navigator = WeakReference(navigator)
    }
}