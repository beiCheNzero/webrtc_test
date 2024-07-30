plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.android.zkaf.webrtcjavacoderbeichen"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.zkaf.webrtcjavacoderbeichen"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    viewBinding {
        enable=true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // firebase数据库
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")

    // gson转换数据
    implementation("com.google.code.gson:gson:2.10.1")
    // webrtc
    implementation("com.mesibo.api:webrtc:1.0.5")
    // 权限
    implementation("com.guolindev.permissionx:permissionx:1.6.1")
    // ws
    implementation("org.java-websocket:Java-WebSocket:1.5.3")
    implementation("com.alibaba:fastjson:1.1.71.android")
    implementation("redis.clients:jedis:5.1.2")

    implementation(project(":myapplication"))
}