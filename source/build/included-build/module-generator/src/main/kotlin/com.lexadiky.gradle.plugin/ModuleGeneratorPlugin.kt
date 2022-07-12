package com.lexadiky.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class ModuleGeneratorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.register("make-module", GenerateModuleTask::class.java)
    }
}
