plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.androidapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.androidapp"
        minSdk = 34
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // ═══ AndroidX Core ═══
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // ═══ Retrofit + OkHttp (réseau) ═══
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)

    // ═══ Google Sign-In ═══
    implementation(libs.play.services.auth)

    // ═══ Security (EncryptedSharedPreferences) ═══
    implementation(libs.security.crypto)

    // ═══ Lifecycle (ViewModel + LiveData) ═══
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // ═══ Tests ═══
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}