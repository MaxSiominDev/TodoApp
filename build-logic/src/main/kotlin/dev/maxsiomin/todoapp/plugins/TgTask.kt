package dev.maxsiomin.todoapp.plugins

import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject


abstract class TgTask @Inject constructor(
    private val tgApi: TgApi,
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @get:Input
    abstract val apkTooLarge: Property<Boolean>

    @TaskAction
    fun execute(): Unit = runBlocking {
        val token = token.get()
        val chatId = chatId.get()
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach {
                tgApi.sendMessage(message = "Build finished", token = token, chatId = chatId)
                if (apkTooLarge.get()) {
                    tgApi.sendMessage(message = "Apk too large", token = token, chatId = chatId)
                } else {
                    tgApi.sendFile(file = it, token = token, chatId = chatId)
                }
            }
    }

}