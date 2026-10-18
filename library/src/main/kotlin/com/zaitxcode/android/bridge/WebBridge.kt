package com.zaitxcode.android.bridge

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.zaitxcode.android.app.AppInfo
import com.zaitxcode.android.app.AppState
import com.zaitxcode.android.content.Clipboard
import com.zaitxcode.android.core.AppHelper
import com.zaitxcode.android.hardware.Audio
import com.zaitxcode.android.hardware.Battery
import com.zaitxcode.android.hardware.Biometric
import com.zaitxcode.android.hardware.Device
import com.zaitxcode.android.hardware.Vibration
import com.zaitxcode.android.net.Network
import com.zaitxcode.android.security.Encryption
import com.zaitxcode.android.io.Storage
import com.zaitxcode.android.util.Time
import com.zaitxcode.android.util.Validation

object WebBridge {

    const val DEFAULT_BRIDGE_NAME = "AndroidHelper"

    @JvmStatic
    @JvmOverloads
    @SuppressLint("SetJavaScriptEnabled")
    fun attach(webView: WebView, interfaceName: String = DEFAULT_BRIDGE_NAME) {
        AppHelper.checkInitialized()
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(InterfaceHandler(), interfaceName)
    }

    @JvmStatic
    @JvmOverloads
    fun detach(webView: WebView, interfaceName: String = DEFAULT_BRIDGE_NAME) {
        AppHelper.checkInitialized()
        try {
            webView.removeJavascriptInterface(interfaceName)
        } catch (_: Exception) {
            // Guard
        }
    }

    @JvmStatic
    @JvmOverloads
    fun evaluateJs(
        webView: WebView,
        script: String,
        callback: ((String?) -> Unit)? = null
    ) {
        AppHelper.checkInitialized()
        try {
            webView.post {
                webView.evaluateJavascript(script) { result ->
                    callback?.invoke(result)
                }
            }
        } catch (_: Exception) {
            callback?.invoke(null)
        }
    }

    class InterfaceHandler {

        @JavascriptInterface
        fun isConnected(): Boolean {
            AppHelper.checkInitialized()
            return Network.isConnected
        }

        @JavascriptInterface
        fun activeTransport(): String {
            AppHelper.checkInitialized()
            return Network.activeTransport()
        }

        @JavascriptInterface
        fun vibrate(ms: Long) {
            AppHelper.checkInitialized()
            Vibration.vibrate(ms)
        }

        @JavascriptInterface
        fun playClickSound() {
            AppHelper.checkInitialized()
            Audio.playClickSound()
        }

        @JavascriptInterface
        fun copyText(text: String) {
            AppHelper.checkInitialized()
            Clipboard.copyText(text)
        }

        @JavascriptInterface
        fun getCopiedText(): String? {
            AppHelper.checkInitialized()
            return Clipboard.getText()
        }

        @JavascriptInterface
        fun getBatteryLevel(): Int {
            AppHelper.checkInitialized()
            return Battery.getBatteryLevel()
        }

        @JavascriptInterface
        fun isCharging(): Boolean {
            AppHelper.checkInitialized()
            return Battery.isCharging()
        }

        @JavascriptInterface
        fun getDeviceName(): String {
            AppHelper.checkInitialized()
            return Device.deviceName()
        }

        @JavascriptInterface
        fun getManufacturer(): String {
            AppHelper.checkInitialized()
            return Device.manufacturer()
        }

        @JavascriptInterface
        fun getModel(): String {
            AppHelper.checkInitialized()
            return Device.model()
        }

        @JavascriptInterface
        fun isDarkMode(): Boolean {
            AppHelper.checkInitialized()
            return Device.isDarkMode()
        }

        @JavascriptInterface
        fun isTablet(): Boolean {
            AppHelper.checkInitialized()
            return Device.isTablet()
        }

        @JavascriptInterface
        fun isEmulator(): Boolean {
            AppHelper.checkInitialized()
            return Device.isEmulator()
        }

        @JavascriptInterface
        fun sha256(text: String): String {
            AppHelper.checkInitialized()
            return Encryption.sha256(text)
        }

        @JavascriptInterface
        fun base64Encode(text: String): String {
            AppHelper.checkInitialized()
            return Encryption.base64Encode(text)
        }

        @JavascriptInterface
        fun base64Decode(text: String): String {
            AppHelper.checkInitialized()
            return Encryption.base64Decode(text)
        }

        @JavascriptInterface
        fun isNativeEngineAvailable(): Boolean {
            AppHelper.checkInitialized()
            return Encryption.isNativeEngineAvailable()
        }

        @JavascriptInterface
        fun getPackageName(): String {
            AppHelper.checkInitialized()
            return AppInfo.packageName()
        }

        @JavascriptInterface
        fun getVersionName(): String {
            AppHelper.checkInitialized()
            return AppInfo.versionName()
        }

        @JavascriptInterface
        fun isAppInForeground(): Boolean {
            AppHelper.checkInitialized()
            return AppState.isAppInForeground()
        }

        @JavascriptInterface
        fun canAuthenticateBiometric(): Boolean {
            AppHelper.checkInitialized()
            return Biometric.canAuthenticate()
        }

        @JavascriptInterface
        fun isValidEmail(email: String): Boolean {
            AppHelper.checkInitialized()
            return Validation.isValidEmail(email)
        }

        @JavascriptInterface
        fun getTimeAgo(millis: Long): String {
            AppHelper.checkInitialized()
            return Time.timeAgo(millis)
        }

        @JavascriptInterface
        fun formatBytes(bytes: Long): String {
            AppHelper.checkInitialized()
            return Storage.formatBytes(bytes)
        }
    }
}
