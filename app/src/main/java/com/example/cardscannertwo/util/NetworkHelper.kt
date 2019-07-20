package com.example.cardscannertwo.util

import android.content.Context
import android.net.ConnectivityManager



    fun Context.isNetworkConnected(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activetNetwork = cm.activeNetworkInfo
        return activetNetwork?.isConnected ?: false
    }
