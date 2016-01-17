package com.hkm.lycollectionsample;

import android.net.Uri;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.hkm.lycollectionsample.life.LifeCycleApp;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<LifeCycleApp> {
    public ApplicationTest() {
        super(LifeCycleApp.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
      /*  mFirstTestActivity = getActivity();
        mFirstTestText =
                (TextView) mFirstTestActivity
                        .findViewById(R.id.my_first_test_text_view);*/
    }

    /**
     * The name 'test preconditions' is a convention to signal that if this
     * test doesn't pass, the test case was not set up properly and it might
     * explain any and all failures in other tests.  This is not guaranteed
     * to run before other tests, as junit uses reflection to find the tests.
     */
    @SmallTest
    public void testPreconditions() {
    }


    /**
     * Test basic startup/shutdown of Application
     */
    @MediumTest
    public void testSimpleCreate() {
        // createApplication();
        String url = "http://hypebeast.com/tags/stephen-curry";
        Uri base = Uri.parse(url);
        String host = base.getHost();
        String auth = base.getAuthority();
        String[] segments = base.getPath().split("/");

        assertEquals("are they the same", segments[1], "tags");


    }

}