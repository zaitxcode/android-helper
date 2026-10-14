package com.zaitxcode.android

import com.zaitxcode.android.core.AppHelper
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AppStateTest {

    @Before
    fun setUp() {
        AppHelper.initializeForTesting()
    }

    @Test
    fun testInitialization() {
        assertTrue(AppHelper.isInitialized())
    }
}
