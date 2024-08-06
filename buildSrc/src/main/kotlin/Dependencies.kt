import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

    const val composeBom = "androidx.compose:compose-bom:${Versions.composeBom}"
    const val composeFoundation = "androidx.compose.foundation:foundation"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeUiGraphics = "androidx.compose.ui:ui-graphics"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeMaterial = "androidx.compose.material:material"
    const val composeMaterial3 = "androidx.compose.material3:material3"

    const val material = "com.google.android.material:material:${Versions.material}"
    const val extendedMaterialIcons =
        "androidx.compose.material:material-icons-extended:${Versions.extendedMaterialIcons}"

    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val lifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    const val navigationRuntimeKtx = "androidx.navigation:navigation-runtime-ktx:${Versions.navigation}"
    const val navigationCompose = "androidx.navigation:navigation-compose:${Versions.navigation}"

    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val junit = "junit:junit:${Versions.junit}"
    const val extJunit = "androidx.test.ext:junit:${Versions.extJunit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val truthAssert = "com.google.truth:truth:${Versions.truthAssert}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"

    const val desugaring = "com.android.tools:desugar_jdk_libs:${Versions.desugaring}"
    const val detektFormattingPlugin = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"
}

fun DependencyHandler.composeCore() {
    implementation(platform(Dependencies.composeBom))
    composeCoreDependencies()
}

fun DependencyHandler.composeMaterial() {
    implementation(platform(Dependencies.composeBom))
    composeCoreDependencies()
    implementation(Dependencies.composeMaterial)
}

fun DependencyHandler.composeMaterial3() {
    implementation(platform(Dependencies.composeBom))
    composeCoreDependencies()
    implementation(Dependencies.composeMaterial3)
}

private fun DependencyHandler.composeCoreDependencies() {
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeUiGraphics)
    implementation(Dependencies.composeUiTooling)
    implementation(Dependencies.composeUiToolingPreview)
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

fun DependencyHandler.carlosCommonDemo() {
    implementation(project(mapOf("path" to ":commondemo")))
}
