// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.android.library") version "8.1.1" apply false
//    id("org.greenrobot:greendao-gradle-plugin") version "3.3.0" apply false
//    classpath 'org.greenrobot:greendao-gradle-plugin:3.3.0'
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.greenrobot:greendao-gradle-plugin:3.3.1") // 确保版本号为最新
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.42")
    }
}
