plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.compose.compiler)
    kotlin("kapt")
    id("telegram-reporter")
}

val maxApkSizeValue = 20
val validationEnabledValue = true
val analysisEnabledValue = true
telegramReporter {
    token.set(providers.environmentVariable("TG_TOKEN"))
    chatId.set(providers.environmentVariable("TG_CHAT"))
    maxApkSize.set(maxApkSizeValue)
    validationEnabled.set(validationEnabledValue)
    analysisEnabled.set(analysisEnabledValue)
}


android {
    namespace = "dev.maxsiomin.todoapp"

    baseAndroidConfig()

    defaultConfig {
        targetSdk = AndroidConst.TARGET_SKD
        manifestPlaceholders["YANDEX_CLIENT_ID"] = "e35f47e544f74071b3bc299a5961bf4e"
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

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.google.truth)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Timber
    implementation(libs.timber)

    implementation(libs.kotlinx.datetime)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(projects.navdestinations)

    implementation(projects.common)
    implementation(projects.core)
    implementation(projects.feature.todolist)
    implementation(projects.feature.auth)
    implementation(projects.feature.settings)

    // Hilt for DI
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

}