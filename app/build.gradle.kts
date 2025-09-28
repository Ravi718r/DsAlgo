plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt") // ✅ kapt plugin
    id("com.google.dagger.hilt.android")
}


android {
    namespace = "com.example.dsalgo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.dsalgo"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

//    // Navigation
//    val nav_version = "2.8.6"
//    implementation("androidx.navigation:navigation-compose:$nav_version")
//
//    implementation("com.google.code.gson:gson:2.10.1")
//
//    // Firebase AI Logic for Gemini API
//    implementation(platform("com.google.firebase:firebase-bom:34.3.0"))
//    implementation("com.google.firebase:firebase-ai")
//
//    // For network requests and JSON parsing
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation("com.squareup.okhttp3:okhttp:4.11.0")
//
//    // Compose UI dependencies
//    implementation("androidx.compose.ui:ui:1.5.4")
//    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
//    implementation("androidx.compose.material3:material3:1.1.2")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
//    implementation("androidx.activity:activity-compose:1.8.0")
//
//    // Coroutines
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//
//    // Hilt (Dependency Injection)
////    implementation("com.google.dagger:hilt-android:2.57.1")
////    kapt("com.google.dagger:hilt-compiler:2.57.1")
//
//
//    // Import the BoM for the Firebase platform
//    implementation(platform("com.google.firebase:firebase-bom:34.3.0"))
//
//    // Add the dependency for the Firebase AI Logic library When using the BoM,
//    // you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-ai")
//
//    implementation("com.google.firebase:firebase-ai:16.0.0-beta01")
//
//    implementation("com.google.ai:generative-ai-client:0.11.0")

    // Navigation
    val nav_version = "2.8.6"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation("com.google.code.gson:gson:2.10.1")

    // Firebase BOM (for Firestore only)
    implementation(platform("com.google.firebase:firebase-bom:34.3.0"))

    // FREE Google AI SDK (No Firebase needed for AI)
    implementation("com.google.ai.client.generativeai:generativeai:0.7.0")

    // For network requests and JSON parsing
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // Compose UI dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // DI
    implementation("com.google.dagger:hilt-android:2.57.1")
    kapt("com.google.dagger:hilt-android-compiler:2.57.1")

    // If you use Hilt + Jetpack libraries (Navigation, Work, etc.)
    val work_version = "2.8.1" // or latest stable
    implementation("androidx.work:work-runtime-ktx:$work_version")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    //hilt view Model
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
}