plugins {
    `android-library`
    `kotlin-android`
    id("com.vanniktech.maven.publish") version Versions.gradleMavenPublishPlugin
    id("org.jetbrains.kotlin.plugin.compose")
}

apply(plugin = "org.jetbrains.dokka")

apply<CommonLibraryGradlePlugin>()

android {
    namespace = "hu.icellmobilsoft.carlosformito.core"

    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = Versions.kotlinJvmTarget
    }
}

mavenPublishing {
    coordinates(
        groupId = "hu.icellmobilsoft.carlosformito",
        artifactId = "carlosformito-core",
        version = "1.0.0"
    )

    pom {
        name.set("CarlosFormito library")
        description.set("Easy form state management for Jetpack Compose.")
        inceptionYear.set("2025")
        url.set("https://github.com/i-Cell-Mobilsoft-Open-Source/CarlosFormito")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("stoicamark")
                name.set("Stoica Márk")
                email.set("mark.stoica@icellmobilsoft.hu")
            }
        }

        scm {
            url.set("https://github.com/i-Cell-Mobilsoft-Open-Source/CarlosFormito")
            connection.set("scm:git:git://github.com/i-Cell-Mobilsoft-Open-Source/CarlosFormito.git")
            developerConnection.set("scm:git:ssh://git@github.com/i-Cell-Mobilsoft-Open-Source/CarlosFormito.git")
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
