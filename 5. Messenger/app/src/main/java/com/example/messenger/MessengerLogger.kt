package com.example.messenger

import android.util.Log

object MessengerLogger {
    private const val TAG = "MessengerLogger"

    fun logError(message: String? = "empty message", throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
    fun logDebug(message: String = "empty message") {
        Log.d(TAG, message)
    }
}
