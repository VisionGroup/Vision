/***
 * @author Amir Blumental
 * @version 1.0 
 */
package com.yp2012g4.blindroid.test;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.AnalogClock;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.SpeakingClockActivity;

public class SpeakingClockActivityTest extends ActivityInstrumentationTestCase2<SpeakingClockActivity> {
	private Solo solo;
	private Activity activity;
	
	public SpeakingClockActivityTest() {
		super("com.yp2012g4.blindroid", SpeakingClockActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(),activity);
	}
	
	public void testCheckDate(){
		solo.assertCurrentActivity("wrong activity", SpeakingClockActivity.class);
		Calendar cal = Calendar.getInstance();
		String date = DateFormat.getDateInstance().format(cal.getTime());
		String text = solo.getText(1).getText().toString();
		assertTrue(text.equals(date));
		solo.clickOnText(date);
		//TODO check if the time and date speaking is OK 
	}
	
	public void testCheckTime(){
		solo.assertCurrentActivity("wrong activity", SpeakingClockActivity.class);
		Calendar cal = Calendar.getInstance();
		String ampm = cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
        int h = cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR);
        String s = h + " : " + cal.get(Calendar.MINUTE) + " " + ampm;
		String text = solo.getText(0).getText().toString();
		assertTrue(text.equals(s));
		solo.clickOnText(s);
	}
	
	
}
