plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.zaitxcode.android.apps.expoapphelper"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.zaitxcode.android.apps.expoapphelper"
        minSdk = 26
        targetSdk = 37
        versionCode = 213
        versionName = "1.1.0-alpha01"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(project(":library"))
    implementation("com.facebook.react:react-android:0.76.5")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
