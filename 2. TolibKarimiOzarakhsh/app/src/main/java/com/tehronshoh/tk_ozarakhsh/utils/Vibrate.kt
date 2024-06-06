package com.tehronshoh.tk_ozarakhsh.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class Vibrate(val context: Context) {
    fun canVibrate(): Boolean =
        (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).hasVibrator()

    fun vibrate(millisecond: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    millisecond,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(millisecond)
        }
    }
}