pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    plugins {
        kotlin("jvm") version "2.5.0-Beta1"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "androidhelper"
include(":library")
include(":app")
include(":java_app")
include(":expo_app")
project(":expo_app").projectDir = file("expo_example/android/app")
include(":rust_app")
project(":rust_app").projectDir = file("rust_app/android/app")
