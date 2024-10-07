plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Required for Compose post-Kotlin 2.0.0.
    alias(libs.plugins.compose.compiler)
    // Plugin for kotlinx serializable, a dependency of Compose Navigation.
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.guillotine.jscorekeeper"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.guillotine.jscorekeeper"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // Navigation Compose.
    implementation(libs.androidx.navigation.compose)
    // Dependency for type-safe Navigation Compose.
    implementation(libs.kotlinx.serialization.core)
    // For the Google Font provider used for the clue box font (I'm not licensing Swiss).
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.3")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}