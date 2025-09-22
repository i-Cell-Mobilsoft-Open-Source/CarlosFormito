plugins {
    `android-library`
    `kotlin-android`
    `maven-publish`
    id("org.jetbrains.kotlin.plugin.compose")
}

apply(plugin = "org.jetbrains.dokka")

apply<CommonLibraryGradlePlugin>()

android {
    namespace = "hu.icellmobilsoft.carlosformito.ui"

    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = Versions.kotlinJvmTarget
    }
}

val sourceJar by tasks.registering(Jar::class) {
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.set("sources")
}

publishing {
    registerCarlosRepository(project)
    publications {
        register("GitHubPackagesRelease", MavenPublication::class.java) {
            groupId = "hu.icellmobilsoft.carlosformito"
            artifactId = "carlosformito-material"
            version = "0.1.0-SNAPSHOT"
            artifact(layout.buildDirectory.dir("outputs/aar/material-debug.aar"))

            // Attach sources
            artifact(sourceJar) {
                classifier = "sources"
            }
        }
    }
}

dependencies {
    carlosCore()

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.material)
    implementation(Dependencies.extendedMaterialIcons)

    composeMaterial()

    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.extJunit)
    androidTestImplementation(Dependencies.espresso)
}
