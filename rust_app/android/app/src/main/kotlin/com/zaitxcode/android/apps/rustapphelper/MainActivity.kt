package com.zaitxcode.android.apps.rustapphelper

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zaitxcode.android.security.RustEngine

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 60, 40, 40)
            setBackgroundColor(0xFF0F172A.toInt())
        }

        val title = TextView(this).apply {
            text = "🦀 Rust Native Engine Android Demo"
            textSize = 22f
            setTextColor(0xFF38BDF8.toInt())
            setPadding(0, 0, 0, 20)
        }
        layout.addView(title)

        val rustStatus = TextView(this).apply {
            val isAvail = RustEngine.isAvailable()
            text = if (isAvail) {
                "Rust Engine Status: Active ⚡ (libandroidhelper_rust.so)"
            } else {
                "Rust Engine Status: Fallback Mode (Kotlin Core)"
            }
            textSize = 16f
            setTextColor(if (isAvail) 0xFF4ADE80.toInt() else 0xFFF87171.toInt())
            setPadding(0, 0, 0, 30)
        }
        layout.addView(rustStatus)

        val sampleText = "Hello Android & Rust Engine 1.1.0"
        val sha3Result = RustEngine.sha3(sampleText)

        val output = TextView(this).apply {
            text = "Input: $sampleText\n\nRust SHA3-256 Output:\n$sha3Result"
            textSize = 15f
            setTextColor(0xFFCBD5E1.toInt())
        }
        layout.addView(output)

        setContentView(layout)
    }
}
