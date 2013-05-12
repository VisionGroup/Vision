/***
 * @author Maytal
 * @version 1.1
 */
package com.yp2012g4.vision.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.DialScreen;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;
import com.yp2012g4.vision.tools.VisionGestureDetector;

public class DialerTest extends ActivityInstrumentationTestCase2<DialScreen> {
  private Solo solo;
  private Activity activity;
  
  public DialerTest() {
    super("com.yp2012g4.vision", DialScreen.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  public void testSimpleDial() {
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
    solo.clickOnView(solo.getView(R.id.digit1));
    solo.clickOnView(solo.getView(R.id.digit2));
    solo.clickOnView(solo.getView(R.id.digit3));
    solo.clickOnView(solo.getView(R.id.digit4));
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "1234");
  }
  
  public void testDialMoreThanMaxNumbers() {
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
    for (int i = 0; i < DialScreen.MAX_LENGTH; ++i)
      solo.clickOnView(solo.getView(R.id.digit1));
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "11111111111111111111");
    solo.clickOnView(solo.getView(R.id.digit2));
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "11111111111111111112");
  }
  
  public void testDialNoNumber() {
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
    solo.clickOnView(solo.getView(R.id.dialer_dial_button));
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
  }
  
  public void testSMSNoNumber() {
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
    solo.clickOnView(solo.getView(R.id.dialer_sms_button));
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
  }
  
  public void testUndo() {
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
    solo.clickOnView(solo.getView(R.id.digit1));
    solo.clickOnView(solo.getView(R.id.digit2));
    solo.clickOnView(solo.getView(R.id.button_delete));
    solo.clickOnView(solo.getView(R.id.digit3));
    solo.clickOnView(solo.getView(R.id.digit4));
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "134");
  }
  
  public void testReset() {
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
    solo.clickOnView(solo.getView(R.id.digit1));
    solo.clickOnView(solo.getView(R.id.digit2));
    solo.clickOnView(solo.getView(R.id.digit3));
    solo.clickOnView(solo.getView(R.id.button_reset));
    solo.clickOnView(solo.getView(R.id.digit4));
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "4");
  }
  
  public static void testGetViewId() {
    VisionActivity screen = new DialScreen();
    assertEquals(screen.getViewId(), R.id.DialScreen);
  }
  
  public void testOnShowPress() {
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
    View v = solo.getView(R.id.digit9);
    TouchUtils.longClickView(this, v);
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "9");
  }
  
  public void testMove() {
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
    View v = solo.getView(R.id.digit3);
    int left1 = VisionGestureDetector.getRelativeLeft(v);
    int top1 = VisionGestureDetector.getRelativeTop(v);
    v = solo.getView(R.id.digit9);
    int left2 = VisionGestureDetector.getRelativeLeft(v);
    int top2 = VisionGestureDetector.getRelativeTop(v);
    TouchUtils.drag(this, left1, left2 + 1, top1, top2 + 1, 10);
    assertEquals(((TalkingButton) solo.getView(R.id.number)).getText().toString(), "9");
  }
}
