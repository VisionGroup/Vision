/***
 * @author Amir Blumental
 * @version 1.0 
 */
package com.yp2012g4.blindroid.test;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.AlarmActivity;
import com.yp2012g4.blindroid.SetClockActivity;

public class AlarmActivityTest extends ActivityInstrumentationTestCase2<AlarmActivity> {
	private Solo solo;
	private Activity activity;
	
	public AlarmActivityTest() {
		super("com.yp2012g4.blindroid", AlarmActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(),activity);
	}
	
	
	public void testNumOfButtons() {
		solo.assertCurrentActivity("wrong activity",AlarmActivity.class);
		ArrayList<Button> btnList = solo.getCurrentButtons();
		assertEquals(4, btnList.size());
	}
	
	public void testSettingAlarm(){
		setAlarmToOneMin();
		solo.waitForActivity("AlarmPopup", 60000);
		solo.clickOnScreen(50, 50);
		solo.waitForActivity("AlarmActivity", 1000);
	}
	
	public void testSnooze(){
		setAlarmToOneMin();
		solo.waitForActivity("AlarmPopup", 60000);
		solo.waitForActivity("AlarmActivity", 30000);
		solo.waitForActivity("AlarmPopup", 60000);
		solo.clickOnScreen(50, 50);
		solo.waitForActivity("AlarmActivity", 1000);
	}
	
	public void testCancelAlarm(){
		setAlarmToOneMin();
		solo.clickOnText(solo.getString(com.yp2012g4.blindroid.R.string.stopAlarm));
		assertFalse(solo.waitForActivity("AlarmPopup", 60000));
		solo.assertCurrentActivity("wrong activity",AlarmActivity.class);
	}

	private void setAlarmToOneMin() {
		solo.assertCurrentActivity("wrong activity",AlarmActivity.class);
		solo.clickOnText(solo.getString(com.yp2012g4.blindroid.R.string.setAlarmButton));
		solo.assertCurrentActivity("wrong activity",SetClockActivity.class);
		assertTrue(solo.waitForText(solo.getString(com.yp2012g4.blindroid.R.string.setHour)));
		solo.clickOnScreen(50, 50);
		solo.assertCurrentActivity("wrong activity",SetClockActivity.class);
		assertTrue(solo.waitForText(solo.getString(com.yp2012g4.blindroid.R.string.setMinutes)));
		flingUp();
		solo.clickOnScreen(50, 50);
		solo.assertCurrentActivity("wrong activity",AlarmActivity.class);
		solo.clickOnText(solo.getString(com.yp2012g4.blindroid.R.string.startAlarm));
	}
	
	private void flingUp(){
		int screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();  
		int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();  
		TouchUtils.drag(this, screenWidth/2, screenWidth/2,  screenHeight/2, screenHeight/2 -10 , 5);
	}
}
