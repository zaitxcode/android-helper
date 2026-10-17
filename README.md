[![Maven Central](https://img.shields.io/maven-central/v/com.zaitxcode/androidhelper.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/com.zaitxcode/androidhelper)
![AndroidX](https://img.shields.io/badge/AndroidX-Required-blue)
![Kotlin](https://img.shields.io/badge/Kotlin-First-purple)
![C++ Engine](https://img.shields.io/badge/C++%20NDK-libandroidhelper.so-blueviolet)
![WebBridge](https://img.shields.io/badge/Web-WebBridge-success)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Release](https://img.shields.io/badge/Release-orange)

# Android Helper

> Enterprise-grade, clean Android & Flutter utility library under `com.zaitxcode.android.*` backed by a high-performance C++ Native Engine (`libandroidhelper.so`) and native WebBridge.

Android Helper provides clean Android category helpers — Network, Audio, Vibration, Display, Biometrics, Secure Intents, Clipboard, Notifications, File Management, Device/Battery info, Cryptography, and WebBridge.

🌐 **Documentation:** [docs.zaitxcode.com/androidhelper](https://docs.zaitxcode.com/androidhelper)  
🌐 **Live GitHub Pages Web Demo:** [zaitxcode.github.io/androidhelper](https://zaitxcode.github.io/androidhelper/)

---

## 📦 Installation & Setup

### 1. Add Dependency (libs.versions.toml)

```toml
[versions]
androidHelper = "1.0.0"

[libraries]
androidhelper = { group = "com.zaitxcode", name = "androidhelper", version.ref = "androidHelper" }
```

### 2. Add Repository (settings.gradle.kts)

```kotlin
repositories {
    google()
    mavenCentral()
}
```

### 3. Add Dependency (app/build.gradle.kts)

```kotlin
dependencies {
    implementation(libs.androidhelper)
}
```

### 4. Initialize in Application Class

```kotlin
import android.app.Application
import com.zaitxcode.android.core.AppHelper

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppHelper.initialize(this)
    }
}
```

---

## 🌐 WebBridge - Web to Android Helper Bridge

Connect any Android `WebView` to native `Android Helper` Kotlin & C++ APIs with a single line of code:

```kotlin
import com.zaitxcode.android.bridge.WebBridge

// Attach bridge to any WebView (exposes window.AndroidHelper to JavaScript)
WebBridge.attach(webView)
```

### JavaScript Usage in Web App:
```javascript
if (window.AndroidHelper) {
    const isOnline = window.AndroidHelper.isConnected();
    const battery = window.AndroidHelper.getBatteryLevel();
    const sha256Hash = window.AndroidHelper.sha256("Hello WebBridge");
    window.AndroidHelper.vibrate(200);
}
```

---

## ⚡ High-Performance C++ Native Engine (`androidhelper.so`)

Android Helper includes a native C++ JNI library (`libandroidhelper.so`) compiled via CMake for high-performance cryptographic operations (SHA-256, Base64, and security utils). If native binaries are unavailable on a target architecture, it automatically falls back to pure Kotlin execution without raising exceptions.

---

## 🚀 Category Usage Examples

```kotlin
import com.zaitxcode.android.net.Network
import com.zaitxcode.android.hardware.Audio
import com.zaitxcode.android.hardware.Vibration
import com.zaitxcode.android.content.Clipboard
import com.zaitxcode.android.hardware.Device
import com.zaitxcode.android.hardware.Battery
import com.zaitxcode.android.security.Encryption
import com.zaitxcode.android.bridge.WebBridge

// Network checks
val isOnline = Network.isConnected
val transport = Network.activeTransport()

// Audio & Haptics
Audio.playClickSound()
Vibration.vibrate(200)

// Cryptography & Native C++ Engine
val hash = Encryption.sha256("AndroidHelper")
val isNativeActive = Encryption.isNativeEngineAvailable()

// Attach WebBridge to WebView
WebBridge.attach(webView)

// Clipboard
Clipboard.copyText("Hello Android Helper")

// Device & Battery Info
val device = Device.deviceName()
val battery = Battery.getBatteryLevel()
```

---

## 🔓 No Obfuscation — Safe to Call

The published AAR is **not obfuscated**, so every helper can be called directly
and safely out of the box. No extra configuration is required in a consumer
project.

For projects that enable their own R8/minification, the AAR also ships
`consumer-rules.pro`, which is applied automatically and guarantees the public
API is never stripped or renamed.

---

## 📄 License

**Android Helper is open source.**

You are free to use it in any project, commercial or non-commercial, either by
declaring it as a dependency in your Gradle build file, or by downloading the
source code and modifying it to fit your own needs.

In return, the **ZaitXCode identity must be preserved**, and the namespace line

```
com.zaitxcode.*
```

must be kept intact in every file that belongs to the library. Any violation of
these terms will be subject to legal action.

See the [LICENSE](LICENSE) file for full terms.

Copyright (c) 2026–2027 ZaitXCode. All rights reserved.
