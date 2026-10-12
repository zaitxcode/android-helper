package com.zaitxcode.android

import com.zaitxcode.android.core.AppHelper
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AppInfoTest {

    @Before
    fun setUp() {
        AppHelper.initializeForTesting()
    }

    @Test
    fun testAppHelperState() {
        assertTrue(AppHelper.isInitialized())
    }
}
