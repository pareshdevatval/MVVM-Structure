package com.vsi.ui

import android.app.Application
import android.util.Log
import com.vsi.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    application: Application
) : BaseViewModel(application) {

    fun printLog() {
        Log.e("Hi", "Hi")
    }
}