plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        namespace = "com.sarva.expenses"
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

    val xcfName = "features:expenses:Kit"

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
                implementation(libs.ui.tooling)
                implementation(libs.components.resources)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.compose.shimmer)
                implementation(libs.navigationevent.compose)

                // --- DI (Koin) ---
                implementation(libs.bundles.koin.common)

                // --- Date ---
                implementation(libs.kotlinx.datetime)

                implementation(project(":core:domain"))
                implementation(project(":core:presentation"))
                implementation(project(":core:designsystem"))
                implementation(project(":core:common"))
                implementation(project(":features:location"))
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
    publicResClass = false
    packageOfResClass = "com.sarva.features.expenses.generated.resources"
    generateResClass = auto
}