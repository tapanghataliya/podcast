package com.example.podcastapp.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    fun hideKeyboard() {
        val view = currentFocus
        view?.let { v ->
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager?.let {
                it.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

}