# Android Helper Expo & React Native Integration Example

This example demonstrates how to integrate the **Android Helper** library (v1.1.0-alpha01) into a **React Native / Expo** application using a native Kotlin `ReactContextBaseJavaModule` bridge (`AndroidHelperModule.kt`).

---

## 🚀 Setup Instructions for Expo & React Native

### 1. Initialize Android Helper in Application Class (`MainApplication.kt`)
In `android/app/src/main/kotlin/com/zaitxcode/android/apps/expoapphelper/MainApplication.kt`:

```kotlin
import android.app.Application
import com.zaitxcode.android.core.AppHelper

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Android Helper once at app launch
        AppHelper.initialize(this)
    }
}
```

---

### 2. Register AndroidHelperPackage in React Native
In `AndroidHelperPackage.kt`:

```kotlin
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext

class AndroidHelperPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(AndroidHelperModule(reactContext))
    }
}
```

---

### 3. Call Android Helper natively from React Native / Expo (`App.tsx`)

```typescript
import { NativeModules, Platform } from 'react-native';

const { AndroidHelper } = NativeModules;

// Check connectivity
const isOnline = await AndroidHelper.isConnected();

// Query battery level
const batteryLevel = await AndroidHelper.getBatteryLevel();

// Trigger haptic vibration
AndroidHelper.vibrate(200);

// Cryptography & C++ NDK / Rust Acceleration
const sha256Val = await AndroidHelper.sha256('Hello Expo');
const sha3Val = await AndroidHelper.sha3('Hello Expo');
```

---

## 📱 Running the Expo Example

```bash
cd expo_example
npm install
npx expo run:android
```
