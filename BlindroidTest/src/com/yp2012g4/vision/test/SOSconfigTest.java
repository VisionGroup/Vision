package com.yp2012g4.vision.test;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.sos.SOSconfig;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionGestureDetector;

/**
 * Tests for SOSconfig
 * 
 * @author Amir Mizrachi
 * @version 2.0
 */
public class SOSconfigTest extends ActivityInstrumentationTestCase2<SOSconfig> {
  private Solo solo;
  private Activity activity;
  
  public SOSconfigTest() {
    super("com.yp2012g4.vision.apps.sos", SOSconfig.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
    solo.assertCurrentActivity("wrong activity", SOSconfig.class);
  }
  
  @MediumTest public void testUndo() {
    solo.clickOnView(solo.getView(R.id.digit9));
    solo.clickOnView(solo.getView(R.id.digit8));
    solo.clickOnView(solo.getView(R.id.button_delete));
    solo.clickOnView(solo.getView(R.id.digit7));
    solo.clickOnView(solo.getView(R.id.digit6));
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "976");
  }
  
  @MediumTest public void testMove() {
    View v = solo.getView(R.id.digit3);
    int left1 = VisionGestureDetector.getRelativeLeft(v);
    int top1 = VisionGestureDetector.getRelativeTop(v);
    v = solo.getView(R.id.digit9);
    final int left2 = VisionGestureDetector.getRelativeLeft(v);
    final int top2 = VisionGestureDetector.getRelativeTop(v);
    TouchUtils.drag(this, left1, left2 + 1, top1, top2 + 1, 10);
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "9");
    v = solo.getView(R.id.digit1);
    left1 = VisionGestureDetector.getRelativeLeft(v);
    top1 = VisionGestureDetector.getRelativeTop(v);
    TouchUtils.drag(this, left2, left1 + 1, top2, top1 + 1, 5);
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "91");
  }
  
  @MediumTest public void testConfigNumber() {
    solo.clickOnView(solo.getView(R.id.digit0));
    solo.clickOnView(solo.getView(R.id.digit5));
    solo.clickOnView(solo.getView(R.id.digit2));
    solo.clickOnView(solo.getView(R.id.digit4));
    solo.clickOnView(solo.getView(R.id.digit1));
    solo.clickOnView(solo.getView(R.id.digit2));
    solo.clickOnView(solo.getView(R.id.digit3));
    solo.clickOnView(solo.getView(R.id.digit4));
    solo.clickOnView(solo.getView(R.id.digit5));
    solo.clickOnView(solo.getView(R.id.digit6));
    solo.clickOnView(solo.getView(R.id.button_ok));
    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SOSconfig.getActivityContext());
    assertEquals(sp.getString(activity.getString(R.string.sos_number), ""), "0524123456");
  }
  
  @MediumTest public void testOkButtonWithEmptyNumber() {
    final View v = solo.getView(R.id.button_ok);
    TouchUtils.longClickView(this, v);
    assertEquals(VisionGestureDetector._spokenString, activity.getString(R.string.SOS_number_empty));
  }
  
  @MediumTest public void testReset() {
    solo.assertCurrentActivity("wrong activity", SOSconfig.class);
    solo.clickOnView(solo.getView(R.id.digit9));
    solo.clickOnView(solo.getView(R.id.digit8));
    solo.clickOnView(solo.getView(R.id.digit7));
    solo.clickOnView(solo.getView(R.id.button_reset));
    solo.clickOnView(solo.getView(R.id.digit6));
    solo.clickOnView(solo.getView(R.id.digit5));
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "65");
  }
  
  @MediumTest public void testNumberToDial() {
    solo.clickOnView(solo.getView(R.id.digit9));
    solo.clickOnView(solo.getView(R.id.digit7));
    solo.clickOnView(solo.getView(R.id.digit2));
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "972");
  }
  
  @Override protected void tearDown() throws Exception {
    super.tearDown();
  }
}
