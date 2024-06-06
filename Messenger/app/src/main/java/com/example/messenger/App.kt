package com.example.messenger

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.example.messenger.repository.SharedPreferencesRepository
import com.example.messenger.util.ContextUtils
import com.example.messenger.util.NetworkMonitor
import java.util.*

class App: Application() {
    override fun attachBaseContext(newBase: Context) {
        SharedPreferencesRepository.init(newBase)
        NetworkMonitor.init(newBase)

        val localeToSwitchTo = Locale(SharedPreferencesRepository.get().language)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}