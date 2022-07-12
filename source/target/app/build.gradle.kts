plugins {
    id("org.lexadiky.template.gradle.preset.target")
}

android {
    resourcePrefix = "app"
    defaultConfig.applicationId = "org.lexadiky.target"
}

dependencies {
    implementation(projects.domain.sample)
    implementation(projects.library.sample)
    implementation(projects.feature.sample)
}
