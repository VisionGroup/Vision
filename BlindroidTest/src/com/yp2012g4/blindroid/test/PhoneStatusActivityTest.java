/**
 * 
 */
package com.yp2012g4.blindroid.test;

import android.content.res.Resources;
import android.os.BatteryManager;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.PhoneStatusActivity;
import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

/**
 * @author Dell
 * 
 */
public class PhoneStatusActivityTest extends
	ActivityInstrumentationTestCase2<PhoneStatusActivity> {
    Resources res;
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
	res = getInstrumentation().getContext().getResources();
    }

    public void test_signalToPercent() {
	solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);

	assertEquals(getActivity().signalToString(99), "No Signal.");
	assertEquals(getActivity().signalToString(28), "Signal is very good.");
	assertEquals(getActivity().signalToString(10), "Signal is good.");
	assertEquals(getActivity().signalToString(6), "Signal is poor.");
	assertEquals(getActivity().signalToString(3), "Signal is very poor.");

    }

//    public void test_getChargeStatus() {
//	solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
//
//	assertEquals(
//		getActivity().getChargeStatus(
//			BatteryManager.BATTERY_STATUS_CHARGING), "Charging");
//	assertEquals(
//		getActivity().getChargeStatus(
//			BatteryManager.BATTERY_STATUS_FULL), "Charging");
//	assertEquals(
//		getActivity().getChargeStatus(
//			BatteryManager.BATTERY_STATUS_NOT_CHARGING), "");
//    }

    public void test_TalkingImageButton() {
	TalkingImageButton tlkbtn = (TalkingImageButton) solo
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
