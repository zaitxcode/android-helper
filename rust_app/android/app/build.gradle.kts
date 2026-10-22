plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.zaitxcode.android.apps.rustapphelper"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.zaitxcode.android.apps.rustapphelper"
        minSdk = 26
        targetSdk = 37
        versionCode = 213
        versionName = "1.1.0-alpha02"
    }

    signingConfigs {
        getByName("debug") {
            enableV1Signing = true
            enableV2Signing = true
        }
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
