/***
 * @author Maytal
 * @version 1.1 
 */

package com.yp2012g4.blindroid.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.DialScreen;
import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.customUI.TalkingButton;

public class DialerTest extends ActivityInstrumentationTestCase2<DialScreen> {

	private Solo solo;
	private Activity activity;

	
	public DialerTest() {
		super("com.yp2012g4.blindroid", DialScreen.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}
	
	public void testSimpleDial() {
		solo.assertCurrentActivity("wrong activity",DialScreen.class);
		solo.clickOnView(solo.getView(R.id.digit1));
		solo.clickOnView(solo.getView(R.id.digit2));
		solo.clickOnView(solo.getView(R.id.digit3));
		solo.clickOnView(solo.getView(R.id.digit4));
		assertEquals(((TalkingButton)solo.getView(R.id.number)).getText().toString(), "1234");
	}
	
	public void testUndo() {
		solo.assertCurrentActivity("wrong activity",DialScreen.class);
		solo.clickOnView(solo.getView(R.id.digit1));
		solo.clickOnView(solo.getView(R.id.digit2));
		solo.clickOnView(solo.getView(R.id.button_delete));
		solo.clickOnView(solo.getView(R.id.digit3));
		solo.clickOnView(solo.getView(R.id.digit4));
		assertEquals(((TalkingButton)solo.getView(R.id.number)).getText().toString(), "134");
	}
	
	public void testReset() {
		solo.assertCurrentActivity("wrong activity",DialScreen.class);
		solo.clickOnView(solo.getView(R.id.digit1));
		solo.clickOnView(solo.getView(R.id.digit2));
		solo.clickOnView(solo.getView(R.id.digit3));
		solo.clickOnView(solo.getView(R.id.button_reset));
		solo.clickOnView(solo.getView(R.id.digit4));
		assertEquals(((TalkingButton)solo.getView(R.id.number)).getText().toString(), "4");
	}
	/*
	public void testOnTouch() {
		solo.assertCurrentActivity("wrong activity",DialScreen.class);
		View v = solo.getView(R.id.digit1);
		View v2 = solo.getView(R.id.digit2);
		solo.drag(v.getLeft() + v.getWidth()/2, v2.getLeft() + v2.getWidth()/2, v.getTop() + 
				v.getHeight()/2, v2.getTop() + v2.getHeight()/2, 0);
		solo.clickOnView(solo.getView(R.id.digit3));
		assertEquals(((TalkingButton)solo.getView(R.id.number)).getText().toString(), "23");
	}*/
	
}
