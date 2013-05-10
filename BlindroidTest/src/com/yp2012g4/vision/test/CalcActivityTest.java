package com.yp2012g4.vision.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.CalcActivity;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionGestureDetector;

public class CalcActivityTest extends
	ActivityInstrumentationTestCase2<CalcActivity> {
    private Solo solo;
    private Activity activity;

    public CalcActivityTest() {
	super("com.yp2012g4.vision", CalcActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	activity = getActivity();
	solo = new Solo(getInstrumentation(), activity);

    }

    public void testResultButton() {
	solo.assertCurrentActivity("wrong activity", CalcActivity.class);
	solo.clickOnView(solo.getView(R.id.digit1));
	solo.clickOnView(solo.getView(R.id.digit2));
	solo.clickOnView(solo.getView(R.id.digit7));
	solo.clickOnView(solo.getView(R.id.plus));
	solo.clickOnView(solo.getView(R.id.digit4));
	solo.clickOnView(solo.getView(R.id.digit3));
	solo.clickOnView(solo.getView(R.id.result));
	assertEquals(((TalkingButton) solo.getView(R.id.result)).getText()
		.toString(), "127+43");
    }

    public void testComplicatedCalculation() {
	solo.assertCurrentActivity("wrong activity", CalcActivity.class);
	solo.clickOnView(solo.getView(R.id.digit8));
	solo.clickOnView(solo.getView(R.id.digit5));
	solo.clickOnView(solo.getView(R.id.plus));
	solo.clickOnView(solo.getView(R.id.digit6));
	solo.clickOnView(solo.getView(R.id.equals));
	assertEquals(((TalkingButton) solo.getView(R.id.result)).getText()
		.toString(), "91");
	solo.clickOnView(solo.getView(R.id.multiplicity));
	solo.clickOnView(solo.getView(R.id.digit3));
	solo.clickOnView(solo.getView(R.id.equals));
	assertEquals(((TalkingButton) solo.getView(R.id.result)).getText()
		.toString(), "273");
    }

    public void testClear() {
	solo.assertCurrentActivity("wrong activity", CalcActivity.class);
	solo.clickOnView(solo.getView(R.id.digit9));
	solo.clickOnView(solo.getView(R.id.digit4));
	solo.clickOnView(solo.getView(R.id.div));
	solo.clickOnView(solo.getView(R.id.digit5));
	solo.clickOnView(solo.getView(R.id.clear));
	solo.clickOnView(solo.getView(R.id.digit3));
	solo.clickOnView(solo.getView(R.id.multiplicity));
	solo.clickOnView(solo.getView(R.id.digit7));
	assertEquals(((TalkingButton) solo.getView(R.id.result)).getText()
		.toString(), "3*7");
    }

    public void testDecimalNumber() {
	solo.assertCurrentActivity("wrong activity", CalcActivity.class);
	solo.clickOnView(solo.getView(R.id.digit5));
	solo.clickOnView(solo.getView(R.id.dot));
	solo.clickOnView(solo.getView(R.id.digit7));
	solo.clickOnView(solo.getView(R.id.plus));
	solo.clickOnView(solo.getView(R.id.digit2));
	solo.clickOnView(solo.getView(R.id.digit3));
	solo.clickOnView(solo.getView(R.id.dot));
	solo.clickOnView(solo.getView(R.id.digit6));
	solo.clickOnView(solo.getView(R.id.equals));
	assertEquals(((TalkingButton) solo.getView(R.id.result)).getText()
		.toString(), "29.3");
    }

    public void testDivisionByZero() {
	solo.assertCurrentActivity("wrong activity", CalcActivity.class);
	solo.clickOnView(solo.getView(R.id.digit4));
	solo.clickOnView(solo.getView(R.id.digit5));
	solo.clickOnView(solo.getView(R.id.div));
	solo.clickOnView(solo.getView(R.id.digit0));
	solo.clickOnView(solo.getView(R.id.dot));
	solo.clickOnView(solo.getView(R.id.digit0));
	solo.clickOnView(solo.getView(R.id.digit0));
	solo.clickOnView(solo.getView(R.id.digit0));
	final View v = solo.getView(R.id.equals);
	TouchUtils.longClickView(this, v);
	assertEquals(((TalkingButton) solo.getView(R.id.result)).getText().toString(),"NaN");
    }

    public void testBadAction() {
	solo.assertCurrentActivity("wrong activity", CalcActivity.class);
	// clickOnArrayElements(arr[10]);
	solo.clickOnView(solo.getView(R.id.digit4));
	solo.clickOnView(solo.getView(R.id.plus));
	solo.clickOnView(solo.getView(R.id.digit7));
	solo.clickOnView(solo.getView(R.id.equals));
	solo.clickOnView(solo.getView(R.id.digit6));
	assertEquals(VisionGestureDetector._spokenString,
		activity.getString(R.string.bad_action));
	solo.clickOnView(solo.getView(R.id.multiplicity));
	solo.clickOnView(solo.getView(R.id.dot));
	assertEquals(VisionGestureDetector._spokenString,
		activity.getString(R.string.bad_action));
	solo.clickOnView(solo.getView(R.id.digit2));
	solo.clickOnView(solo.getView(R.id.dot));
	solo.clickOnView(solo.getView(R.id.dot));
	assertEquals(VisionGestureDetector._spokenString,
		activity.getString(R.string.bad_action));
	solo.clickOnView(solo.getView(R.id.digit5));
	solo.clickOnView(solo.getView(R.id.equals));
	solo.clickOnView(solo.getView(R.id.equals));
	assertEquals(VisionGestureDetector._spokenString,
		activity.getString(R.string.bad_action));
    }

}
