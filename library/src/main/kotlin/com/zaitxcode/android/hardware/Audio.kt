package com.zaitxcode.android.hardware

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import com.zaitxcode.android.core.AppHelper

object Audio {

    @JvmStatic
    @JvmOverloads
    fun playClickSound(context: Context = AppHelper.ctx()) {
        AppHelper.checkInitialized()
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            audioManager?.playSoundEffect(AudioManager.FX_KEY_CLICK)
        } catch (_: Exception) {
            // Guard
        }
    }

    @JvmStatic
    @JvmOverloads
    fun isMuted(context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            val mode = audioManager?.ringerMode ?: AudioManager.RINGER_MODE_NORMAL
            mode != AudioManager.RINGER_MODE_NORMAL
        } catch (_: Exception) {
            false
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getMusicVolume(context: Context = AppHelper.ctx()): Int {
        AppHelper.checkInitialized()
        return try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            if (audioManager == null) return 0
            val current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            if (max <= 0) 0 else ((current.toFloat() / max.toFloat()) * 100).toInt()
        } catch (_: Exception) {
            0
        }
    }

    @JvmStatic
    @JvmOverloads
    fun isHeadphonesConnected(context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return false
        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        return devices.any {
            it.type == AudioDeviceInfo.TYPE_WIRED_HEADSET ||
                it.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES ||
                it.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP ||
                it.type == AudioDeviceInfo.TYPE_USB_HEADSET
        }
    }
}
