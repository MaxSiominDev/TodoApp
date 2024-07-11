plugins {
    alias(libs.plugins.compose.compiler)
    id("android-lib-convention")
}

android {
    namespace = "dev.maxsiomin.todoapp.feature.auth"

    defaultConfig {
        manifestPlaceholders["YANDEX_CLIENT_ID"] = "e35f47e544f74071b3bc299a5961bf4e"
    }
}

dependencies {

    implementation(projects.common)
    implementation(projects.core)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(projects.navdestinations)

    // Ktor
    implementation(libs.bundles.ktor)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Work Manager
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.authsdk)

    // Hilt for DI
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)

}