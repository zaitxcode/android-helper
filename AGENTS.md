# AGENTS.md

Guidance for AI agents working in this repository. Read this first.

> **MANDATORY INSTRUCTION FOR ALL AI AGENTS**:
> Whenever you modify, add, or remove files, packages, or architectural components in this repository, you **MUST** update the Project Directory Tree and the Change Log sections in this file (`AGENTS.md`) before finishing your task.

## What this project is

**Android Helper** is a multi-module Android library plus demo applications.
The library is the product; everything else exists to demonstrate, exercise,
or publish it.

It provides clean, single-purpose helper objects that wrap repetitive
Android tasks: connectivity checks, vibration and haptics, audio, display info,
biometric prompts, safe intents, clipboard, notifications, file and storage access,
device and battery info, time and validation utilities, keyboard control,
screen-capture blocking, app metadata and signatures, cryptography with C++ NDK & Rust acceleration,
and native WebBridge for WebView integration.

Public API lives under the `com.zaitxcode.android.*` namespace.
The codebase is English-only by policy.

## Module layout

| Module | Role |
|---|---|
| `library` | The actual library. Published to Maven Central (`com.zaitxcode:androidhelper`). |
| `app` | Kotlin demo app (Jetpack Compose & Miuix Material3). Exercises every helper. |
| `java_app` | Pure-Java demo app (Views/XML). Proves Java interop. |
| `flutter_example` | Flutter demo that bridges to the library via a MethodChannel (`androidhelper/core`). |
| `packages/web` | Web & TS helper package (`@zaitxcode/web-helper`) for WebBridge integration. |
| `web_demo` | Standalone interactive HTML/JS WebBridge demo page. |

## Project Directory Tree

```
androidhelper/
├── AGENTS.md                          # Guidance and architecture rules for AI agents
├── PROMPTS.md                         # Reusable prompt templates and agent working agreement
├── README.md                          # Public repository overview and installation guide
├── LICENSE                             # Apache-2.0 License & ZaitXCode identity terms
├── build.gradle.kts                   # Root Gradle build script
├── settings.gradle.kts                # Root project module definitions
├── gradle.properties                  # Gradle properties & JVM configurations
├── jitpack.yml                        # JitPack publication configuration
├── gradle/
│   ├── libs.versions.toml             # Central Version Catalog (single source of truth)
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties  # Gradle wrapper configuration
├── library/                           # Published Android Library Module
│   ├── build.gradle.kts               # Library build configuration, C++ NDK, Rust & Maven publish setup
│   ├── consumer-rules.pro             # Consumer ProGuard/R8 keep rules (packaged in AAR)
│   ├── proguard-rules.pro             # Library ProGuard reference rules
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml    # Library manifest & FileProvider declaration
│       │   ├── cpp/                   # Native C++ NDK Engine
│       │   │   ├── CMakeLists.txt     # CMake build script for libandroidhelper.so
│       │   │   └── androidhelper.cpp  # C++ JNI implementation (SHA-256, Base64, CRC32)
│       │   ├── rust/                  # Native Rust Crate
│       │   │   ├── Cargo.toml         # Rust Crate manifest for libandroidhelper_rust.so
│       │   │   └── src/lib.rs         # Rust JNI implementation (SHA-3, ChaCha20)
│       │   ├── kotlin/com/zaitxcode/android/
│       │   │   ├── app/               # AppInfo, AppState, Notification, Permission, Signature
│       │   │   ├── bridge/            # WebBridge (WebView JavaScript interface)
│       │   │   ├── browser/           # Browser (Custom Tabs)
│       │   │   ├── content/           # Clipboard, Intent (Safe Intents)
│       │   │   ├── core/              # AppHelper (Initialization & Context tracking)
│       │   │   ├── hardware/          # Audio, Battery, Biometric, Device, Display, Sensors, Vibration
│       │   │   ├── io/                # Download, File (Path Traversal Protection), Storage
│       │   │   ├── net/               # ConnectivityStream, Network (Live Connectivity Observer)
│       │   │   ├── security/          # CryptoGcm (AES-GCM), Encryption (AES-256, HMAC, Hashes), RustEngine
│       │   │   ├── util/              # Time, Validation
│       │   │   └── view/              # Keyboard (WindowInsets), Screen, WindowInsetsHelper
│       │   └── res/
│       │       └── xml/
│       │           └── androidhelper_file_paths.xml  # Safe FileProvider path rules
│       └── test/kotlin/com/zaitxcode/android/        # Unit Tests (31 passed tests)
│           ├── AppInfoTest.kt
│           ├── AppStateTest.kt
│           ├── DeviceTest.kt
│           ├── EncryptionTest.kt
│           ├── FileTest.kt
│           ├── HardwareAndContentTest.kt
│           ├── NetworkTest.kt
│           ├── NewHelpersTest.kt
│           ├── TimeTest.kt
│           └── ValidationTest.kt
├── app/                               # Jetpack Compose Demo Application
│   ├── build.gradle.kts
│   └── src/main/kotlin/com/zaitxcode/android/apps/apphelper/
│       ├── MainActivity.kt
│       ├── theme/                     # AppTheme, GlassModifier, ThemeManager
│       └── ui/                        # HomeScreen (Exercises all helpers), SettingsScreen
├── java_app/                          # Pure Java & Views Demo Application
│   ├── build.gradle.kts
│   └── src/main/java/com/zaitxcode/android/apps/javaapphelper/
│       └── MainActivity.java          # Full Java Interop Demonstration
├── flutter_example/                   # Flutter MethodChannel Demo Application
│   ├── pubspec.yaml
│   ├── lib/main.dart                  # Flutter UI communicating via MethodChannel
│   └── android/                       # Native Android host module
│       └── app/src/main/kotlin/com/zaitxcode/android/apps/flutterapphelper/
│           ├── MainActivity.kt        # MethodChannel handler ("androidhelper/core")
│           └── MyApp.kt               # Application class initializing AppHelper
├── packages/web/                      # Web & TS Helper Package (@zaitxcode/web-helper)
│   ├── package.json
│   └── src/
└── web_demo/                          # Interactive Standalone HTML/JS WebBridge Demo
    └── index.html
```

## Recent Changes & History Log

The following changes have been implemented and verified across the project:

- **Release `1.1.0-alpha01`**: Published to Maven Central (`com.zaitxcode:androidhelper:1.1.0-alpha01`) and tagged on GitHub.
- **Kotlin 2.5.0-Beta1 & Java 21**: Upgraded to Kotlin `2.5.0-Beta1` and Java 21 toolchain across all Gradle modules.
- **Native Rust Engine (`libandroidhelper_rust.so`)**: Added Rust crate in `library/src/main/rust/` with `security.RustEngine` for Rust SHA-3 hashing and ChaCha20 encryption.
- **Native C++ NDK Engine Extensions**: Expanded `androidhelper.cpp` to include native CRC32 hashing.
- **AES-GCM Cryptography (`security.CryptoGcm`)**: Added AES/GCM/NoPadding encryption with 12-byte random IVs and authentication tags.
- **Hardware Sensors Manager (`hardware.Sensors`)**: Added helpers for accelerometer, gyroscope, barometer, step counter, and ambient light lux meter.
- **Download Manager (`io.Download`)**: Added system DownloadManager enqueueing, status polling, and cancellation.
- **WindowInsets & Edge-to-Edge (`view.WindowInsetsHelper`)**: Added Edge-to-Edge UI enablement and status/navigation bar height DP helpers.
- **Coroutines Flow Connectivity (`net.ConnectivityStream`)**: Added reactive `Flow<Boolean>` for real-time network state observation in Compose & ViewModel.
- **WebView Two-Way Bridge (`bridge.WebBridge`)**: Added `evaluateJs` and `detach` methods for two-way JavaScript communication.
- **Unit Test Suite**: Expanded unit tests to 31 passing tests (`31/31 Passed`).
- **Cloudflare Pages & R2 Storage**: Deployed updated docs site to Cloudflare Pages (`docs.zaitxcode.com/androidhelper/`) and uploaded release APK to Cloudflare R2 Remote Bucket `zaitxcode-storage`.

## Library architecture

The library is organized by **category**, one package per category, one
`object` per helper:

- `core` — `AppHelper`: initialization, lifecycle tracking, logging entry point
- `net` — `Network`, `ConnectivityStream`: live connectivity, active transport, Coroutines Flow
- `hardware` — `Audio`, `Battery`, `Biometric`, `Device`, `Display`, `Sensors`, `Vibration`
- `content` — `Clipboard`, `Intent`: safe intent routing (WhatsApp, SMS, Dial, Email, Share, Maps)
- `app` — `AppInfo`, `AppState`, `Notification`, `Permission`, `Signature` (Anti-Tampering)
- `io` — `Download`, `File` (Path Traversal Protection), `Storage`
- `security` — `CryptoGcm` (AES-GCM), `Encryption` (AES-256, HMAC, Hashes, CRC32, C++ NDK), `RustEngine` (Rust SHA-3)
- `util` — `Time`, `Validation` (Email, Phone, Egyptian ID, Luhn Credit Card, JSON, Hex Color)
- `view` — `Keyboard` (WindowInsets), `Screen` (Capture Blocking), `WindowInsetsHelper` (Edge-to-Edge)
- `browser` — `Browser`: Custom Tabs
- `bridge` — `WebBridge`: WebView JavaScript interface (`window.AndroidHelper`) with two-way `evaluateJs`

### Conventions every helper follows

These are deliberate and must be preserved:

1. **Singleton `object`** — helpers are stateless entry points, not instances.
2. **`@JvmStatic` + `@JvmOverloads`** — so Java callers get clean static-style
   access and sensible default arguments.
3. **Optional `context` parameter defaulting to `AppHelper.ctx()`** — callers
   can omit the context after `AppHelper.initialize(...)` has run.
4. **Fail safe, never throw** — helpers catch internal exceptions and return a
   safe fallback (`false`, `null`, `""`, empty list). Callers should never have
   to wrap helper calls in try/catch.

### Initialization contract

`AppHelper.initialize(context)` must be called once, typically from the app's
`Application.onCreate`. It stores the application context, registers activity
lifecycle callbacks to track the current foreground `Activity`, and initializes
`Network`. Helpers that need a context rely on this having happened.
`AppHelper.getContext()` and `AppHelper.getActivity()` are the public accessors
for Java consumers; the internal `ctx()`/`act()` are library-private.

## Build and tooling

- Gradle with Kotlin DSL everywhere. Version catalog at `gradle/libs.versions.toml`
  is the single source of truth for versions and SDK levels.
- Java/Kotlin target is **Java 21**. The minimum supported Android SDK is 26.
- Always invoke the wrapper as `bash ./gradlew <task>` — the execute bit is
  unreliable in this environment.
- The library publishes to Maven Central (`com.zaitxcode:androidhelper`).

### Environment quirk (important)

This host is **aarch64**, but the Android Gradle Plugin ships `aapt2` for
**x86_64** only, which fails here. The workaround is a machine-local override
pointing at the SDK's native ARM64 `aapt2`, configured **outside** the project
(via `android.aapt2FromMavenOverride` in the user-level Gradle properties).
Do not add this override to the project's own files — it is host-specific and
would break x86_64 users.

## No obfuscation — consumer safety

**Obfuscation is disabled for the library and for every demo module**
(`isMinifyEnabled = false`). The published AAR is not shrunk and its names are
not renamed, so consumers can call every helper safely out of the box with no
extra configuration. Do not re-enable minification for the library.

The rule files are kept as documentation and as a safety net:

- `library/proguard-rules.pro` describes the public surface. It is not applied
  while building the library (minification is off), but it is ready if that ever
  changes.
- `library/consumer-rules.pro` is packaged *inside* the AAR and is applied
  automatically to every app that depends on the library. It guarantees that a
  consumer who enables their own minification never loses native JNI bindings.

## Non-obvious facts and pitfalls

- **Do not commit build output or caches.** `build/`, `.gradle/`, `.kotlin/`,
  `.wrangler/`, and Flutter's `.dart_tool/` are generated. Keep the tree clean.
- **`proguard-rules.pro` must never be gitignored.** Ignoring it silently
  removes the consumer-protection layer from the repository.
- The `flutter_example` module depends on a **prebuilt `androidhelper.aar`
  committed into the repo** (`android/app/libs/`). It is a deliberate
  dependency artifact, not a cache — do not delete it. Rebuild and refresh it
  when the library's public API changes.
- The library ships a `FileProvider` (authority `"${applicationId}.provider"`)
  and its path config; file-sharing helpers depend on it.
- `Encryption` uses AES-256 and HMAC-SHA256 with C++ NDK acceleration.
- `RustEngine` provides Rust-native cryptographic operations.
- The codebase is **English-only**: all identifiers, comments, strings, and
  docs. Keep it that way.

## Working agreements

- **MANDATORY**: Keep the Project Directory Tree and Change Log in `AGENTS.md` up to date whenever files or architecture changes occur.
- Keep the category/package structure and the singleton + `@JvmStatic` style.
- Preserve Java interop: any new public helper must be callable cleanly from
  plain Java.
- Any new public API must be reflected in the ProGuard keep rules so a
  consumer who enables minification cannot strip JNI bindings.
- Keep modules self-consistent: a change to the library's public API may
  require updating `app`, `java_app`, `flutter_example`, and the bundled AAR.
- Prefer editing existing files over adding scratch or staging files.
- Reusable prompt templates and the working agreement for AI agents live in
  `PROMPTS.md`. Read it when starting a new task.
