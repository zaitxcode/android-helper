package com.zaitxcode.android.security

import android.util.Base64
import com.zaitxcode.android.core.AppHelper
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoGcm {

    private const val GCM_TAG_LENGTH = 128
    private const val GCM_IV_LENGTH = 12

    @JvmStatic
    fun aesGcmEncrypt(plainText: String, secretKey: String): String {
        AppHelper.checkInitialized()
        return try {
            val keyBytes = MessageDigest.getInstance("SHA-256").digest(secretKey.toByteArray(Charsets.UTF_8))
            val keySpec = SecretKeySpec(keyBytes, "AES")

            val iv = ByteArray(GCM_IV_LENGTH)
            SecureRandom().nextBytes(iv)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec)

            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

            val combined = ByteArray(iv.size + encryptedBytes.size)
            System.arraycopy(iv, 0, combined, 0, iv.size)
            System.arraycopy(encryptedBytes, 0, combined, iv.size, encryptedBytes.size)

            try {
                Base64.encodeToString(combined, Base64.NO_WRAP)
            } catch (_: Throwable) {
                java.util.Base64.getEncoder().encodeToString(combined)
            }
        } catch (_: Exception) {
            ""
        }
    }

    @JvmStatic
    fun aesGcmDecrypt(encryptedBase64: String, secretKey: String): String {
        AppHelper.checkInitialized()
        return try {
            val combined = try {
                Base64.decode(encryptedBase64, Base64.NO_WRAP)
            } catch (_: Throwable) {
                java.util.Base64.getDecoder().decode(encryptedBase64)
            }

            if (combined.size <= GCM_IV_LENGTH) return ""

            val iv = ByteArray(GCM_IV_LENGTH)
            System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH)

            val encryptedSize = combined.size - GCM_IV_LENGTH
            val encryptedBytes = ByteArray(encryptedSize)
            System.arraycopy(combined, GCM_IV_LENGTH, encryptedBytes, 0, encryptedSize)

            val keyBytes = MessageDigest.getInstance("SHA-256").digest(secretKey.toByteArray(Charsets.UTF_8))
            val keySpec = SecretKeySpec(keyBytes, "AES")

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec)

            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (_: Exception) {
            ""
        }
    }
}
