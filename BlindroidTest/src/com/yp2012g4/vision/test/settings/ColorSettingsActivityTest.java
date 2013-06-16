/**
 * Tests for ColorSettingsActivity
 * 
 * @author Maytal
 * @version 1.0
 */
package com.yp2012g4.vision.test.settings;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.ImageButton;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.VisionApplication;
import com.yp2012g4.vision.apps.main.MainActivity;
import com.yp2012g4.vision.apps.settings.ColorSettingsActivity;

public class ColorSettingsActivityTest extends ActivityInstrumentationTestCase2<ColorSettingsActivity> {
  private Solo solo;
  private Activity activity;
  
  public ColorSettingsActivityTest() {
    super("com.yp2012g4.vision.apps.settings", ColorSettingsActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  @MediumTest public void testNumOfButtons() {
    solo.assertCurrentActivity("Check on first activity", ColorSettingsActivity.class);
    final ArrayList<Button> btnList = solo.getCurrentButtons();
    assertEquals(7, btnList.size());
  }
  
  @MediumTest public void testNumOfImageButtons() {
    solo.assertCurrentActivity("Check on first activity", ColorSettingsActivity.class);
    final ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
    assertEquals(4, btnList.size());
  }
  
  @MediumTest public void testWhiteBlue() {
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.WhiteBlue));
    solo.clickOnView(solo.getView(R.id.home_button));
    solo.waitForActivity(MainActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#FFFFFF"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#0489B1"));
  }
  
  @MediumTest public void testWhiteBlack() {
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.WhiteBlack));
    solo.clickOnView(solo.getView(R.id.home_button));
    solo.waitForActivity(MainActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#FFFFFF"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#000000"));
  }
  
  @MediumTest public void testWhiteRed() {
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.WhiteRed));
    solo.clickOnView(solo.getView(R.id.home_button));
    solo.waitForActivity(MainActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#FFFFFF"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#B40404"));
  }
  
  @MediumTest public void testWhiteGreen() {
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.WhiteGreen));
    solo.clickOnView(solo.getView(R.id.home_button));
    solo.waitForActivity(MainActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#FFFFFF"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#04B45F"));
  }
  
  @MediumTest public void testBlueBlack() {
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.BlueBlack));
    solo.clickOnView(solo.getView(R.id.home_button));
    solo.waitForActivity(MainActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#0489B1"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#000000"));
  }
  
  @MediumTest public void testRedBlack() {
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.RedBlack));
    solo.clickOnView(solo.getView(R.id.home_button));
    solo.waitForActivity(MainActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#B40404"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#000000"));
  }
  
  @MediumTest public void testGreenBlack() {
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.GreenBlack));
    solo.clickOnView(solo.getView(R.id.home_button));
    solo.waitForActivity(MainActivity.class.getName(), 2000);
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#04B45F"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#000000"));
  }
}
