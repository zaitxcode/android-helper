# ============================================================
# AndroidHelper - Consumer ProGuard / R8 rules
# ------------------------------------------------------------
# This file is packaged inside the AAR and is applied
# automatically to every app that depends on the library.
#
# It preserves the full public API surface so consumers who enable
# their own minification never lose helpers they call.
# ============================================================

# Keep public API surface: class names and public members.
-keep public class com.zaitxcode.android.** {
    public *;
}

# Keep Kotlin object singleton INSTANCE fields.
-keepclassmembers class com.zaitxcode.android.** {
    public static ** INSTANCE;
}

# Keep Kotlin metadata for reflection-based tools.
-keep class kotlin.Metadata { *; }

# Preserve native (NDK / JNI) methods and their enclosing classes
# so C++ JNI symbol bindings (e.g. Java_com_zaitxcode_android_security_Encryption_...) remain intact.
-keepclasseswithmembernames,includedescriptorclasses class com.zaitxcode.android.** {
    native <methods>;
}

# AndroidX FileProvider declared in the merged manifest.
-keep class androidx.core.content.FileProvider { *; }
-keep class androidx.core.content.FileProvider$* { *; }
