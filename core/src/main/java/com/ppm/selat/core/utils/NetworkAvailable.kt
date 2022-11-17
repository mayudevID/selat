package com.ppm.selat.core.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager

fun isNetworkAvailable(activity: Activity): Boolean {
    val connectivityManager: ConnectivityManager =
        activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
        .isConnected
}