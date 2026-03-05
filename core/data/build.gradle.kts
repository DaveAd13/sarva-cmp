plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.serialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspCommonMainMetadata", libs.androidx.room.compiler)

    configurations.filter { it.name.startsWith("ksp") && it.name != "kspCommonMainMetadata" }
        .forEach { config ->
            add(config.name, libs.androidx.room.compiler)
        }
}

kotlin {
    android {
        namespace = "com.sarva.core.data"
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

    val xcfName = "core:dataKit"

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
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.components.resources)

                // --- Ktor ---
                implementation(libs.bundles.ktor)

                // --- DI (Koin) ---
                implementation(libs.bundles.koin.common)

                // --- Date ---
                implementation(libs.kotlinx.datetime)

                // --- DB ---
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preferences)


                implementation(project(":core:domain"))
                implementation(project(":core:common"))
            }
        }


        androidMain {
            dependencies {
                // --- Ktor ---
                implementation(libs.ktor.client.okhttp)
            }
        }


        iosMain {
            dependencies {
                // --- Ktor ---
                implementation(libs.ktor.client.darwin)
            }
        }

        jvmMain {
            dependencies {
                // --- Ktor ---
                implementation(libs.ktor.client.okhttp)
            }
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "com.sarva.core.data.generated.resources"
    generateResClass = auto
}