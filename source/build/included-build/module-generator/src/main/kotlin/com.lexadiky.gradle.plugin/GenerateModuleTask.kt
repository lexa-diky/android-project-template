package com.lexadiky.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.yaml.snakeyaml.Yaml

open class GenerateModuleTask : DefaultTask() {

    @set:Option(
        description = "Type of generated module, alwalable options are: target, feature, library, domain.",
        option = "type"
    )
    @get:Input
    var moduleType: String = ""

    @set:Option(
        description = "Name of module",
        option = "name"
    )
    @get:Input
    var moduleName: String = ""

    @TaskAction
    fun run() {
        val projectSettings = loadProjectSettings()
        val moduleType = ModuleType.from(moduleType)
        val rootModuleFolder = project.layout.projectDirectory.dir("source")
            .dir(moduleType.name)
            .dir(moduleName)

        rootModuleFolder.asFile.deleteRecursively()
        rootModuleFolder.asFile.mkdirs()
        rootModuleFolder.dir("libs").asFile.mkdirs()
        rootModuleFolder.file("proguard-rules.pro").asFile.createNewFile()
        rootModuleFolder.file(".gitignore").asFile.writeText("/build")
        makeGradleFile(projectSettings, rootModuleFolder, moduleType)
        var kotlinSourceDir = rootModuleFolder.dir("src").dir("main").dir("kotlin")
        projectSettings.projectPackage.split(".").forEach { subDir ->
            kotlinSourceDir = kotlinSourceDir.dir(subDir)
        }
        val genDir = kotlinSourceDir.dir(moduleType.name).dir(moduleName)
        genDir.asFile.mkdirs()
        genDir.file("Sample.kt").asFile.writeText(
            """
                |package ${projectSettings.projectPackage}.${moduleType.name}.${moduleName}
                |
                |fun sample(a: Int, b: Int): Int = a + b
            """.trimMargin()
        )
        rootModuleFolder.dir("src").dir("main").file("AndroidManifest.xml").asFile
            .writeText("""
                |<?xml version="1.0" encoding="utf-8"?>
                |<manifest package="com.lexadiky.template.library.sample" />
            """.trimIndent())
    }

    private fun makeGradleFile(projectSettings: ProjectSettings, rootModuleFolder: Directory, moduleType: ModuleType) {
        when (moduleType) {
            is ModuleType.Other -> {
                rootModuleFolder.file("build.gradle.kts").asFile.writeText(
                    """
                    |plugins {
                    |    id("${moduleType.preset}")
                    |}
                    |
                    |android {
                    |    resourcePrefix = "$moduleName"
                    |}
                """.trimMargin()
                )
            }
            is ModuleType.Target -> {
                rootModuleFolder.file("build.gradle.kts").asFile.writeText(
                    """
                    |plugins {
                    |    id("${moduleType.preset}")
                    |}
                    |
                    |android {
                    |    resourcePrefix = "$moduleName"
                    |    defaultConfig.applicationId = "${projectSettings.projectPackage}.$moduleName"
                    |}
                    |
                    |dependencies {
                    |    implementation(projects.domain.sample)
                    |    implementation(projects.library.sample)
                    |    implementation(projects.feature.sample)
                    |}
                """.trimMargin()
                )
            }
        }

    }

    private fun loadProjectSettings(): ProjectSettings {
        try {
            val data = Yaml().load<Map<String, Any>>(
                project.layout.projectDirectory.file(PROJECT_FILE)
                    .asFile.bufferedReader()
            )
            return ProjectSettings(
                projectPackage = data[PROJECT_KEY_PACKAGE] as String
            )

        } catch (t: Throwable) {
            throw t
        }
    }

    companion object {

        private const val PROJECT_FILE = "project.yaml"
        private const val PROJECT_KEY_PACKAGE = "package"
    }
}