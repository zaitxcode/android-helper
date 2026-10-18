package com.zaitxcode.android.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network as AndroidNetwork
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.zaitxcode.android.core.AppHelper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object ConnectivityStream {

    @JvmStatic
    @JvmOverloads
    fun observeNetworkStatus(context: Context = AppHelper.ctx()): Flow<Boolean> {
        AppHelper.checkInitialized()
        return callbackFlow {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: AndroidNetwork) {
                    trySend(true)
                }

                override fun onLost(network: AndroidNetwork) {
                    trySend(false)
                }
            }

            if (connectivityManager != null) {
                val request = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()
                connectivityManager.registerNetworkCallback(request, callback)

                trySend(Network.isConnected)
            } else {
                trySend(false)
            }

            awaitClose {
                try {
                    connectivityManager?.unregisterNetworkCallback(callback)
                } catch (_: Exception) {
                    // Guard
                }
            }
        }
    }
}
