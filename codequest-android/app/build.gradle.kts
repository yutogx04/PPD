plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.codequest"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.codequest"
        minSdk = 26
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

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    // Android core
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Retrofit — API calls
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)

    // Room — Local database cache
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Navigation — Fragment navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Lifecycle — ViewModel + LiveData
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // Firebase — Push notifications + Auth
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.auth)

    // Google Sign-In (Credential Manager)
    implementation(libs.play.services.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services)
    implementation(libs.googleid)

    // UI
    implementation(libs.glide)
    implementation(libs.viewpager2)
    implementation(libs.swiperefreshlayout)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}