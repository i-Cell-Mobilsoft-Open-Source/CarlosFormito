plugins {
    `android-library`
    `kotlin-android`
    `maven-publish`
    id("org.jetbrains.kotlin.plugin.compose")
}

apply<CommonLibraryGradlePlugin>()

android {
    namespace = "com.icell.external.carlosformito.core"

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
            artifactId = "carlosformito-core"
            version = "0.0.4-SNAPSHOT"
            artifact(layout.buildDirectory.dir("outputs/aar/core-debug.aar"))

            // Attach sources
            artifact(sourceJar) {
                classifier = "sources"
            }
        }
    }
}

dependencies {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.lifecycleRuntimeKtx)

    composeCore()

    testImplementation(Dependencies.mockk)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.truthAssert)
    testImplementation(Dependencies.coroutinesTest)
    androidTestImplementation(Dependencies.extJunit)
    androidTestImplementation(Dependencies.espresso)
}
