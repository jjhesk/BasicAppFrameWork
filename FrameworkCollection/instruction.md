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


To implementing #2 we can start like this:

```java

```

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
