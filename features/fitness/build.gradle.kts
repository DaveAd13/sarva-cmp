plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        namespace = "com.sarva.fitness"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withHostTestBuilder {

        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }


        androidResources {
            enable = true
        }
    }

    val xcfName = "features:fitnessKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.material3)
            implementation(libs.compose.ui)
            implementation(libs.ui.tooling.preview)
            implementation(libs.material.icons.extended)
            implementation(libs.components.resources)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // --- DI (Koin) ---
            implementation(libs.bundles.koin.common)

            // --- Date ---
            api(libs.kotlinx.datetime)

            implementation(project(":core:domain"))
            implementation(project(":core:presentation"))
            implementation(project(":core:designsystem"))
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.connect.client)
            implementation(libs.ui.tooling)
        }

        iosMain.dependencies {

        }
    }

}

compose.resources {
    publicResClass = false
    packageOfResClass = "com.sarva.features.fitness.generated.resources"
    generateResClass = auto
}