import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("io.gitlab.arturbosch.detekt") version Versions.detekt apply true
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
