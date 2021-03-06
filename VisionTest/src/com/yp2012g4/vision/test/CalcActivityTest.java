package com.yp2012g4.vision.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.calculator.CalcActivity;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionGestureDetector;

/**
 * Tests for CalcActivity
 * 
 * @author Amir Mizrachi
 * @version 2.0
 */
public class CalcActivityTest extends ActivityInstrumentationTestCase2<CalcActivity> {
  private Solo solo;
  private Activity activity;
  
  public CalcActivityTest() {
    super("com.yp2012g4.vision.apps.calculator", CalcActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
    solo.assertCurrentActivity("wrong activity", CalcActivity.class);
  }
  
  @MediumTest public void testResultButton() {
    solo.clickOnView(solo.getView(R.id.digit1));
    solo.clickOnView(solo.getView(R.id.digit2));
    solo.clickOnView(solo.getView(R.id.digit7));
    solo.clickOnView(solo.getView(R.id.plus));
    solo.clickOnView(solo.getView(R.id.digit4));
    solo.clickOnView(solo.getView(R.id.digit3));
    solo.clickOnView(solo.getView(R.id.result));
    assertEquals(((TalkingButton) solo.getView(R.id.result)).getText().toString(), "127+43");
  }
  
  @MediumTest public void testComplicatedCalculation() {
    solo.clickOnView(solo.getView(R.id.digit8));
    solo.clickOnView(solo.getView(R.id.digit5));
    solo.clickOnView(solo.getView(R.id.plus));
    solo.clickOnView(solo.getView(R.id.digit6));
    solo.clickOnView(solo.getView(R.id.equals));
    assertEquals(((TalkingButton) solo.getView(R.id.result)).getText().toString(), "91");
    solo.clickOnView(solo.getView(R.id.multiplicity));
    solo.clickOnView(solo.getView(R.id.digit3));
    solo.clickOnView(solo.getView(R.id.equals));
    assertEquals(((TalkingButton) solo.getView(R.id.result)).getText().toString(), "273");
  }
  
  @MediumTest public void testClear() {
    solo.clickOnView(solo.getView(R.id.digit9));
    solo.clickOnView(solo.getView(R.id.digit4));
    solo.clickOnView(solo.getView(R.id.div));
    solo.clickOnView(solo.getView(R.id.digit5));
    solo.clickOnView(solo.getView(R.id.clear));
    solo.clickOnView(solo.getView(R.id.digit3));
    solo.clickOnView(solo.getView(R.id.multiplicity));
    solo.clickOnView(solo.getView(R.id.digit7));
    assertEquals(((TalkingButton) solo.getView(R.id.result)).getText().toString(), "3*7");
  }
  
  @MediumTest public void testDecimalNumber() {
    solo.clickOnView(solo.getView(R.id.digit5));
    solo.clickOnView(solo.getView(R.id.dot));
    solo.clickOnView(solo.getView(R.id.digit7));
    solo.clickOnView(solo.getView(R.id.plus));
    solo.clickOnView(solo.getView(R.id.digit2));
    solo.clickOnView(solo.getView(R.id.digit3));
    solo.clickOnView(solo.getView(R.id.dot));
    solo.clickOnView(solo.getView(R.id.digit6));
    solo.clickOnView(solo.getView(R.id.equals));
    assertEquals(((TalkingButton) solo.getView(R.id.result)).getText().toString(), "29.3");
  }
  
  @MediumTest public void testDivisionByZero() {
    solo.clickOnView(solo.getView(R.id.digit4));
    solo.clickOnView(solo.getView(R.id.digit5));
    solo.clickOnView(solo.getView(R.id.div));
    solo.clickOnView(solo.getView(R.id.digit0));
    solo.clickOnView(solo.getView(R.id.dot));
    solo.clickOnView(solo.getView(R.id.digit0));
    solo.clickOnView(solo.getView(R.id.digit0));
    solo.clickOnView(solo.getView(R.id.digit0));
    final View v = solo.getView(R.id.equals);
    TouchUtils.longClickView(this, v);
    assertEquals(((TalkingButton) solo.getView(R.id.result)).getText().toString(), "NaN");
  }
  
  @MediumTest public void testBadAction() {
    solo.clickOnView(solo.getView(R.id.digit4));
    solo.clickOnView(solo.getView(R.id.plus));
    solo.clickOnView(solo.getView(R.id.digit7));
    solo.clickOnView(solo.getView(R.id.equals));
    solo.clickOnView(solo.getView(R.id.digit6));
    assertEquals(VisionGestureDetector._spokenString, activity.getString(R.string.bad_action));
    solo.clickOnView(solo.getView(R.id.multiplicity));
    solo.clickOnView(solo.getView(R.id.dot));
    assertEquals(VisionGestureDetector._spokenString, activity.getString(R.string.bad_action));
    solo.clickOnView(solo.getView(R.id.digit2));
    solo.clickOnView(solo.getView(R.id.dot));
    solo.clickOnView(solo.getView(R.id.dot));
    assertEquals(VisionGestureDetector._spokenString, activity.getString(R.string.bad_action));
    solo.clickOnView(solo.getView(R.id.digit5));
    solo.clickOnView(solo.getView(R.id.equals));
    solo.clickOnView(solo.getView(R.id.equals));
    assertEquals(VisionGestureDetector._spokenString, activity.getString(R.string.bad_action));
  }
}
