plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.yaml:snakeyaml:1.30")
}

gradlePlugin {
    plugins {
        create("module-generator") {
            id = "com.lexadiky.gradle.plugin.module-generator"
            implementationClass = "com.lexadiky.gradle.plugin.ModuleGeneratorPlugin"
            displayName = "Module Generator"
        }
    }
}
