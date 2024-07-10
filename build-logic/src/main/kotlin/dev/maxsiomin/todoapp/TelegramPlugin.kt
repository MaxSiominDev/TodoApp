package dev.maxsiomin.todoapp

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.create
import javax.inject.Inject

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
            project.tasks.register(
                "reportTelegramApkFor$name",
                TgTask::class.java,
                api,
            ).configure {
                apkDir.set(artifacts)
                token.set(ext.token)
                chatId.set(ext.chatId)
            }
        }
    }

}

abstract class TgTask @Inject constructor(
    private val tgApi: TgApi,
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @TaskAction
    fun execute() = runBlocking {
        val token = token.get()
        val chatId = chatId.get()
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach {
                tgApi.sendMessage(message = "Build finished", token = token, chatId = chatId)
                tgApi.sendFile(file = it, token = token, chatId = chatId)
            }
    }

}

interface TelegramExtension {
    val chatId: Property<String>
    val token: Property<String>
}

