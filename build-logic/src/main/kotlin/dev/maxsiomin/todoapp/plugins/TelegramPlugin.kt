package dev.maxsiomin.todoapp.plugins

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create

class TelegramPlugin : Plugin<Project> {

    @Suppress("DefaultLocale")
    override fun apply(project: Project) {
        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)
               ?: throw GradleException("Android plugin required")

        val ext = project.extensions.create("telegramReporter", TelegramExtension::class)
        val api = TgApi(HttpClient(OkHttp))

        androidComponents.onVariants { variant: Variant ->
            val artifacts = variant.artifacts.get(SingleArtifact.APK)
            val name = variant.name.replaceFirstChar { it.uppercase() }

            val validateTaskProvider = project.tasks.register(
                "validateApkSizeFor$name",
                ValidateApkSizeTask::class.java
            ).apply {
                configure {
                    apkDir.set(artifacts)
                    maxApkSize.set(ext.maxApkSize)
                }
            }

            project.tasks.register(
                "reportTelegramApkFor$name",
                TgTask::class.java,
                api,
            ).configure {
                dependsOn(validateTaskProvider)
                val apkTooLarge = validateTaskProvider.flatMap { it.apkTooLarge }
                this.apkTooLarge.set(apkTooLarge)
                apkDir.set(artifacts)
                token.set(ext.token)
                chatId.set(ext.chatId)
            }
        }
    }

}

interface TelegramExtension {
    val chatId: Property<String>
    val token: Property<String>
    val maxApkSize: Property<Int>
}

