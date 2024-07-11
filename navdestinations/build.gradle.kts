plugins {
    alias(libs.plugins.kotlinxSerialization)
    id("jvm-lib-convention")
}

dependencies {

    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)

}
