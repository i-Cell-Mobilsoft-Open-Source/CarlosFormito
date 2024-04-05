plugins {
    `android-library`
    `kotlin-android`
    `maven-publish`
}

apply<CommonLibraryGradlePlugin>()

android {
    namespace = "com.icell.external.carlosformito.ui"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
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
    publications {
        register("GitHubPackagesRelease", MavenPublication::class.java) {
            groupId = "com.icell.external.carlosformito"
            artifactId = "carlosformito-material3"
            version = "0.0.2-SNAPSHOT"
            artifact("$buildDir/outputs/aar/material3-debug.aar")

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
