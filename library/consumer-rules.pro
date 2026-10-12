# ============================================================
# AndroidHelper - Consumer ProGuard / R8 rules
# ------------------------------------------------------------
# This file is packaged inside the AAR and is applied
# automatically to every app that depends on the library.
#
# All Kotlin classes and methods in this library can be obfuscated,
# shrunk, and renamed normally when a consumer enables minification,
# EXCEPT for native (NDK / JNI) methods and their enclosing classes,
# which are preserved so C++ native engine calls do not fail at runtime.
# ============================================================

# Preserve native (NDK / JNI) methods and their enclosing classes
# so C++ JNI symbol bindings (e.g. Java_com_zaitxcode_android_security_Encryption_...) remain intact.
-keepclasseswithmembernames,includedescriptorclasses class com.zaitxcode.android.** {
    native <methods>;
}

# AndroidX FileProvider declared in the merged manifest.
-keep class androidx.core.content.FileProvider { *; }
-keep class androidx.core.content.FileProvider$* { *; }
