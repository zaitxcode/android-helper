package com.zaitxcode.android.content

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.zaitxcode.android.core.AppHelper
import com.zaitxcode.android.browser.Browser

object Intent {
    // Name intentionally shadows android.content.Intent.
    // In Kotlin prefer an alias to avoid confusion:
    // import com.zaitxcode.android.content.Intent as HelperIntent
    // In Java use the fully qualified name:
    // com.zaitxcode.android.content.Intent.openWhatsApp(...)

    @JvmStatic
    @JvmOverloads
    fun openWhatsApp(phone: String, message: String? = null, context: Context = AppHelper.ctx()) {
        AppHelper.checkInitialized()
        val formatted = phone.filter { it.isDigit() }
        val uri = if (!message.isNullOrEmpty()) {
            Uri.parse("https://api.whatsapp.com/send?phone=$formatted&text=${Uri.encode(message)}")
        } else {
            Uri.parse("https://api.whatsapp.com/send?phone=$formatted")
        }
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        safelyStart(context, intent)
    }

    @JvmStatic
    @JvmOverloads
    fun dial(phone: String, context: Context = AppHelper.ctx()) {
        AppHelper.checkInitialized()
        val uri = Uri.parse("tel:$phone")
        val intent = Intent(Intent.ACTION_DIAL, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        safelyStart(context, intent)
    }

    @JvmStatic
    @JvmOverloads
    fun sendSms(phone: String, message: String = "", context: Context = AppHelper.ctx()) {
        AppHelper.checkInitialized()
        val uri = Uri.parse("smsto:$phone")
        val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
            putExtra("sms_body", message)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        safelyStart(context, intent)
    }

    @JvmStatic
    @JvmOverloads
    fun sendEmail(
        email: String,
        subject: String = "",
        body: String = "",
        context: Context = AppHelper.ctx()
    ) {
        AppHelper.checkInitialized()
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        safelyStart(context, intent)
    }

    @JvmStatic
    @JvmOverloads
    fun shareText(text: String, context: Context = AppHelper.ctx()) {
        AppHelper.checkInitialized()
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        safelyStart(context, shareIntent)
    }

    @JvmStatic
    @JvmOverloads
    fun shareFile(
        uri: Uri,
        mimeType: String,
        chooserTitle: String = "Share via",
        context: Context = AppHelper.ctx()
    ) {
        AppHelper.checkInitialized()
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = mimeType
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val shareIntent = Intent.createChooser(sendIntent, chooserTitle).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        safelyStart(context, shareIntent)
    }

    @JvmStatic
    @JvmOverloads
    fun openMap(
        latitude: Double,
        longitude: Double,
        label: String? = null,
        context: Context = AppHelper.ctx()
    ) {
        AppHelper.checkInitialized()
        val query = if (!label.isNullOrEmpty()) {
            "geo:$latitude,$longitude?q=$latitude,$longitude(${Uri.encode(label)})"
        } else {
            "geo:$latitude,$longitude"
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(query)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        safelyStart(context, intent)
    }

    @JvmStatic
    @JvmOverloads
    fun openAppSettings(context: Context = AppHelper.ctx()) {
        AppHelper.checkInitialized()
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${context.packageName}")
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        safelyStart(context, intent)
    }

    @JvmStatic
    @JvmOverloads
    fun openPlayStore(packageName: String = AppHelper.ctx().packageName, context: Context = AppHelper.ctx()) {
        AppHelper.checkInitialized()
        val marketIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$packageName")
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        if (!safelyStart(context, marketIntent)) {
            Browser.openUrl("https://play.google.com/store/apps/details?id=$packageName", context)
        }
    }

    private fun safelyStart(context: Context, intent: Intent): Boolean {
        return try {
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}
