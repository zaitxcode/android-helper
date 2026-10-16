package com.zaitxcode.android.security

import android.util.Base64
import com.zaitxcode.android.core.AppHelper
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object Encryption {

    private const val GCM_IV_BYTES = 12
    private const val GCM_TAG_BITS = 128

    private var isNativeLoaded = false

    init {
        try {
            System.loadLibrary("androidhelper")
            isNativeLoaded = nativeIsLoaded()
        } catch (_: Throwable) {
            isNativeLoaded = false
        }
    }

    @JvmStatic
    fun isNativeEngineAvailable(): Boolean {
        AppHelper.checkInitialized()
        return isNativeLoaded
    }

    @JvmStatic
    @Suppress("KotlinJniMissingFunction")
    private external fun nativeIsLoaded(): Boolean

    @JvmStatic
    @Suppress("KotlinJniMissingFunction")
    private external fun nativeSha256(text: String): String

    @JvmStatic
    @Suppress("KotlinJniMissingFunction")
    private external fun nativeBase64Encode(text: String): String

    @JvmStatic
    @Suppress("KotlinJniMissingFunction")
    private external fun nativeBase64Decode(encodedText: String): String

    @JvmStatic fun md5(text: String): String {
        AppHelper.checkInitialized()
        return hash(text, "MD5")
    }

    @JvmStatic fun sha1(text: String): String {
        AppHelper.checkInitialized()
        return hash(text, "SHA-1")
    }

    @JvmStatic fun sha256(text: String): String {
        AppHelper.checkInitialized()
        if (isNativeLoaded) {
            try {
                return nativeSha256(text)
            } catch (_: Throwable) {
                // Fallback
            }
        }
        return hash(text, "SHA-256")
    }

    @JvmStatic fun sha512(text: String): String {
        AppHelper.checkInitialized()
        return hash(text, "SHA-512")
    }

    @JvmStatic fun hmacSha256(text: String, secret: String): String {
        AppHelper.checkInitialized()
        return try {
            val hmac = Mac.getInstance("HmacSHA256")
            val key = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "HmacSHA256")
            hmac.init(key)
            val bytes = hmac.doFinal(text.toByteArray(Charsets.UTF_8))
            bytesToHex(bytes)
        } catch (_: Exception) {
            ""
        }
    }

    /**
     * Legacy compatibility encryption using AES/ECB/PKCS5Padding.
     * ECB reveals repeated plaintext blocks and provides no integrity check.
     * Kept for backward compatibility only. For new sensitive data use
     * [aesGcmEncrypt] and [aesGcmDecrypt] instead.
     */
    @JvmStatic fun aesEncrypt(text: String, secretKey: String): String {
        AppHelper.checkInitialized()
        return try {
            val keyBytes = MessageDigest.getInstance("SHA-256").digest(secretKey.toByteArray(Charsets.UTF_8))
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            val encrypted = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
            base64Encode(String(encrypted, Charsets.ISO_8859_1))
        } catch (_: Exception) {
            ""
        }
    }

    /**
     * Legacy compatibility decryption for [aesEncrypt].
     * For new sensitive data use [aesGcmDecrypt] instead.
     */
    @JvmStatic fun aesDecrypt(encryptedText: String, secretKey: String): String {
        AppHelper.checkInitialized()
        return try {
            val keyBytes = MessageDigest.getInstance("SHA-256").digest(secretKey.toByteArray(Charsets.UTF_8))
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val decodedBytes = base64Decode(encryptedText).toByteArray(Charsets.ISO_8859_1)
            String(cipher.doFinal(decodedBytes), Charsets.UTF_8)
        } catch (_: Exception) {
            ""
        }
    }

    /**
     * Recommended symmetric encryption using AES/GCM/NoPadding with a random
     * 12-byte IV and 128-bit auth tag. The IV is prepended to the ciphertext
     * and the whole payload is Base64 encoded. Returns "" on failure.
     */
    @JvmStatic fun aesGcmEncrypt(text: String, secretKey: String): String {
        AppHelper.checkInitialized()
        return try {
            val keyBytes = MessageDigest.getInstance("SHA-256").digest(secretKey.toByteArray(Charsets.UTF_8))
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val iv = ByteArray(GCM_IV_BYTES).also { SecureRandom().nextBytes(it) }
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, GCMParameterSpec(GCM_TAG_BITS, iv))
            val encrypted = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
            encodeBytes(iv + encrypted)
        } catch (_: Exception) {
            ""
        }
    }

    /**
     * Decrypts payloads produced by [aesGcmEncrypt]. Returns "" on failure,
     * including authentication failures when key or data was tampered with.
     */
    @JvmStatic fun aesGcmDecrypt(encryptedText: String, secretKey: String): String {
        AppHelper.checkInitialized()
        return try {
            val combined = decodeBytes(encryptedText) ?: return ""
            if (combined.size <= GCM_IV_BYTES) return ""
            val iv = combined.copyOfRange(0, GCM_IV_BYTES)
            val cipherText = combined.copyOfRange(GCM_IV_BYTES, combined.size)
            val keyBytes = MessageDigest.getInstance("SHA-256").digest(secretKey.toByteArray(Charsets.UTF_8))
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, GCMParameterSpec(GCM_TAG_BITS, iv))
            String(cipher.doFinal(cipherText), Charsets.UTF_8)
        } catch (_: Exception) {
            ""
        }
    }

    @JvmStatic fun base64Encode(text: String): String {
        AppHelper.checkInitialized()
        if (isNativeLoaded) {
            try {
                return nativeBase64Encode(text)
            } catch (_: Throwable) {
                // Fallback
            }
        }
        return try {
            Base64.encodeToString(text.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
        } catch (_: Throwable) {
            java.util.Base64.getEncoder().encodeToString(text.toByteArray(Charsets.UTF_8))
        }
    }

    @JvmStatic fun base64Decode(encodedText: String): String {
        AppHelper.checkInitialized()
        if (isNativeLoaded) {
            try {
                return nativeBase64Decode(encodedText)
            } catch (_: Throwable) {
                // Fallback
            }
        }
        return try {
            String(Base64.decode(encodedText, Base64.NO_WRAP), Charsets.UTF_8)
        } catch (_: Throwable) {
            String(java.util.Base64.getDecoder().decode(encodedText), Charsets.UTF_8)
        }
    }

    @JvmStatic fun base64UrlEncode(text: String): String {
        AppHelper.checkInitialized()
        return try {
            Base64.encodeToString(
                text.toByteArray(Charsets.UTF_8),
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
            )
        } catch (_: Throwable) {
            java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(text.toByteArray(Charsets.UTF_8))
        }
    }

    @JvmStatic fun base64UrlDecode(encodedText: String): String {
        AppHelper.checkInitialized()
        return try {
            String(
                Base64.decode(encodedText, Base64.URL_SAFE or Base64.NO_WRAP),
                Charsets.UTF_8
            )
        } catch (_: Throwable) {
            String(java.util.Base64.getUrlDecoder().decode(encodedText), Charsets.UTF_8)
        }
    }

    private fun hash(text: String, algorithm: String): String {
        return try {
            val digest = MessageDigest.getInstance(algorithm)
            val bytes = digest.digest(text.toByteArray(Charsets.UTF_8))
            bytesToHex(bytes)
        } catch (_: Exception) {
            ""
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    private fun encodeBytes(bytes: ByteArray): String {
        return try {
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (_: Throwable) {
            java.util.Base64.getEncoder().encodeToString(bytes)
        }
    }

    private fun decodeBytes(encodedText: String): ByteArray? {
        return try {
            Base64.decode(encodedText, Base64.NO_WRAP)
        } catch (_: Throwable) {
            try {
                java.util.Base64.getDecoder().decode(encodedText)
            } catch (_: Exception) {
                null
            }
        }
    }
}
