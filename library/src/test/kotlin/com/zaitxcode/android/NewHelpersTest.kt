package com.zaitxcode.android

import com.zaitxcode.android.core.AppHelper
import com.zaitxcode.android.hardware.DeviceUI
import com.zaitxcode.android.security.CryptoGcm
import com.zaitxcode.android.security.Encryption
import com.zaitxcode.android.security.RustEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NewHelpersTest {

    @Before
    fun setUp() {
        AppHelper.initializeForTesting()
    }

    @Test
    fun testCryptoGcmEncryptDecrypt() {
        val original = "Secret Text 12345"
        val secretKey = "my_gcm_secret_key"

        val encrypted = CryptoGcm.aesGcmEncrypt(original, secretKey)
        assertTrue(encrypted.isNotBlank())
        assertNotEquals(original, encrypted)

        val decrypted = CryptoGcm.aesGcmDecrypt(encrypted, secretKey)
        assertEquals(original, decrypted)
    }

    @Test
    fun testCrc32() {
        val input = "Hello Android Helper 1.1.0-alpha01"
        val crc = Encryption.crc32(input)
        assertTrue(crc != 0L)
    }

    @Test
    fun testRustEngineFallback() {
        val text = "Rust SHA-3 Test"
        val hash = RustEngine.sha3(text)
        assertNotNull(hash)
        assertTrue(hash.isNotBlank())
    }

    @Test
    fun testDeviceUI() {
        assertNotNull(DeviceUI.getOemSkinName())
        assertNotNull(DeviceUI.isHyperOS())
        assertNotNull(DeviceUI.isMiui())
        assertNotNull(DeviceUI.isHyperOSOptimizationEnabled())
        assertNotNull(DeviceUI.getHyperOSBuildCode())
        assertNotNull(DeviceUI.isColorOS())
        assertNotNull(DeviceUI.isOxygenOS())
        assertNotNull(DeviceUI.isRealmeUI())
        assertNotNull(DeviceUI.isOneUI())
        assertNotNull(DeviceUI.isOriginOS())
        assertNotNull(DeviceUI.isFuntouchOS())
        assertNotNull(DeviceUI.isEMUI())
        assertNotNull(DeviceUI.isHarmonyOS())
        assertNotNull(DeviceUI.isMagicOS())
        assertNotNull(DeviceUI.isNothingOS())
        assertNotNull(DeviceUI.isFlyme())
        assertNotNull(DeviceUI.isLineageOS())
        assertNotNull(DeviceUI.isPixelExperience())
        assertNotNull(DeviceUI.isGrapheneOS())
        assertNotNull(DeviceUI.isCalyxOS())
        assertNotNull(DeviceUI.isCrDroid())
        assertNotNull(DeviceUI.isCustomRom())
    }
}
