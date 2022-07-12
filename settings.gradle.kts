@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
pluginManagement {
    includeBuild("./source/build/included-build")
}

rootProject.name = "lexadiky-template"

includeBuild("./source/build/included-build")
includeRecursive(file("./source/library"))
includeRecursive(file("./source/domain"))
includeRecursive(file("./source/feature"))
includeRecursive(file("./source/target"))

fun includeRecursive(dir: File) {
    dir.walkTopDown().maxDepth(3).forEach { subDir ->
        if (isModule(subDir)) {
            val moduleName = createModuleName(subDir, dir)

            include(moduleName)
            project(moduleName).projectDir = subDir
        }
    }
}

fun isModule(dir: File): Boolean {
    return File(dir, "build.gradle.kts").exists() || File(dir, "build.gradle.kts.kts").exists()
}

fun createModuleName(subDir: File, dir: File): String {
    var moduleName = ":${subDir.name}"
    var currentDir = subDir.parentFile

    while (currentDir != null) {
        moduleName = ":${currentDir.name}" + moduleName

        currentDir = if (currentDir == dir) {
            null
        } else {
            currentDir.parentFile
        }
    }

    return moduleName.replace("${dir.name}-", "")
}
