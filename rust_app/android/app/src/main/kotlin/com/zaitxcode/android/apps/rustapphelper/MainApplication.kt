package com.zaitxcode.android.apps.rustapphelper

import android.app.Application
import com.zaitxcode.android.core.AppHelper

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppHelper.initialize(this)
    }
}
