plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins.register("telegram-reporter") {
        id = "telegram-reporter"
        implementationClass = "dev.maxsiomin.todoapp.plugins.TelegramPlugin"
    }
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.coroutines.core)

    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.core)

}