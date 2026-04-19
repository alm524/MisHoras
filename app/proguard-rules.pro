# Reglas de ProGuard para MisHoras.
# Mantener el puente JavaScript -> Android.
-keepclassmembers class com.mishoras.app.MainActivity$AndroidBridge {
    @android.webkit.JavascriptInterface <methods>;
}
