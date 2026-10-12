package com.zaitxcode.android

import com.zaitxcode.android.core.AppHelper
import com.zaitxcode.android.hardware.Device
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeviceTest {

    @Before
    fun setUp() {
        AppHelper.initializeForTesting()
    }

    @Test
    fun testDeviceBuildInfo() {
        assertNotNull(Device.manufacturer())
        assertNotNull(Device.model())
        assertNotNull(Device.brand())
        assertNotNull(Device.androidVersion())
        assertNotNull(Device.supportedAbis())
        assertNotNull(Device.deviceName())
        assertTrue(Device.getUptimeMillis() >= 0)
    }
}
