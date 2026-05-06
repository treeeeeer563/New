# Keep our main activity
-keep public class com.system.update.MainActivity {
    public <init>();
    public void onCreate(android.os.Bundle);
}

# Remove logs
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}