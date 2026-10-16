# Android Helper Flutter Integration Example

This example demonstrates how to integrate the **Android Helper** library (v1.0.0-RC01) into a Flutter application using a native Kotlin `MethodChannel` bridge.

---

## 🚀 Setup Instructions

### 1. Android Application Class (`MyApp.kt`)
In `android/app/src/main/kotlin/com/zaitxcode/android/apps/flutterapphelper/MyApp.kt`:

```kotlin
package com.zaitxcode.android.apps.flutterapphelper

import android.app.Application
import com.zaitxcode.android.core.AppHelper

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppHelper.initialize(this)
    }
}
```

### 2. MethodChannel Handler (`MainActivity.kt`)
In `android/app/src/main/kotlin/com/zaitxcode/android/apps/flutterapphelper/MainActivity.kt`:

```kotlin
package com.zaitxcode.android.apps.flutterapphelper

import com.zaitxcode.android.core.AppHelper
import com.zaitxcode.android.net.Network
import com.zaitxcode.android.content.Clipboard
import com.zaitxcode.android.hardware.Vibration
import com.zaitxcode.android.hardware.Audio
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterFragmentActivity() {
    private val channel = "androidhelper/core"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channel).setMethodCallHandler { call, result ->
            when (call.method) {
                "isConnected" -> result.success(Network.isConnected)
                "vibrate" -> {
                    Vibration.vibrate(call.argument<Int>("ms")?.toLong() ?: 200)
                    result.success(null)
                }
                "copyText" -> {
                    Clipboard.copyText(call.argument<String>("text") ?: "")
                    result.success(null)
                }
                "audioInfo" -> {
                    Audio.playClickSound()
                    result.success("Muted=${Audio.isMuted()} MusicVolume=${Audio.getMusicVolume()}%")
                }
                else -> result.notImplemented()
            }
        }
    }
}
```

### 3. Flutter Dart Invocation
In `lib/main.dart`:

```dart
import 'package:flutter/services.dart';

const _channel = MethodChannel('androidhelper/core');

// Check connectivity
final isOnline = await _channel.invokeMethod<bool>('isConnected');

// Copy text & play tactile sound
await _channel.invokeMethod('copyText', {'text': 'Hello Android Helper'});
await _channel.invokeMethod('vibrate', {'ms': 200});
```

---

## 📄 License
Copyright (c) 2026–2027 ZaitXCode. All rights reserved.
