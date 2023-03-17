package com.example.podcastapp.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>:ViewModel() {
    private lateinit var navigator: WeakReference<N>

    fun getNavigator(): N? = navigator.get()

    fun setNavigator(navigator: N) {
        this.navigator = WeakReference(navigator)
    }
}