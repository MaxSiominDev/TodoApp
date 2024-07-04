plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.compose.compiler)
    kotlin("kapt")
}

android {
    namespace = "dev.maxsiomin.todoapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.maxsiomin.todoapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["YANDEX_CLIENT_ID"] = "e35f47e544f74071b3bc299a5961bf4e"
    }

    signingConfigs {

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.create("release").apply {
                keyAlias = "todoapp"
                keyPassword = "ZqyA4ErgoXCvVs0RtSujL94H49FmbqARIbGlzANOooHhk9rhf2"
                storeFile = File("$projectDir/keys.jks")
                storePassword = "ZqyA4ErgoXCvVs0RtSujL94H49FmbqARIbGlzANOooHhk9rhf2"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {

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

    // Timber
    implementation(libs.timber)

    implementation(libs.kotlinx.datetime)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(project(":navdestinations"))

    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":feature:todolist"))
    implementation(project(":feature:auth"))

    // Hilt for DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

}