package dev.maxsiomin.todoapp.plugins

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.create

class TelegramPlugin : Plugin<Project> {

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
                ValidateApkSizeTask::class.java,
                api,
            )
            validateTaskProvider.configure {
                token.set(ext.token)
                chatId.set(ext.chatId)
                apkDir.set(artifacts)
                maxApkSize.set(ext.maxApkSize)
            }

            project.tasks.register(
                "analyzeApkFor$name",
                AnalyzeApkTask::class.java,
                api,
            ).configure {
                dependsOn("createDebugApkListingFileRedirect")
                token.set(ext.token)
                chatId.set(ext.chatId)
                apkDir.set(artifacts)
                projectDir.set(project.projectDir)
            }

            project.tasks.register(
                "reportTelegramApkFor$name",
                TgTask::class.java,
                api,
            ).configure {
                val sizeStr: Property<String> = project.objects.property(String::class.java)
                sizeStr.set("")
                if (ext.validationEnabled.get() == true) {
                    dependsOn(validateTaskProvider)
                    sizeStr.set(validateTaskProvider.flatMap { it.size })
                }
                this.sizeStr.set(sizeStr)
                apkDir.set(artifacts)
                token.set(ext.token)
                chatId.set(ext.chatId)

                if (ext.analysisEnabled.get() == true) {
                    finalizedBy("analyzeApkFor$name")
                }
            }
        }
    }

}

interface TelegramExtension {
    val chatId: Property<String>
    val token: Property<String>
    val maxApkSize: Property<Int>
    val validationEnabled: Property<Boolean>
    val analysisEnabled: Property<Boolean>
}

