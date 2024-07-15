plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinxSerialization)
    id("android-lib-convention")
    kotlin("kapt")
}

android {
    namespace = "dev.maxsiomin.todoapp.feature.todolist"
}

dependencies {

    // Room DB
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.room.compiler)

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

    // Hilt for DI
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)

}