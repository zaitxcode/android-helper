package com.zaitxcode.android.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.core.view.WindowCompat
import com.zaitxcode.android.core.AppHelper

object WindowInsetsHelper {

    @JvmStatic
    fun enableEdgeToEdge(activity: Activity) {
        AppHelper.checkInitialized()
        try {
            WindowCompat.setDecorFitsSystemWindows(activity.window, false)
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window.navigationBarColor = Color.TRANSPARENT
        } catch (_: Exception) {
            // Guard
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getStatusBarHeightDp(context: Context = AppHelper.ctx()): Int {
        AppHelper.checkInitialized()
        val activity = AppHelper.act()
        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insets = activity.windowManager.currentWindowMetrics.windowInsets
            val heightPx = insets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars()).top
            val density = context.resources.displayMetrics.density
            return if (density > 0) (heightPx / density).toInt() else 0
        }
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            val px = context.resources.getDimensionPixelSize(resourceId)
            val density = context.resources.displayMetrics.density
            if (density > 0) (px / density).toInt() else 0
        } else {
            0
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getNavigationBarHeightDp(context: Context = AppHelper.ctx()): Int {
        AppHelper.checkInitialized()
        val activity = AppHelper.act()
        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insets = activity.windowManager.currentWindowMetrics.windowInsets
            val heightPx = insets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars()).bottom
            val density = context.resources.displayMetrics.density
            return if (density > 0) (heightPx / density).toInt() else 0
        }
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            val px = context.resources.getDimensionPixelSize(resourceId)
            val density = context.resources.displayMetrics.density
            if (density > 0) (px / density).toInt() else 0
        } else {
            0
        }
    }
}
