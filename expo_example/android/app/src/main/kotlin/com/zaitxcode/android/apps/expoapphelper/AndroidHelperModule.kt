package com.zaitxcode.android.apps.expoapphelper

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.zaitxcode.android.app.AppInfo
import com.zaitxcode.android.app.AppState
import com.zaitxcode.android.content.Clipboard
import com.zaitxcode.android.hardware.Audio
import com.zaitxcode.android.hardware.Battery
import com.zaitxcode.android.hardware.Biometric
import com.zaitxcode.android.hardware.Device
import com.zaitxcode.android.hardware.Vibration
import com.zaitxcode.android.net.Network
import com.zaitxcode.android.security.Encryption
import com.zaitxcode.android.security.RustEngine
import com.zaitxcode.android.io.Storage
import com.zaitxcode.android.util.Time
import com.zaitxcode.android.util.Validation

class AndroidHelperModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "AndroidHelper"

    @ReactMethod
    fun isConnected(promise: Promise) {
        promise.resolve(Network.isConnected)
    }

    @ReactMethod
    fun activeTransport(promise: Promise) {
        promise.resolve(Network.activeTransport())
    }

    @ReactMethod
    fun vibrate(ms: Double) {
        Vibration.vibrate(ms.toLong())
    }

    @ReactMethod
    fun playClickSound() {
        Audio.playClickSound()
    }

    @ReactMethod
    fun copyText(text: String) {
        Clipboard.copyText(text)
    }

    @ReactMethod
    fun getCopiedText(promise: Promise) {
        promise.resolve(Clipboard.getText())
    }

    @ReactMethod
    fun getBatteryLevel(promise: Promise) {
        promise.resolve(Battery.getBatteryLevel())
    }

    @ReactMethod
    fun isCharging(promise: Promise) {
        promise.resolve(Battery.isCharging())
    }

    @ReactMethod
    fun getDeviceName(promise: Promise) {
        promise.resolve(Device.deviceName())
    }

    @ReactMethod
    fun isDarkMode(promise: Promise) {
        promise.resolve(Device.isDarkMode())
    }

    @ReactMethod
    fun sha256(text: String, promise: Promise) {
        promise.resolve(Encryption.sha256(text))
    }

    @ReactMethod
    fun sha3(text: String, promise: Promise) {
        promise.resolve(RustEngine.sha3(text))
    }

    @ReactMethod
    fun isNativeEngineAvailable(promise: Promise) {
        promise.resolve(Encryption.isNativeEngineAvailable())
    }

    @ReactMethod
    fun isRustAvailable(promise: Promise) {
        promise.resolve(RustEngine.isAvailable())
    }

    @ReactMethod
    fun getPackageName(promise: Promise) {
        promise.resolve(AppInfo.packageName())
    }

    @ReactMethod
    fun isValidEmail(email: String, promise: Promise) {
        promise.resolve(Validation.isValidEmail(email))
    }

    @ReactMethod
    fun formatBytes(bytes: Double, promise: Promise) {
        promise.resolve(Storage.formatBytes(bytes.toLong()))
    }
}
