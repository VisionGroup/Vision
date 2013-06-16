/**
 * 
 */
package com.yp2012g4.vision.test;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.Resources;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.CallListActivity;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.phoneStatus.PhoneNotifications;
import com.yp2012g4.vision.apps.phoneStatus.PhoneStatusActivity;
import com.yp2012g4.vision.customUI.TalkingImageButton;

/**
 * @author Amit Yaffe
 * 
 */
public class PhoneStatusActivityTest extends ActivityInstrumentationTestCase2<PhoneStatusActivity> {
  Resources res;
  private Solo solo;
  private Activity activity;
  private final String phoneNumber = "000000";
  
  // private PhoneNotifications pn;
  public PhoneStatusActivityTest() {
    super("com.yp2012g4.vision", PhoneStatusActivity.class);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Override protected void setUp() throws Exception {
    super.setUp();
    solo = new Solo(getInstrumentation(), getActivity());
    res = getInstrumentation().getContext().getResources();
    activity = getActivity();
    // pn = new PhoneNotifications(getActivity());
  }
  
  @MediumTest public void test_missedCallsScreen() {
    addUnansweredCall();
    checkNonEmptyCallList(com.yp2012g4.vision.R.id.button_getMissedCalls);
    removeAllUnansweredCalls();
    checkEmptyMissedCallList(com.yp2012g4.vision.R.id.button_getMissedCalls);
  }
  
  @MediumTest public void test_signalToPercent() {
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
    PhoneNotifications.signal = 99;
    assertEquals(getActivity().signalToString(), getActivity().getString(R.string.phoneStatus_message_noSignal_read));
    PhoneNotifications.signal = 28;
    assertEquals(getActivity().signalToString(), getActivity().getString(R.string.phoneStatus_message_veryGoodSignal_read));
    PhoneNotifications.signal = 10;
    assertEquals(getActivity().signalToString(), getActivity().getString(R.string.phoneStatus_message_goodSignal_read));
    PhoneNotifications.signal = 6;
    assertEquals(getActivity().signalToString(), getActivity().getString(R.string.phoneStatus_message_poorSignal_read));
    PhoneNotifications.signal = 3;
    assertEquals(getActivity().signalToString(), getActivity().getString(R.string.phoneStatus_message_veryPoorSignal_read));
  }
  
  @MediumTest public void test_TalkingImageButton() {
    final TalkingImageButton tlkbtn = (TalkingImageButton) solo.getView(R.id.button_getBatteryStatus);
    tlkbtn.setReadText("Test String");
    assertEquals(tlkbtn.getReadText(), "Test String");
    tlkbtn.setReadToolTip("Test String2");
    assertEquals(tlkbtn.getReadToolTip(), "Test String2");
  }
  
  private void checkNonEmptyCallList(final int id) {
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
    final TalkingImageButton tb = (TalkingImageButton) activity.findViewById(id);
    // Test Back button
    solo.clickOnView(tb);
    solo.waitForActivity(CallListActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", CallListActivity.class);
    solo.clickOnView(solo.getView(com.yp2012g4.vision.R.id.back_button));
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
  }
  
  private void checkEmptyMissedCallList(final int id) {
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
    final TalkingImageButton tb = (TalkingImageButton) activity.findViewById(id);
    // should go back if list is empty.
    solo.clickOnView(tb);
    solo.waitForActivity(CallListActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
  }
  
  private void removeAllUnansweredCalls() {
    try {
      final ContentValues values = new ContentValues();
      values.put(Calls.NEW, Integer.valueOf(0));
      final StringBuilder where = new StringBuilder();
      where.append(Calls.NEW + " = 1");
      where.append(" AND ");
      where.append(Calls.TYPE + " = ?");// + Calls.MISSED_TYPE);
      final int i = activity.getContentResolver().update(Calls.CONTENT_URI, values, where.toString(),
          new String[] { Integer.toString(Calls.MISSED_TYPE) });
      System.out.println(i);
    } catch (final Exception e) {
      e.getMessage();
    }
  }
  
  private void addUnansweredCall() {
    final ContentResolver cr = activity.getApplicationContext().getContentResolver();
    final ContentValues values = new ContentValues();
    values.put(CallLog.Calls.NUMBER, phoneNumber);
    values.put(CallLog.Calls.DATE, Long.valueOf(System.currentTimeMillis()));
    values.put(CallLog.Calls.DURATION, Integer.valueOf(2));
    values.put(CallLog.Calls.TYPE, Integer.valueOf(CallLog.Calls.MISSED_TYPE));
    values.put(CallLog.Calls.NEW, Integer.valueOf(1));
    values.put(CallLog.Calls.CACHED_NAME, "");
    values.put(CallLog.Calls.CACHED_NUMBER_TYPE, Integer.valueOf(0));
    values.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");
    cr.insert(CallLog.Calls.CONTENT_URI, values);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @Override protected void tearDown() throws Exception {
    super.tearDown();
  }
}