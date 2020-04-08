package com.example.myitsdriver.utils

import android.content.Context
import android.net.ConnectivityManager
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager


object Utils {

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}