package com.tehronshoh.optimalroute

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("bb070786-988b-45a6-8014-c8268895b1d4")
    }
}