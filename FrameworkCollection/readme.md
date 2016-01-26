To start the tab host UI layout, there are two options
1. solid status bar
2. translucent status bar


To implementing #1 we will code to start as the following:

```java
import com.hkm.layout.App.WeiXinHost;
import com.hkm.layout.Menu.TabIconView;
import com.hkm.layout.WeiXinTabHost;

public class MainActivity extends WeiXinHost<Fragment> {

}

```


To implementing #2 we can start like this in the xml for style:

```xml
  <!--this will be used at the article page theme setting-->
    <style name="ArticlePageTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <!-- take out the default action bar-->
        <item name="windowActionBar">false</item>
        <item name="android:windowActionBarOverlay">true</item>
        <item name="elevation">0dp</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowTranslucentNavigation">false</item>
        
        
        <item name="android:windowTranslucentStatus">true</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="colorPrimary">@color/second_pref</item>
        <item name="colorPrimaryDark">@color/main_background</item>
        <!-- <item name="actionBarStyle">@style/actionBarWhiteNoShadow</item>-->
        <!-- <item name="actionBarInsetStart"></item>-->
    </style>
    
    
```

Make use of the parameter ```<item name="android:windowTranslucentStatus">false</item>``` as this will give u the full control of status bar.

Setup the toolbar we can start something like this in anywhere in the MainActivity.class:

```java

    @Override
    protected void configToolBar(Toolbar mxToolBarV7) {
        BeastBar.Builder bb = new BeastBar.Builder();
        bb.search(R.mipmap.ic_action_find);
        bb.companyIcon(R.drawable.actionbar_bg_hb_logo);
        bb.back(R.drawable.ic_back_adjusted);
        bb.background(R.drawable.actionbar_bg_hb_white);
        mBeastWorker = BeastBar.withToolbar(this, mxToolBarV7, bb);
        mBeastWorker.setFindIconFunc(new Runnable() {
            @Override
            public void run() {
                searchView.showSearch();
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        super.configToolBar(mxToolBarV7);
    }
    
    
```
