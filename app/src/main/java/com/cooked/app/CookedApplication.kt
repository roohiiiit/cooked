package com.cooked.app

import android.app.Application
import app.rive.runtime.kotlin.core.Rive

class CookedApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Rive.init(this)
    }
}
