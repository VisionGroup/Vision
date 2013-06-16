/**
 * 
 */
package com.yp2012g4.vision.test;

import android.app.Activity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.phoneStatus.CallListActivity;
import com.yp2012g4.vision.apps.phoneStatus.PhoneStatusActivity;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.test.utils.GestureTestUtils;
import com.yp2012g4.vision.test.utils.ManagerUtils;

/**
 * @author Roman Gurevitch
 * 
 */
public class MissedCallsTest extends ActivityInstrumentationTestCase2<PhoneStatusActivity> {
  Resources res;
  private Solo solo;
  private Activity activity;
  private final String num1 = "00000";
  private final String num2 = "11111";
  
  // private PhoneNotifications pn;
  public MissedCallsTest() {
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
    GestureTestUtils.flingRight(this);
    assertEquals(num2, ((TalkingButton) solo.getView(R.id.call_number)).getText());
    GestureTestUtils.flingLeft(this);
    assertEquals(num1, ((TalkingButton) solo.getView(R.id.call_number)).getText());
    pressBack();
    ManagerUtils.removeAllUnansweredCalls(activity.getApplicationContext());
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
