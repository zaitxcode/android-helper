package com.zaitxcode.android.hardware

import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.SystemClock
import com.zaitxcode.android.core.AppHelper
import java.util.Locale

object Device {

    @JvmStatic fun manufacturer(): String {
        AppHelper.checkInitialized()
        return Build.MANUFACTURER ?: ""
    }

    @JvmStatic fun model(): String {
        AppHelper.checkInitialized()
        return Build.MODEL ?: ""
    }

    @JvmStatic fun brand(): String {
        AppHelper.checkInitialized()
        return Build.BRAND ?: ""
    }

    @JvmStatic fun sdk(): Int {
        AppHelper.checkInitialized()
        return Build.VERSION.SDK_INT
    }

    @JvmStatic fun androidVersion(): String {
        AppHelper.checkInitialized()
        return Build.VERSION.RELEASE ?: ""
    }

    @JvmStatic fun supportedAbis(): List<String> {
        AppHelper.checkInitialized()
        return Build.SUPPORTED_ABIS?.toList() ?: emptyList()
    }

    @JvmStatic
    fun deviceName(): String {
        AppHelper.checkInitialized()
        return listOfNotNull(Build.MANUFACTURER, Build.MODEL)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .trim()
    }

    @JvmStatic
    @JvmOverloads
    fun getTotalRam(context: Context = AppHelper.ctx()): Long {
        AppHelper.checkInitialized()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return 0L
        val memoryInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem
    }

    @JvmStatic
    @JvmOverloads
    fun getFreeRam(context: Context = AppHelper.ctx()): Long {
        AppHelper.checkInitialized()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return 0L
        val memoryInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem
    }

    @JvmStatic fun getUptimeMillis(): Long {
        AppHelper.checkInitialized()
        return try {
            SystemClock.elapsedRealtime()
        } catch (_: Exception) {
            0L
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getDeviceLanguage(context: Context = AppHelper.ctx()): String {
        AppHelper.checkInitialized()
        return context.resources.configuration.locales.get(0).language
    }

    @JvmStatic
    @JvmOverloads
    fun isDarkMode(context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        val mode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

    @JvmStatic
    @JvmOverloads
    fun isTablet(context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        val screenLayout = context.resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK
        return screenLayout >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    @JvmStatic
    fun isEmulator(): Boolean {
        AppHelper.checkInitialized()
        val fingerprint = (Build.FINGERPRINT ?: "").lowercase(Locale.US)
        val model = (Build.MODEL ?: "").lowercase(Locale.US)
        val manufacturer = (Build.MANUFACTURER ?: "").lowercase(Locale.US)
        val brand = (Build.BRAND ?: "").lowercase(Locale.US)
        val device = (Build.DEVICE ?: "").lowercase(Locale.US)
        val product = (Build.PRODUCT ?: "").lowercase(Locale.US)

        return fingerprint.startsWith("generic") ||
            fingerprint.contains("emulator") ||
            model.contains("google_sdk") ||
            model.contains("emulator") ||
            model.contains("android sdk built for") ||
            manufacturer.contains("genymotion") ||
            brand.startsWith("generic") && device.startsWith("generic") ||
            product == "google_sdk"
    }
}
