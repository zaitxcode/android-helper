import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
}

configure<LibraryExtension> {
    namespace = "com.zaitxcode.androidhelper"

    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        // Keep rules shipped inside the AAR. They are automatically applied to
        // every consumer app. Obfuscation is intentionally disabled for the
        // library itself, so these rules only document the public surface and
        // guarantee consumers can never lose the API they call.
        consumerProguardFiles("consumer-rules.pro")

        externalNativeBuild {
            cmake {
                cppFlags("-std=c++17")
            }
        }
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }

    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            // Obfuscation is intentionally disabled so every consumer can call
            // the library safely. The rules below are kept as reference and are
            // only applied if a consumer enables minification themselves.
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

group = "com.zaitxcode"

mavenPublishing {
    publishToMavenCentral()

    val hasSigningKey = project.hasProperty("signing.keyId") ||
        project.hasProperty("signingKeyId") ||
        project.hasProperty("signing.key") ||
        project.hasProperty("signingKey") ||
        project.hasProperty("signing.secretKeyRingFile")

    if (hasSigningKey) {
        signAllPublications()
    }

    coordinates(
        groupId = "com.zaitxcode",
        artifactId = "androidhelper",
        version = libs.versions.versionName.get()
    )

    pom {
        name.set("Android Helper")
        description.set("Enterprise-grade, clean Android & Flutter utility library under com.zaitxcode.android.*")
        url.set("https://github.com/zaitxcode/androidhelper")
        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("zaitxcode")
                name.set("ZaitXCode")
                email.set("info@zaitxcode.com")
            }
        }
        scm {
            connection.set("scm:git:github.com/zaitxcode/androidhelper.git")
            developerConnection.set("scm:git:ssh://github.com/zaitxcode/androidhelper.git")
            url.set("https://github.com/zaitxcode/androidhelper")
        }
    }
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.browser)
    api(libs.androidx.swiperefreshlayout)
    api(libs.androidx.biometric)
    api(libs.androidx.lifecycle.process)

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20231013")
}
