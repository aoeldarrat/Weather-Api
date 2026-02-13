plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
//    alias(libs.plugins.kotlin)

    // Make sure that you have the Google services Gradle plugin
    id("com.google.gms.google-services")
    // Add the Performance Monitoring Gradle plugin
    id("com.google.firebase.firebase-perf")

    // Hilt dependency injection
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}
android {
    namespace = "com.app.weather"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.app.weather"
        minSdk = 28
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Dependency injection - Hilt
    implementation(libs.hilt.dagger)
    ksp(libs.hilt.compiler)

    // Room dependency
    implementation(libs.room)
    ksp(libs.room.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime.ktx)

    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))

    // Add the dependency for the Performance Monitoring library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.perf)

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics)
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}