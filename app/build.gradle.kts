plugins {
    alias(libs.plugins.kotlin.android)
    id("com.android.application")
    id("com.google.gms.google-services")
    alias(libs.plugins.google.firebase.crashlytics)
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"

}

android {
    namespace = "com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.storage)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    implementation("com.google.dagger:dagger-compiler:2.51.1")
    ksp("com.google.dagger:dagger-compiler:2.51.1")

    implementation("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation("com.algolia:algoliasearch-client-kotlin:3.10.1")

    implementation("io.ktor:ktor-client-core:3.0.0")        // Core client
    implementation("io.ktor:ktor-client-okhttp:3.0.0")    // Android engine
    implementation("io.ktor:ktor-client-content-negotiation:3.0.0") // Optional if needed
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")  // Optional for JSON support
    implementation("io.ktor:ktor-client-logging:3.0.0")   // Optional for logging
    implementation("io.ktor:ktor-client-plugins:3.0.0")

    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
}