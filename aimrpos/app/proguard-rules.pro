# AIMR POS ProGuard Rules - Security Hardening

# Keep security classes
-keep class com.aimr.aimrpos.security.** { *; }
-keep class com.aimr.aimrpos.crypto.** { *; }

# Keep Room entities and DAOs
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# Keep Hilt injection
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }

# Keep ML Kit
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep Supabase
-keep class io.github.jan-tennert.** { *; }

# Keep Compose
-keep class androidx.compose.** { *; }

# Obfuscate everything else
-repackageclasses
-allowaccessmodification
-overloadaggressively

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
}

# Security: prevent reflection attacks
-keepattributes *Annotation*
-dontwarn java.lang.invoke.*

# Prevent reverse engineering
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep serialization classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Firebase Crashlytics
-keep class com.google.firebase.crashlytics.** { *; }
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Biometric
-keep class androidx.biometric.** { *; }

# CameraX
-keep class androidx.camera.** { *; }

# Kotlin serialization
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-keepclassmembers @kotlinx.serialization.Serializable class * {
    static ** Companion;
}