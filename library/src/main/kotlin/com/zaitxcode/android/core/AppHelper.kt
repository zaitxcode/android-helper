package com.zaitxcode.android.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.zaitxcode.android.net.Network

object AppHelper {

    private var applicationContext: Context? = null
    private var currentActivity: Activity? = null
    private var isLifecycleRegistered = false
    private var initialized = false

    @JvmStatic
    fun initialize(context: Context) {
        val appContext = context.applicationContext ?: context
        applicationContext = appContext
        initialized = true

        if (context is Activity) {
            currentActivity = context
        }

        val app = appContext as? Application
        if (app != null && !isLifecycleRegistered) {
            registerLifecycle(app)
            isLifecycleRegistered = true
        }

        Network.initialize(appContext)
    }

    @JvmStatic
    fun initialize(activity: Activity) {
        currentActivity = activity
        initialize(activity as Context)
    }

    @JvmStatic
    fun isInitialized(): Boolean = initialized || applicationContext != null || currentActivity != null

    @JvmStatic
    @JvmOverloads
    fun initializeForTesting(context: Context? = null) {
        initialized = true
        if (context != null) {
            applicationContext = context
        }
    }

    @JvmStatic
    fun checkInitialized() {
        if (!isInitialized()) {
            throw IllegalStateException(
                "AppHelper must be initialized before calling Android Helper functions: " +
                "call AppHelper.initialize(context) in Application.onCreate()"
            )
        }
    }

    /**
     * Returns the application context.
     * Public entry point so Kotlin and Java consumers can obtain the context
     * without relying on internal-only accessors.
     *
     * @throws IllegalStateException when [initialize] has not been called yet.
     */
    @JvmStatic
    fun getContext(): Context = ctx()

    /**
     * Returns the current foreground Activity, or null when none is available.
     */
    @JvmStatic
    fun getActivity(): Activity? = act()

    internal fun ctx(): Context {
        checkInitialized()
        return applicationContext
            ?: currentActivity?.applicationContext
            ?: throw IllegalStateException("AppHelper must be initialized: AppHelper.initialize(context)")
    }

    internal fun act(): Activity? {
        checkInitialized()
        return currentActivity
    }

    private fun registerLifecycle(app: Application) {
        app.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(a: Activity, b: Bundle?) {
                currentActivity = a
            }

            override fun onActivityStarted(a: Activity) {
                currentActivity = a
            }

            override fun onActivityResumed(a: Activity) {
                currentActivity = a
            }

            override fun onActivityPaused(a: Activity) {}
            override fun onActivityStopped(a: Activity) {}
            override fun onActivitySaveInstanceState(a: Activity, b: Bundle) {}

            override fun onActivityDestroyed(a: Activity) {
                if (currentActivity == a) {
                    currentActivity = null
                }
            }
        })
    }

    @JvmStatic
    fun log(tag: String, message: String) {
        checkInitialized()
        Log.d(tag, message)
    }

    @JvmStatic
    fun logWarning(tag: String, message: String) {
        checkInitialized()
        Log.w(tag, message)
    }

    @JvmStatic
    @JvmOverloads
    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        checkInitialized()
        if (throwable != null) {
            Log.e(tag, message, throwable)
            showLogDialog("Error", tag, "$message\n\n${throwable.localizedMessage}")
        } else {
            Log.e(tag, message)
            showLogDialog("Error", tag, message)
        }
    }

    private fun showLogDialog(type: String, tag: String, message: String) {
        val activity = act() ?: return
        activity.runOnUiThread {
            AlertDialog.Builder(activity)
                .setTitle("$type : $tag")
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", null)
                .show()
        }
    }
}
