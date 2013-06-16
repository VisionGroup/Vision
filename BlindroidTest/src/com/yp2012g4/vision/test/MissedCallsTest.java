package com.yp2012g4.vision.test;

import android.app.Activity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.CallListActivity;
import com.yp2012g4.vision.apps.phoneStatus.PhoneStatusActivity;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.test.utils.ManagerUtils;

public class MissedCallsTest extends ActivityInstrumentationTestCase2<CallListActivity> {
  Resources res;
  private Solo solo;
  private Activity activity;
  
  // private PhoneNotifications pn;
  public MissedCallsTest() {
    super("com.yp2012g4.vision", CallListActivity.class);
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
  }
  
  @MediumTest public void test_missedCallsScreen() {
    ManagerUtils.addUnansweredCall(activity.getApplicationContext(), "0000");
    checkNonEmptyCallList(com.yp2012g4.vision.R.id.button_getMissedCalls);
    ManagerUtils.removeAllUnansweredCalls(activity.getApplicationContext());
    checkEmptyMissedCallList(com.yp2012g4.vision.R.id.button_getMissedCalls);
  }
  
  private void checkNonEmptyCallList(int id) {
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
    final TalkingImageButton tb = (TalkingImageButton) activity.findViewById(id);
    // Test Back button
    solo.clickOnView(tb);
    solo.waitForActivity(CallListActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", CallListActivity.class);
    solo.clickOnView(solo.getView(com.yp2012g4.vision.R.id.back_button));
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
  }
  
  private void checkEmptyMissedCallList(int id) {
    solo.assertCurrentActivity("wrong activity", PhoneStatusActivity.class);
    final TalkingImageButton tb = (TalkingImageButton) activity.findViewById(id);
    // should go back if list is empty.
    solo.clickOnView(tb);
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
