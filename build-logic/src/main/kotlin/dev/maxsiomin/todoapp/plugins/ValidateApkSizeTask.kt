package dev.maxsiomin.todoapp.plugins

import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class ValidateApkSizeTask @Inject constructor(
    private val tgApi: TgApi,
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val maxApkSize: Property<Int>

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @get:Internal
    val size: Property<String> = project.objects.property(String::class.java)

    @TaskAction
    fun execute() = runBlocking {
        val token = token.get()
        val chatId = chatId.get()

        val apkFile = apkDir.get().asFile.listFiles()?.first { it.name.endsWith(".apk") }!!

        val fileSizeInBytes = apkFile.length()
        val fileSizeInKB = fileSizeInBytes / 1024
        val fileSizeInMB = fileSizeInKB / 1024

        if (fileSizeInMB > maxApkSize.get()) {
            tgApi.sendMessage(message = "Apk too large", chatId = chatId, token = token)
            throw GradleException("Apk too large")
        }

        size.set("Size: $fileSizeInMB MB")
    }

}