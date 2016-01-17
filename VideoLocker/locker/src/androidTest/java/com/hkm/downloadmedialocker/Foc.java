package com.hkm.downloadmedialocker;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Created by hesk on 12/5/15.
 */
public class Foc extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testPreconditions() {


        String url = "http://hypebeast.com/tags/stephen-curry";
        Uri base = Uri.parse(url);
        String host = base.getHost();
        String auth = base.getAuthority();
        String[] segments = base.getPath().split("/");

        assertEquals("are they the same", segments[1], "tags");
    }
}
