plugins {
    alias(libs.plugins.compose.compiler)
    id("android-lib-convention")
}

android {
    namespace = "dev.maxsiomin.todoapp.core"
}

dependencies {

    implementation(projects.common)

    // Ktor
    implementation(libs.bundles.ktor)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.preference.ktx)

    // Hilt for DI
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)

}