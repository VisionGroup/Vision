/**
 * 
 */
package com.yp2012g4.vision.test;

import android.app.Activity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.CallListActivity;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.phoneStatus.PhoneNotifications;
import com.yp2012g4.vision.apps.phoneStatus.PhoneStatusActivity;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.test.utils.GestureUtils;
import com.yp2012g4.vision.test.utils.ManagerUtils;

/**
 * @author Amit Yaffe
 * 
 */
public class PhoneStatusActivityTest extends ActivityInstrumentationTestCase2<PhoneStatusActivity> {
  Resources res;
  private Solo solo;
  private Activity activity;
  private final String num1 = "00000";
  private final String num2 = "11111";
  
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
  
  @MediumTest public void test_missedCallsScreenAlive() {
    ManagerUtils.addUnansweredCall(activity.getApplicationContext(), num1);
    checkNonEmptyCallList();
    ManagerUtils.removeAllUnansweredCalls(activity.getApplicationContext());
    checkEmptyMissedCallList();
  }
  
  @MediumTest public void test_missedCallsScreenSwipe() {
    ManagerUtils.removeAllUnansweredCalls(activity.getApplicationContext());
    ManagerUtils.addUnansweredCall(activity.getApplicationContext(), num1);
    ManagerUtils.addUnansweredCall(activity.getApplicationContext(), num2);
    goToCallsScreen();
    assertEquals(num1, ((TalkingButton) solo.getView(R.id.call_number)).getText());
    GestureUtils.flingRight(this);
    assertEquals(num2, ((TalkingButton) solo.getView(R.id.call_number)).getText());
    pressBack();
    ManagerUtils.removeAllUnansweredCalls(activity.getApplicationContext());
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
  
  private void goToCallsScreen() {
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
    final TalkingImageButton tb = (TalkingImageButton) activity.findViewById(com.yp2012g4.vision.R.id.button_getMissedCalls);
    solo.clickOnView(tb);
  }
  
  private void checkNonEmptyCallList() {
    goToCallsScreen();
    solo.waitForActivity(CallListActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", CallListActivity.class);
    pressBack();
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
  }
  
  private void pressBack() {
    solo.clickOnView(solo.getView(com.yp2012g4.vision.R.id.back_button));
  }
  
  private void checkEmptyMissedCallList() {
    goToCallsScreen();
    solo.waitForActivity(CallListActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
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
