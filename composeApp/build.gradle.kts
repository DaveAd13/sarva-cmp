import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
//    alias(libs.plugins.composeHotReload)
}

kotlin {
    androidLibrary {
        namespace = "com.sarva.app.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }

        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.components.resources)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ui.tooling.preview)
            implementation(libs.material.icons.extended)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // --- DI (Koin) ---
            implementation(libs.bundles.koin.common)

            // --- Navigation3 ---
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodel.nav3)

            // --- Haze ---
            implementation(libs.haze)
            implementation(libs.haze.materials)

            implementation(project(":features:fitness"))
            implementation(project(":features:expenses"))
            implementation(project(":features:location"))
            implementation(project(":core:domain"))
            implementation(project(":core:presentation"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:data"))
            implementation(project(":core:common"))
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.connect.client)
            implementation(libs.ui.tooling)
        }

        iosMain.dependencies {

        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

composeCompiler {
    stabilityConfigurationFiles.addAll(
        rootProject.layout.projectDirectory.file("compose_stability.conf")
    )
}

compose.desktop {
    application {
        mainClass = "com.sarva.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.sarva.app"
            packageVersion = "1.0.0"
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "com.sarva.app.generated.resources"
    generateResClass = auto
}
