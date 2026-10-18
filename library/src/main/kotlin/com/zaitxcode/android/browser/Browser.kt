package com.zaitxcode.android.browser

import android.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.zaitxcode.android.core.AppHelper

object Browser {

    @JvmStatic
    @JvmOverloads
    fun openUrl(url: String, context: Context = AppHelper.ctx()) {
        openCustomTab(url = url, context = context)
    }

    @JvmStatic
    @JvmOverloads
    fun openCustomTab(
        url: String,
        toolbarColor: Int? = null,
        showTitle: Boolean = true,
        context: Context = AppHelper.ctx()
    ): Boolean {
        AppHelper.checkInitialized()
        val safeUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }

        return try {
            val builder = CustomTabsIntent.Builder()
                .setShowTitle(showTitle)
                .setUrlBarHidingEnabled(true)

            val color = toolbarColor ?: ContextCompat.getColor(context, R.color.holo_blue_dark)
            val params = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(color)
                .build()
            builder.setDefaultColorSchemeParams(params)

            val customTabsIntent = builder.build()
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            customTabsIntent.launchUrl(context, Uri.parse(safeUrl))
            true
        } catch (_: Exception) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(safeUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            try {
                context.startActivity(intent)
                true
            } catch (_: Exception) {
                false
            }
        }
    }
}
