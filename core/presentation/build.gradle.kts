plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    androidRuntimeClasspath(libs.ui.tooling)
}

kotlin {
    android {
        namespace = "com.sarva.core.presentation"
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

    val xcfName = "core:presentationKit"

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
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.material3)
                implementation(libs.material.icons.extended)
                implementation(libs.ui.tooling.preview)
                implementation(libs.foundation)
                implementation(libs.runtime)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.components.resources)
                implementation(libs.kotlinx.datetime)
                implementation(libs.navigationevent.compose)

                // --- DI (Koin) ---
                implementation(libs.bundles.koin.common)

                implementation(project(":core:domain"))
                implementation(project(":core:designsystem"))
            }
        }

        androidMain {
            dependencies {

            }
        }

        iosMain {
            dependencies {

            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.sarva.core.presentation.generated.resources"
    generateResClass = auto
}