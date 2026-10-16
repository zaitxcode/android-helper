package com.zaitxcode.android.io

import android.content.Context
import com.zaitxcode.android.core.AppHelper
import java.io.File

object File {
    // Name intentionally shadows java.io.File. This helper operates only
    // inside the app filesDir with path-traversal protection.
    // In Kotlin prefer an alias to avoid confusion:
    // import com.zaitxcode.android.io.File as HelperFile
    // In Java use the fully qualified name:
    // com.zaitxcode.android.io.File.writeText(...)

    @JvmStatic @JvmOverloads fun writeText(fileName: String, text: String, context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return try {
            getFile(context, fileName).writeText(text)
            true
        } catch (_: Exception) {
            false
        }
    }

    @JvmStatic @JvmOverloads fun appendText(fileName: String, text: String, context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return try {
            getFile(context, fileName).appendText(text)
            true
        } catch (_: Exception) {
            false
        }
    }

    @JvmStatic @JvmOverloads fun readText(fileName: String, context: Context = AppHelper.ctx()): String? {
        AppHelper.checkInitialized()
        return try {
            val file = getFile(context, fileName)
            if (!file.exists()) return null
            file.readText()
        } catch (_: Exception) {
            null
        }
    }

    @JvmStatic @JvmOverloads fun readLines(fileName: String, context: Context = AppHelper.ctx()): List<String>? {
        AppHelper.checkInitialized()
        return try {
            val file = getFile(context, fileName)
            if (!file.exists()) return null
            file.readLines()
        } catch (_: Exception) {
            null
        }
    }

    @JvmStatic @JvmOverloads fun writeBytes(fileName: String, bytes: ByteArray, context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return try {
            getFile(context, fileName).writeBytes(bytes)
            true
        } catch (_: Exception) {
            false
        }
    }

    @JvmStatic @JvmOverloads fun readBytes(fileName: String, context: Context = AppHelper.ctx()): ByteArray? {
        AppHelper.checkInitialized()
        return try {
            val file = getFile(context, fileName)
            if (!file.exists()) return null
            file.readBytes()
        } catch (_: Exception) {
            null
        }
    }

    @JvmStatic @JvmOverloads fun delete(fileName: String, context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return try {
            val file = getFile(context, fileName)
            if (file.exists()) file.delete() else true
        } catch (_: Exception) {
            false
        }
    }

    @JvmStatic @JvmOverloads fun exists(fileName: String, context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return getFile(context, fileName).exists()
    }

    @JvmStatic @JvmOverloads fun size(fileName: String, context: Context = AppHelper.ctx()): Long {
        AppHelper.checkInitialized()
        val file = getFile(context, fileName)
        return if (file.exists()) file.length() else 0L
    }

    @JvmStatic fun getExtension(fileName: String): String {
        AppHelper.checkInitialized()
        val lastDot = fileName.lastIndexOf('.')
        return if (lastDot != -1 && lastDot < fileName.length - 1) {
            fileName.substring(lastDot + 1).lowercase()
        } else ""
    }

    @JvmStatic fun getMimeType(fileName: String): String {
        AppHelper.checkInitialized()
        return when (getExtension(fileName)) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "pdf" -> "application/pdf"
            "txt" -> "text/plain"
            "json" -> "application/json"
            else -> "*/*"
        }
    }

    @JvmStatic @JvmOverloads fun list(context: Context = AppHelper.ctx()): List<String> {
        AppHelper.checkInitialized()
        return context.filesDir.list()?.toList() ?: emptyList()
    }

    @JvmStatic @JvmOverloads fun clear(context: Context = AppHelper.ctx()): Boolean {
        AppHelper.checkInitialized()
        return try {
            context.filesDir.listFiles()?.forEach { it.delete() }
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun getFile(context: Context, fileName: String): File {
        val file = File(context.filesDir, fileName)
        val canonicalPath = file.canonicalPath
        val canonicalDir = context.filesDir.canonicalPath
        if (!canonicalPath.startsWith(canonicalDir)) {
            throw SecurityException("Path traversal attempt detected: $fileName")
        }
        return file
    }
}
