plugins {
    `android-library`
    `kotlin-android`
    id("org.jetbrains.kotlin.plugin.compose")
}

apply<CommonLibraryGradlePlugin>()

android {
    namespace = "hu.icellmobilsoft.carlosformito.commondemo"

    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = Versions.kotlinJvmTarget
    }
}

dependencies {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)

    composeCore()

    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.extJunit)
    androidTestImplementation(Dependencies.espresso)
}
