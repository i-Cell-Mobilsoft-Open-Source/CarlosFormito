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

publishing {
    publications {
        register("GitHubPackagesRelease", MavenPublication::class.java) {
            groupId = "com.icell.external.carlosformito"
            artifactId = "carlosformito-material"
            version = "0.0.2-SNAPSHOT"
            artifact("$buildDir/outputs/aar/material-debug.aar")
        }
    }
}

dependencies {
    carlosCore()

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.material)

    composeMaterial()

    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.extJunit)
    androidTestImplementation(Dependencies.espresso)
}
