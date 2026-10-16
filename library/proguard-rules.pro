# ============================================================
# AndroidHelper - Library R8 / ProGuard rules
# ------------------------------------------------------------
# Rules for the library module.
#
# They preserve the full public API surface so the published AAR
# stays callable out of the box and consumers who enable their own
# minification never lose helpers they call.
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
