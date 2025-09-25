plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.glory.wewenunua"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.glory.wewenunua"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //Firebase
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    //End

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.8.4")
    implementation("androidx.navigation:navigation-runtime-ktx:2.9.4")
    //End

    // Cloudinary
    implementation("io.coil-kt:coil-compose:2.0.0") // Load and display images in Jetpack Compose
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Make API requests (network client)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Convert JSON to Kotlin objects and vice versa
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3") // Log API request and response for debugging
    implementation("com.cloudinary:cloudinary-android:2.3.1") // Cloudinary SDK for uploading and managing images
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Coroutines support for background tasks (API calls, uploads)
    // End




}