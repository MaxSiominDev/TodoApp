import java.io.File

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.roomCompiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.daggerHilt) apply false
    alias(libs.plugins.compose.compiler) apply false
}


tasks.register("generateVersionConstants") {
    val outputDir = file("src/main/kotlin")
    val outputFile = File(outputDir, "Versions.kt")

    doLast {
        val tomlFile = file("gradle/libs.versions.toml")
        val tomlContent = tomlFile.readText()

        val versionRegex = """(\w+)\s*=\s*"([^"]+)"""".toRegex()
        val versions = versionRegex.findAll(tomlContent).map { matchResult ->
            val (name, version) = matchResult.destructured
            "const val ${name} = \"$version\""
        }.joinToString("\n")

        outputFile.writeText(
            """
            object Versions {
                $versions
            }
            """.trimIndent()
        )
    }
}

tasks.named("compileKotlin") {
    dependsOn("generateVersionConstants")
}