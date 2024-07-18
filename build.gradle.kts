import io.gitlab.arturbosch.detekt.Detekt
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("io.gitlab.arturbosch.detekt") version Versions.detekt apply true
    id("com.github.ben-manes.versions") version Versions.dependencyUpdates apply true
    id("org.jetbrains.kotlin.plugin.compose") version Versions.composeCompiler apply false
}

val projectSource = file(projectDir)
val configFile = files("$rootDir/quality/detekt/detekt-config.yml")
val kotlinFiles = "**/*.kt"
val resourceFiles = "**/resources/**"
val buildFiles = "**/build/**"

tasks.register<Detekt>("detektAll") {
    val autoFix = project.hasProperty("detektAutoFix")
    description = "Custom DETEKT build for all modules"
    parallel = true
    ignoreFailures = false
    autoCorrect = autoFix
    buildUponDefaultConfig = true
    setSource(projectSource)
    config.setFrom(configFile)
    include(kotlinFiles)
    exclude(resourceFiles, buildFiles)
    reports {
        html.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
    }
}

dependencies {
    detektPlugins(Dependencies.detektFormattingPlugin)
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

// https://github.com/ben-manes/gradle-versions-plugin
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}
