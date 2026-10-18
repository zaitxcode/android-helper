package com.zaitxcode.android.security

import com.zaitxcode.android.core.AppHelper
import java.security.MessageDigest

object RustEngine {

    private var isRustLoaded = false

    init {
        try {
            System.loadLibrary("androidhelper_rust")
            isRustLoaded = nativeRustIsLoaded()
        } catch (_: Throwable) {
            isRustLoaded = false
        }
    }

    @JvmStatic
    fun isAvailable(): Boolean {
        AppHelper.checkInitialized()
        return isRustLoaded
    }

    @JvmStatic
    @Suppress("KotlinJniMissingFunction")
    private external fun nativeRustIsLoaded(): Boolean

    @JvmStatic
    @Suppress("KotlinJniMissingFunction")
    private external fun nativeRustSha3(text: String): String

    @JvmStatic
    @Suppress("KotlinJniMissingFunction")
    private external fun nativeRustKeccak256(text: String): String

    @JvmStatic
    @Suppress("KotlinJniMissingFunction")
    private external fun nativeRustChaCha20Encrypt(text: String, key: String): String

    @JvmStatic
    fun sha3(text: String): String {
        AppHelper.checkInitialized()
        if (isRustLoaded) {
            try {
                return nativeRustSha3(text)
            } catch (_: Throwable) {
                // Fallback
            }
        }
        return try {
            val digest = MessageDigest.getInstance("SHA3-256")
            val bytes = digest.digest(text.toByteArray(Charsets.UTF_8))
            val sb = StringBuilder()
            for (b in bytes) {
                sb.append(String.format("%02x", b))
            }
            sb.toString()
        } catch (_: Exception) {
            Encryption.sha256(text)
        }
    }

    @JvmStatic
    fun keccak256(text: String): String {
        AppHelper.checkInitialized()
        if (isRustLoaded) {
            try {
                return nativeRustKeccak256(text)
            } catch (_: Throwable) {
                // Fallback
            }
        }
        return Encryption.sha256(text)
    }

    @JvmStatic
    fun chacha20Encrypt(text: String, key: String): String {
        AppHelper.checkInitialized()
        if (isRustLoaded) {
            try {
                return nativeRustChaCha20Encrypt(text, key)
            } catch (_: Throwable) {
                // Fallback
            }
        }
        return CryptoGcm.aesGcmEncrypt(text, key)
    }
}
