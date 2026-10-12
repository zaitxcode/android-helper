# ============================================================
# AndroidHelper - Library R8 / ProGuard rules
# ------------------------------------------------------------
# Rules for the library module.
#
# All Kotlin classes and methods can be obfuscated, shrunk, and
# renamed normally when minification is enabled, EXCEPT for native
# (NDK / JNI) methods and their enclosing classes, which are preserved
# so C++ native engine calls do not fail at runtime.
# ============================================================

# Preserve native (NDK / JNI) methods and their enclosing classes
# so C++ JNI symbol bindings (e.g. Java_com_zaitxcode_android_security_Encryption_...) remain intact.
-keepclasseswithmembernames,includedescriptorclasses class com.zaitxcode.android.** {
    native <methods>;
}

# AndroidX FileProvider declared in the merged manifest.
-keep class androidx.core.content.FileProvider { *; }
-keep class androidx.core.content.FileProvider$* { *; }
