plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mafunzo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mafunzo"

        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
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

    // AndroidX
    implementation(libs.core)
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.fragment)

    // Material Design
    implementation(libs.material)

    // UI
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)

    // Lottie for JSON animations
    implementation("com.airbnb.android:lottie:6.4.0")

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Architecture
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // Media
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}