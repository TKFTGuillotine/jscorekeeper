// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Must be added for Compose post-Kotlin 2.0.0
    alias(libs.plugins.compose.compiler) apply false
    // Plugin for kotlinx serializable, a dependency of Compose Navigation.
    alias(libs.plugins.kotlinx.serialization) apply false
    // Plugin for Kotlin Symbol Processing, used by Room.
    alias(libs.plugins.devtools.ksp) apply false
    // Plugin for Room
    alias(libs.plugins.androidx.room) apply false
}