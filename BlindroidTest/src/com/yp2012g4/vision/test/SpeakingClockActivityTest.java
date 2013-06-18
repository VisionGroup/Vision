/***
 * @author Amir Blumental
 * @version 1.0
 */
package com.yp2012g4.vision.test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.clock.SpeakingClockActivity;

public class SpeakingClockActivityTest extends ActivityInstrumentationTestCase2<SpeakingClockActivity> {
  private Solo solo;
  private Activity activity;
  
  public SpeakingClockActivityTest() {
    super("com.yp2012g4.vision.apps.clock", SpeakingClockActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  @MediumTest public void testCheckDate() {
    solo.assertCurrentActivity("wrong activity", SpeakingClockActivity.class);
    final Calendar cal = Calendar.getInstance();
    final String date = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(cal.getTime());
    final String text = solo.getText(1).getText().toString();
    assertTrue(text.equals(date));
    solo.clickOnText(date);
  }
  
  @MediumTest public void testCheckTime() {
    solo.assertCurrentActivity("wrong activity", SpeakingClockActivity.class);
    final Calendar cal = Calendar.getInstance();
    final String ampm = cal.get(Calendar.AM_PM) == Calendar.AM ? activity.getString(R.string.AM) : activity.getString(R.string.PM);
    final int h = cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR);
    final String s = h + " : " + cal.get(Calendar.MINUTE) + " " + ampm;
    final String text = solo.getText(0).getText().toString();
    assertTrue(text.equals(s));
    solo.clickOnText(s);
  }
}
