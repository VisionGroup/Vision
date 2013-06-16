package com.yp2012g4.vision.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsSender.SendSMSActivity;
import com.yp2012g4.vision.tools.VisionGestureDetector;

/**
 * Tests for SendSMSActivity
 * 
 * @author Amir Mizrachi
 * @version 2.0
 */
public class SendSMSActivityTest extends ActivityInstrumentationTestCase2<SendSMSActivity> {
  private Solo solo;
  private Activity activity;
  
  public SendSMSActivityTest() {
    super("com.yp2012g4.vision.apps.smsSender", SendSMSActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
    solo.assertCurrentActivity("Check on first activity", SendSMSActivity.class);
  }
  
  @MediumTest public void testEmptyPhoneNumber() {
    solo.clickOnView(solo.getView(R.id.phoneNumber));
    assertEquals(VisionGestureDetector._spokenString, activity.getString(R.string.enter_a_phone_number));
  }
  
  @MediumTest public void testEmptyMessage() {
    solo.clickOnView(solo.getView(R.id.message));
    assertEquals(VisionGestureDetector._spokenString, activity.getString(R.string.enter_a_message));
  }
  
  // TODO: Need to activate keyboard within test in order to test other branches
  @Override protected void tearDown() throws Exception {
    super.tearDown();
  }
}
