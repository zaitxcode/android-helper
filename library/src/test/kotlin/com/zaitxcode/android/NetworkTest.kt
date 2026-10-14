package com.zaitxcode.android

import com.zaitxcode.android.core.AppHelper
import com.zaitxcode.android.net.Network
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class NetworkTest {

    @Before
    fun setUp() {
        AppHelper.initializeForTesting()
    }

    @Test
    fun testNetworkMethods() {
        assertNotNull(Network.activeTransport())
        assertNotNull(Network.isWifiConnected())
        assertNotNull(Network.isCellularConnected())
        assertNotNull(Network.isEthernetConnected())
        assertNotNull(Network.isVpnConnected())
        assertNotNull(Network.isConnectionMetered())
    }
}
