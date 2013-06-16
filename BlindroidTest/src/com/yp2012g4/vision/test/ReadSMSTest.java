package com.yp2012g4.vision.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.main.MainActivity;
import com.yp2012g4.vision.apps.smsReader.ReadSmsActivity;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.test.utils.GestureTestUtils;
import com.yp2012g4.vision.test.utils.ManagerUtils;

public class ReadSMSTest extends ActivityInstrumentationTestCase2<MainActivity> {
  private Solo solo;
  private Activity activity;
  final String msg = "Test message";
  final String address = "12345";
  
  public ReadSMSTest() {
    super("com.yp2012g4.vision.apps.main", MainActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
    // Add a message to the SMS inbox
    ManagerUtils.storeMessage(activity, address, msg);
  }
  
  public void testReadSms() {
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    final TalkingImageButton tb = (TalkingImageButton) activity.findViewById(R.id.read_sms_button);
    // Test Back button
    solo.clickOnView(tb);
    solo.assertCurrentActivity("wrong activity", ReadSmsActivity.class);
    assertEquals(msg, ((TalkingButton) solo.getView(R.id.sms_body)).getText());
    assertEquals(address, ((TalkingButton) solo.getView(R.id.sms_from)).getText());
    deleteCurrentSMS(true);
  }
  
  @Override protected void tearDown() throws Exception {
    solo.finishOpenedActivities();
    super.tearDown();
  }
  
  @MediumTest private void deleteCurrentSMS(final boolean confirmDelete) {
    solo.assertCurrentActivity("wrong activity", ReadSmsActivity.class);
    solo.clickOnView(solo.getView(R.id.sms_remove));
    GestureTestUtils.useDeleteConfirmation(confirmDelete, solo, this);
    solo.assertCurrentActivity("wrong activity", ReadSmsActivity.class);
  }
}
