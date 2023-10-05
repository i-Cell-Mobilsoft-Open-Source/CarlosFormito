plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.icell.external.carlosformito.demo"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.icell.external.carlosformito.demo"

        minSdk = ProjectConfig.demoMinSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = Versions.kotlinJvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    carlosCore()
    carlosMaterial()

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.navigationCompose)
    implementation(Dependencies.navigationRuntimeKtx)
    implementation(Dependencies.material)

    composeMaterial()

    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.extJunit)
    androidTestImplementation(Dependencies.espresso)
}
