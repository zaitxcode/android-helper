package com.zaitxcode.android.io

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.zaitxcode.android.core.AppHelper

object Download {

    @JvmStatic
    @JvmOverloads
    fun enqueue(
        url: String,
        fileName: String,
        title: String = fileName,
        description: String = "Downloading file...",
        mimeType: String? = null,
        destinationDir: String = Environment.DIRECTORY_DOWNLOADS,
        context: Context = AppHelper.ctx()
    ): Long {
        AppHelper.checkInitialized()
        return try {
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager ?: return -1L
            val request = DownloadManager.Request(Uri.parse(url)).apply {
                setTitle(title)
                setDescription(description)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(destinationDir, fileName)
                if (!mimeType.isNullOrBlank()) {
                    setMimeType(mimeType)
                }
            }
            manager.enqueue(request)
        } catch (_: Exception) {
            -1L
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getStatus(downloadId: Long, context: Context = AppHelper.ctx()): String {
        AppHelper.checkInitialized()
        if (downloadId <= 0) return "INVALID"
        return try {
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager ?: return "UNKNOWN"
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor = manager.query(query)
            if (cursor != null && cursor.moveToFirst()) {
                val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = if (statusIndex >= 0) cursor.getInt(statusIndex) else -1
                cursor.close()
                when (status) {
                    DownloadManager.STATUS_PENDING -> "PENDING"
                    DownloadManager.STATUS_RUNNING -> "RUNNING"
                    DownloadManager.STATUS_PAUSED -> "PAUSED"
                    DownloadManager.STATUS_SUCCESSFUL -> "SUCCESSFUL"
                    DownloadManager.STATUS_FAILED -> "FAILED"
                    else -> "UNKNOWN"
                }
            } else {
                cursor?.close()
                "NOT_FOUND"
            }
        } catch (_: Exception) {
            "UNKNOWN"
        }
    }

    @JvmStatic
    @JvmOverloads
    fun cancel(downloadId: Long, context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        if (downloadId <= 0) return false
        return try {
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager ?: return false
            manager.remove(downloadId) > 0
        } catch (_: Exception) {
            false
        }
    }
}
