/***
 * @author Amir Blumental
 * @version 1.0
 */
package com.yp2012g4.vision.test;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Suppress;
import android.widget.ImageButton;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.apps.alarm.AlarmActivity;
import com.yp2012g4.vision.apps.clock.SpeakingClockActivity;
import com.yp2012g4.vision.apps.contacts.ContactsMenuActivity;
import com.yp2012g4.vision.apps.main.MainActivity;
import com.yp2012g4.vision.apps.phoneStatus.PhoneStatusActivity;
import com.yp2012g4.vision.apps.settings.DisplaySettingsActivity;
import com.yp2012g4.vision.apps.smsReader.ReadSmsActivity;
import com.yp2012g4.vision.apps.sos.SOSActivity;
import com.yp2012g4.vision.apps.whereAmI.WhereAmIActivity;
import com.yp2012g4.vision.customUI.TalkingImageButton;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
  private Solo solo;
  private Activity activity;
  
  public MainActivityTest() {
    super("com.yp2012g4.vision.apps.main", MainActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  public void testNumOfButtons() {
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    final ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
    assertEquals(12, btnList.size());
  }
  
  public void testSOS() {
    checkBackAndHome(com.yp2012g4.vision.R.id.sos_button, SOSActivity.class);
  }
  
  public void testSpeakingClock() {
    checkBackAndHome(com.yp2012g4.vision.R.id.time_button, SpeakingClockActivity.class);
  }
  
  public void testWhereAmI() {
    checkBackAndHome(com.yp2012g4.vision.R.id.where_am_i_button, WhereAmIActivity.class);
  }
  
  public void testPhoneStatus() {
    checkBackAndHome(com.yp2012g4.vision.R.id.phone_status_button, PhoneStatusActivity.class);
  }
  
  public void testAlarm() {
    checkBackAndHome(com.yp2012g4.vision.R.id.alarm_clock_button, AlarmActivity.class);
  }
  
  public void testContacts() {
    checkBackAndHome(com.yp2012g4.vision.R.id.contacts_button, ContactsMenuActivity.class);
  }
  
  public void testSettings() {
    checkBackAndHome(com.yp2012g4.vision.R.id.setting_button, DisplaySettingsActivity.class);
  }
  
  @Suppress public void testReadSms() {
    checkBackAndHome(com.yp2012g4.vision.R.id.read_sms_button, ReadSmsActivity.class);
  }
  
  /**
   * 
   */
  public void checkBackAndHome(int id, Class<?> c) {
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    final TalkingImageButton tb = (TalkingImageButton) activity.findViewById(id);
    // Test Back button
    solo.clickOnView(tb);
    solo.waitForActivity(c.getName(), 2000);
    //solo.assertCurrentActivity("wrong activity", c);
    solo.clickOnView(solo.getView(com.yp2012g4.vision.R.id.back_button));
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    // Test Home button
    solo.clickOnView(tb);
    //solo.assertCurrentActivity("wrong activity", c);
    solo.waitForActivity(c.getName(), 2000);
    solo.clickOnView(solo.getView(com.yp2012g4.vision.R.id.home_button));
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    solo.finishOpenedActivities();
  }
  
  @Override protected void tearDown() throws Exception {
    solo.finishOpenedActivities();
    super.tearDown();
  }
}
