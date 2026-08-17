package com.aimr.aimrpos.ui.haptics

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.ContextCompat

object HapticFeedbackUtils {

    fun lightClick(context: Context) {
        val activity = context as? Activity ?: return
        activity.currentFocus?.performHapticFeedback(android.view.HapticFeedbackConstants.LIGHT_CLICK)
    }

    fun heavyClick(context: Context) {
        val activity = context as? Activity ?: return
        activity.currentFocus?.performHapticFeedback(android.view.HapticFeedbackConstants.HEAVY_CLICK)
    }

    fun success(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val vibrator = getVibrator(context)
            val effect = VibrationEffect.createWaveform(
                longArrayOf(0, 30, 20, 40),
                intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE),
                -1
            )
            vibrator?.vibrate(effect)
        } else {
            val activity = context as? Activity
            activity?.currentFocus?.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
        }
    }

    fun error(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val vibrator = getVibrator(context)
            val effect = VibrationEffect.createWaveform(
                longArrayOf(0, 50, 50, 50),
                intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE),
                -1
            )
            vibrator?.vibrate(effect)
        } else {
            val activity = context as? Activity
            activity?.currentFocus?.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)
        }
    }

    fun scanSuccess(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val vibrator = getVibrator(context)
            val effect = VibrationEffect.createWaveform(
                longArrayOf(0, 20, 10, 30, 10, 40),
                intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE),
                -1
            )
            vibrator?.vibrate(effect)
        } else {
            val activity = context as? Activity
            activity?.currentFocus?.performHapticFeedback(android.view.HapticFeedbackConstants.LIGHT_CLICK)
        }
    }

    fun paymentSuccess(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val vibrator = getVibrator(context)
            val effect = VibrationEffect.createWaveform(
                longArrayOf(0, 25, 15, 25, 15, 50),
                intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE),
                -1
            )
            vibrator?.vibrate(effect)
        } else {
            val activity = context as? Activity
            activity?.currentFocus?.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
        }
    }

    private fun getVibrator(context: Context): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = ContextCompat.getSystemService(context, VibratorManager::class.java)
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            ContextCompat.getSystemService(context, Vibrator::class.java)
        }
    }
}