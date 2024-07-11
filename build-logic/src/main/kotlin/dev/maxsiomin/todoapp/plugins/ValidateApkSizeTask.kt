package dev.maxsiomin.todoapp.plugins

import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ValidateApkSizeTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val maxApkSize: Property<Int>

    @get:Internal
    val apkTooLarge: Property<Boolean> = project.objects.property(Boolean::class.java)

    @TaskAction
    fun execute() = runBlocking {
        var valid = true
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach { apkFile ->
                if (apkFile.exists()) {
                    val fileSizeInBytes = apkFile.length()
                    val fileSizeInKB = fileSizeInBytes / 1024
                    val fileSizeInMB = fileSizeInKB / 1024
                    if (fileSizeInMB > maxApkSize.get()) {
                        valid = false
                    }
                }
            }
        apkTooLarge.set(!valid)
    }

    private fun processApk(apkFile: File) {
        if (apkFile.exists()) {
            val fileSizeInBytes = apkFile.length()
            val fileSizeInKB = fileSizeInBytes / 1024
            val fileSizeInMB = fileSizeInKB / 1024
            if (fileSizeInMB > maxApkSize.get()) {
                apkTooLarge.set(true)
            } else {
                apkTooLarge.set(false)
            }
        }
    }

}