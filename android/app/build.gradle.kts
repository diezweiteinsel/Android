@file:Suppress("DEPRECATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "de.cau.inf.se.sopro"
    compileSdk = 36

    defaultConfig {
        applicationId = "de.cau.inf.se.sopro"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        /* ⚠ Cleartext (HTTP) enabled on purpose.
         * Educational project: keeps setup simple so students can use a local backend
         * (e.g., http://10.0.2.2) without extra configuration.
         * Do NOT use this in production — enforce HTTPS and remove this flag.
         */
        manifestPlaceholders["ALLOW_CLEARTEXT"] = "true"
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
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

// Retrofit 3 + official Kotlinx-Converter (BOM)
    implementation(platform(libs.retrofit.bom))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.androidx.compose.foundation)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp 5 (BOM)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // kotlinx-serialization runtime
    implementation(libs.kotlinx.serialization.json)

    //mocking
    implementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.material3)
    implementation(libs.androidx.navigationevent)
    ksp(libs.room.compiler)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.size.class1)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)


    // Navigation-Compose
    implementation(libs.androidx.navigation.compose)

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
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("com.google.dagger:hilt-android:2.57.2")
    ksp("com.google.dagger:hilt-compiler:2.57.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.14.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("app.cash.turbine:turbine:1.2.1")
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")
    implementation("com.auth0.android:jwtdecode:2.0.2")
    implementation("androidx.compose.material:material:1.9.2")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("androidx.compose.material:material-icons-extended:<version>")
}
ksp{
    arg("room.schemaLocation", "$projectDir/schemas");
    arg("ksp.verbose", "true")
}
