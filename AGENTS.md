# AGENTS.md

Guidance for AI agents working in this repository. Read this first.

## What this project is

**Android Helper** is a multi-module Android library plus demo applications.
The library is the product; everything else exists to demonstrate, exercise,
or publish it.

It provides clean, single-purpose helper objects that wrap the repetitive
Android tasks app developers write over and over: connectivity checks,
vibration and haptics, audio, display info, biometric prompts, safe intents,
clipboard, notifications, file and storage access, device and battery info,
time and validation utilities, keyboard control, screen-capture blocking,
app metadata and signatures, and cryptography.

Public API lives under the `com.zaitxcode.android.*` namespace.
The codebase is English-only by policy.

## Module layout

| Module | Role |
|---|---|
| `library` | The actual library. This is what gets published. |
| `app` | Kotlin demo app (Jetpack Compose). Exercises every helper. |
| `java_app` | Pure-Java demo app (Views/XML). Proves Java interop. |
| `flutter_example` | Flutter demo that bridges to the library via a MethodChannel. |

## Library architecture

The library is organized by **category**, one package per category, one
`object` per helper:

- `core` — `AppHelper`: initialization, lifecycle tracking, logging entry point
- `net` — `Network`
- `hardware` — `Audio`, `Battery`, `Biometric`, `Device`, `Display`, `Vibration`
- `content` — `Clipboard`, `Intent`
- `app` — `AppInfo`, `AppState`, `Notification`, `Permission`, `Signature`
- `io` — `File`, `Storage`
- `security` — `Encryption`
- `util` — `Time`, `Validation`
- `view` — `Keyboard`, `Screen`
- `browser` — `Browser`

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
- Java/Kotlin target is 17. The minimum supported Android SDK is 26.
- Always invoke the wrapper as `bash ./gradlew <task>` — the execute bit is
  unreliable in this environment.
- The library publishes to JitPack. Only the `library` module is published.

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
  consumer who enables their own minification never has the API stripped or
  renamed.

Keep the intent of both files: **keep class names and public members of
`com.zaitxcode.android.**`; keep the Kotlin `INSTANCE` field of singletons; keep
`kotlin.Metadata`; keep `androidx.core.content.FileProvider`.**

## Non-obvious facts and pitfalls

- **Do not commit build output or caches.** `build/`, `.gradle/`, `.kotlin/`,
  and Flutter's `.dart_tool/` are generated. Keep the tree clean.
- **`proguard-rules.pro` must never be gitignored.** Ignoring it silently
  removes the consumer-protection layer from the repository.
- The `flutter_example` module depends on a **prebuilt `androidhelper.aar`
  committed into the repo** (`android/app/libs/`). It is a deliberate
  dependency artifact, not a cache — do not delete it. Rebuild and refresh it
  when the library's public API changes with
  `bash ./gradlew :library:refreshFlutterAar`.
- The library ships a `FileProvider` (authority `"${applicationId}.provider"`)
  and its path config; file-sharing helpers depend on it.
- `Encryption` keeps AES-ECB only for backward compatibility. For new sensitive
  use call `aesGcmEncrypt`/`aesGcmDecrypt` (AES-GCM with random IV and auth tag).
- The codebase is **English-only**: all identifiers, comments, strings, and
  docs. Keep it that way.

## Working agreements

- Keep the category/package structure and the singleton + `@JvmStatic` style.
- Preserve Java interop: any new public helper must be callable cleanly from
  plain Java.
- Any new public API must be reflected in the ProGuard keep rules so a
  consumer who enables minification cannot strip it.
- Keep modules self-consistent: a change to the library's public API may
  require updating `app`, `java_app`, `flutter_example`, and the bundled AAR.
- Prefer editing existing files over adding scratch or staging files.
- Reusable prompt templates and the working agreement for AI agents live in
  `PROMPTS.md`. Read it when starting a new task.
