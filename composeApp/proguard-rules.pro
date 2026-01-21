# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:/Users/davda/AppData/Local/Android/Sdk/tools/proguard/proguard-android-optimize.txt
# You can edit that file to add default rules that apply to all projects.

# Keep - Dagger specific rules
-keep class dagger.internal.codegen.** { *; }
-dontwarn dagger.internal.codegen.**

# Keep - Kotlin extensions
-keep class kotlin.Metadata { *; }

# Keep - kotlin-reflect cause of "kotlin.jvm.internal.Reflection"
-keep class kotlin.reflect.** { *; }
-dontwarn kotlin.reflect.jvm.internal.**

# Keep - kotlinx.coroutines
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }

# Keep - Koin
-keep class org.koin.** { *; }
-keepclassmembers class * {
    @org.koin.core.annotation.KoinInternalApi <methods>;
}
