package dev.maxsiomin.todoapp.plugins

import AndroidConst
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
    abstract val sizeStr: Property<String>

    @TaskAction
    fun execute(): Unit = runBlocking {
        val token = token.get()
        val chatId = chatId.get()
        val sizeStr = sizeStr.get()
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach { file ->
                val variant = getVariantFromApkName(file.name)
                val name = "todolist-$variant-${AndroidConst.VERSION_CODE}.apk"
                tgApi.sendMessage(message = "Build finished", token = token, chatId = chatId)
                tgApi.sendFile(file = file, filename = name, token = token, chatId = chatId)
                if (sizeStr.isNotEmpty()) {
                    tgApi.sendMessage(message = sizeStr, token = token, chatId = chatId)
                }
            }
    }

    private fun getVariantFromApkName(name: String): String {
        val withoutExtension = name.removeSuffix(".apk")
        val withoutPrefix = withoutExtension.removePrefix("app-")
        return withoutPrefix
    }

}