# AIMR POS ProGuard Rules

# Keep Room entities
-keep class com.aimr.aimrpos.data.local.entity.** { *; }

# Keep Room DAOs
-keep class com.aimr.aimrpos.data.local.dao.** { *; }

# Keep Room Database
-keep class com.aimr.aimrpos.data.local.AimrPosDatabase { *; }

# Keep Hilt classes
-keep class com.aimr.aimrpos.di.** { *; }
-keep class dagger.hilt.** { *; }

# Keep ViewModels
-keep class com.aimr.aimrpos.presentation.**.ViewModel { *; }

# Keep domain models
-keep class com.aimr.aimrpos.domain.model.** { *; }

# Keep navigation classes
-keep class com.aimr.aimrpos.navigation.** { *; }

# Keep Compose components
-keep class com.aimr.aimrpos.presentation.** { *; }

# Keep Supabase classes
-keep class io.github.jan-tennert.supabase.** { *; }

# Keep ML Kit classes
-keep class com.google.mlkit.** { *; }

# Keep CameraX classes
-keep class androidx.camera.** { *; }

# Keep Vico chart classes
-keep class com.patrykandpatrick.vico.** { *; }

# Keep WorkManager classes
-keep class androidx.work.** { *; }

# Optimization: remove unused code
-dontnote **
-dontwarn **
-ignorewarnings

# Keep annotations
-keepattributes *Annotation*

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep R classes
-keep class **.R { *; }
-keep class **.R$* { *; }

# Keep BuildConfig
-keep class **.BuildConfig { *; }