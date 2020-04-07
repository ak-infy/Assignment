package com.example.interviewassignment.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun isNetworkAvailable(context: Context): Boolean {
    val cm =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    if (cm != null) {
        if (Build.VERSION.SDK_INT < 23) {
            val networkInfo = cm.getActiveNetworkInfo()

            if (networkInfo != null) {
                return (networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE));
            }
        } else {
            val network = cm.getActiveNetwork();

            if (network != null) {
                val networkCapabilities = cm.getNetworkCapabilities(network);

                return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
            }
        }
    }
    return false
}