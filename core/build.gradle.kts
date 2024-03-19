plugins {
    `android-library`
    `kotlin-android`
    `maven-publish`
}

apply<CommonLibraryGradlePlugin>()

android {
    namespace = "com.icell.external.carlosformito.core"

    kotlinOptions {
        jvmTarget = Versions.kotlinJvmTarget
    }
}

publishing {
    publications {
        register("GitHubPackagesRelease", MavenPublication::class.java) {
            groupId = "com.icell.external.carlosformito"
            artifactId = "carlosformito-core"
            version = "0.0.1-SNAPSHOT"
            artifact("$buildDir/outputs/aar/core-debug.aar")
        }
    }
}

dependencies {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.lifecycleRuntimeKtx)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.truthAssert)
    testImplementation(Dependencies.coroutinesTest)
    androidTestImplementation(Dependencies.extJunit)
    androidTestImplementation(Dependencies.espresso)
}
