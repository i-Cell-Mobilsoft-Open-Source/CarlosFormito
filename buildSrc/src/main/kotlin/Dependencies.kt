import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

    val composeBom = "androidx.compose:compose-bom:${Versions.composeBom}"
    const val composeFoundation = "androidx.compose.foundation:foundation"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeUiGraphics = "androidx.compose.ui:ui-graphics"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeMaterial = "androidx.compose.material:material"
    const val composeMaterial3 = "androidx.compose.material3:material3"

    val material = "com.google.android.material:material:${Versions.material}"
    val extendedMaterialIcons = "androidx.compose.material:material-icons-extended:${Versions.extendedMaterialIcons}"

    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    val lifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"
    val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    val navigationRuntimeKtx = "androidx.navigation:navigation-runtime-ktx:${Versions.navigation}"
    val navigationCompose = "androidx.navigation:navigation-compose:${Versions.navigation}"

    val junit = "junit:junit:${Versions.junit}"
    val extJunit = "androidx.test.ext:junit:${Versions.extJunit}"
    val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    val truthAssert = "com.google.truth:truth:${Versions.truthAssert}"
    val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"

    val desugaring = "com.android.tools:desugar_jdk_libs:${Versions.desugaring}"
    val detektFormattingPlugin = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"
}

fun DependencyHandler.composeMaterial() {
    implementation(platform(Dependencies.composeBom))
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeUiGraphics)
    implementation(Dependencies.composeUiTooling)
    implementation(Dependencies.composeUiToolingPreview)
    implementation(Dependencies.composeMaterial)
}

fun DependencyHandler.composeMaterial3() {
    implementation(platform(Dependencies.composeBom))
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeUiGraphics)
    implementation(Dependencies.composeUiTooling)
    implementation(Dependencies.composeUiToolingPreview)
    implementation(Dependencies.composeMaterial3)
}

fun DependencyHandler.desugaring() {
    coreLibraryDesugaring(Dependencies.desugaring)
}

fun DependencyHandler.carlosCore() {
    implementation(project(mapOf("path" to ":core")))
}

fun DependencyHandler.carlosMaterial() {
    implementation(project(mapOf("path" to ":material")))
}

fun DependencyHandler.carlosMaterial3() {
    implementation(project(mapOf("path" to ":material3")))
}
