/**
 * 
 */
package com.yp2012g4.vision.test;

import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.PhoneNotifications;
import com.yp2012g4.vision.PhoneStatusActivity;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingImageButton;

/**
 * @author Amit Yaffe
 * 
 */
public class PhoneStatusActivityTest extends
	ActivityInstrumentationTestCase2<PhoneStatusActivity> {
    Resources res;
    private Solo solo;

    // private PhoneNotifications pn;

    public PhoneStatusActivityTest() {
	super("com.yp2012g4.vision", PhoneStatusActivity.class);
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
	res = getInstrumentation().getContext().getResources();
	// pn = new PhoneNotifications(getActivity());
    }

    public void test_signalToPercent() {
	solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);

	PhoneNotifications.signal = 99;
	assertEquals(getActivity().signalToString(), "No Signal");
	PhoneNotifications.signal = 28;
	assertEquals(getActivity().signalToString(), "Signal is very good");
	PhoneNotifications.signal = 10;
	assertEquals(getActivity().signalToString(), "Signal is good");
	PhoneNotifications.signal = 6;
	assertEquals(getActivity().signalToString(), "Signal is poor");
	PhoneNotifications.signal = 3;
	assertEquals(getActivity().signalToString(), "Signal is very poor");

    }

    public void test_TalkingImageButton() {
	final TalkingImageButton tlkbtn = (TalkingImageButton) solo
		.getView(R.id.button_getBatteryStatus);
	tlkbtn.setReadText("Test String");
	assertEquals(tlkbtn.getReadText(), "Test String");
	tlkbtn.setReadToolTip("Test String2");
	assertEquals(tlkbtn.getReadToolTip(), "Test String2");
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
