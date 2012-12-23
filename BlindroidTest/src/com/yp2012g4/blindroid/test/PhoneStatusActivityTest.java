/**
 * 
 */
package com.yp2012g4.blindroid.test;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.PhoneStatusActivity;

/**
 * @author Dell
 * 
 */
public class PhoneStatusActivityTest extends
	ActivityInstrumentationTestCase2<PhoneStatusActivity> {

    private Solo solo;

    /**
     * @param name
     */
    public PhoneStatusActivityTest() {
	super("com.yp2012g4.blindroid", PhoneStatusActivity.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
	super.setUp();
	solo = new Solo(getInstrumentation(), getActivity());
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
	super.tearDown();
    }

}
