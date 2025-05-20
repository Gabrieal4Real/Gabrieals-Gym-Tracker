# Keep Kotlin Metadata
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep Kotlin Serialization
-keepattributes RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations, RuntimeVisibleParameterAnnotations, RuntimeInvisibleParameterAnnotations, AnnotationDefault

# Voyager
-keep class cafe.adriel.voyager.** { *; }
-keepclassmembers class cafe.adriel.voyager.** { *; }

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep your model classes
-keep class org.gabrieal.gymtracker.model.** { *; }
-keep class org.gabrieal.gymtracker.model.enums.** { *; }

# Keep Exercise class for JSON serialization
-keepclassmembers class org.gabrieal.gymtracker.model.Exercise {
    <fields>;
}

# Keep ViewModels
-keep class org.gabrieal.gymtracker.viewmodel.** { *; }

# Keep resources
-keep class **.R
-keep class **.R$* {
    <fields>;
}

# Android specific
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# Uncomment if you use Coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Uncomment if you use Reflection
# -keepattributes Signature
# -keepattributes EnclosingMethod