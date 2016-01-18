-keepnames public class * extends io.realm.RealmObject
-keep class io.realm.** { *; }
-dontwarn javax.**
-dontwarn io.realm.**
-keep class !android.support.v7.internal.view.menu.**,android.support.** {*;}
-keep class !android.support.v7.internal.widget.**,android.support.** {*;}
-keep public class com.google.android.gms.ads.** {
   public *;
}

-keep public class com.google.ads.** {
   public *;
}
-keep public class com.google.ads.mediation.** { public *; }