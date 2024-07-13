plugins {
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.compose.compiler)
    id("android-lib-convention")
    kotlin("kapt")
}

android {
    namespace = "dev.maxsiomin.todoapp.feature.settings"
}

dependencies {

    implementation(projects.common)
    implementation(projects.core)

    // Hilt for DI
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(projects.navdestinations)

}