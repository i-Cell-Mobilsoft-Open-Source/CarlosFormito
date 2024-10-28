plugins {
    `android-library`
    `kotlin-android`
    `maven-publish`
    id("org.jetbrains.kotlin.plugin.compose")
}

apply(plugin = "org.jetbrains.dokka")

apply<CommonLibraryGradlePlugin>()

android {
    namespace = "com.icell.external.carlosformito.ui"

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
            groupId = "com.icell.external.carlosformito"
            artifactId = "carlosformito-material3"
            version = "0.0.4-SNAPSHOT"
            artifact(layout.buildDirectory.dir("outputs/aar/material3-debug.aar"))

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
    implementation(Dependencies.extendedMaterialIcons)

    composeMaterial3()

    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.extJunit)
    androidTestImplementation(Dependencies.espresso)
}
