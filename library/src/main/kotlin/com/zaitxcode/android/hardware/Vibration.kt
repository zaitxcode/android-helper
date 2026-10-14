package com.zaitxcode.android.hardware

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.zaitxcode.android.core.AppHelper

@SuppressLint("MissingPermission", "NewApi", "InlinedApi")
object Vibration {

    @JvmStatic
    @JvmOverloads
    fun vibrate(ms: Long = 500, context: Context = AppHelper.ctx()) {
        AppHelper.checkInitialized()
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        val effect = VibrationEffect.createOneShot(
            ms,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
        vibrator.vibrate(effect)
    }

    @JvmStatic
    @JvmOverloads
    fun vibratePattern(
        pattern: LongArray,
        repeat: Int = -1,
        context: Context = AppHelper.ctx()
    ) {
        AppHelper.checkInitialized()
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        val effect = VibrationEffect.createWaveform(pattern, repeat)
        vibrator.vibrate(effect)
    }

    @JvmStatic
    @JvmOverloads
    fun cancel(context: Context = AppHelper.ctx()) {
        AppHelper.checkInitialized()
        getVibrator(context)?.cancel()
    }

    private fun getVibrator(context: Context): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
                as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }
}
