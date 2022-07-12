plugins {
    id("org.lexadiky.gradle.preset.target-android")
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
