package com.lexadiky.gradle.plugin

sealed class ModuleType(val name: String, val preset: String) {

    class Target(name: String) : ModuleType(name, ModuleConstants.TARGET_PRESET)

    class Other(name: String) : ModuleType(name, ModuleConstants.LIBRARY_PRESET)

    companion object {

        fun from(name: String): ModuleType = when (name) {
            "library",
            "feature",
            "domain" -> Other(name)
            "target" -> Target(name)
            else -> throw IllegalArgumentException("Unknown module type=$name, available options are: library, feature, domain, target")
        }
    }
}