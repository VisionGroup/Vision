/***
 * @author Maytal
 * @version 1.0
 */
package com.yp2012g4.vision.test.settings;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.ImageButton;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.VisionApplication;
import com.yp2012g4.vision.apps.main.MainActivity;
import com.yp2012g4.vision.apps.settings.ColorSettingsActivity;
import com.yp2012g4.vision.apps.settings.SettingsActivity;
import com.yp2012g4.vision.apps.settings.ThemeSettingsActivity;

/**
 * Tests for ThemeSettingsActivity
 * 
 * @author Maytal
 * @version 1.0
 */
public class ThemeSettingsActivityTest extends ActivityInstrumentationTestCase2<ThemeSettingsActivity> {
  private Solo solo;
  private Activity activity;
  
  public ThemeSettingsActivityTest() {
    super("com.yp2012g4.vision.apps.settings", ThemeSettingsActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  @MediumTest public void testNumOfButtons() {
    solo.assertCurrentActivity("Check on first activity", ThemeSettingsActivity.class);
    final ArrayList<Button> btnList = solo.getCurrentButtons();
    assertEquals(3, btnList.size());
  }
  
  @MediumTest public void testNumOfImageButtons() {
    solo.assertCurrentActivity("Check on first activity", ThemeSettingsActivity.class);
    final ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
    assertEquals(4, btnList.size());
  }
  
  @MediumTest// /////////////////////////////SYSTEM
  // TESTS//////////////////////////////////
  public void testSetFontSizeToLarge() {
    solo.assertCurrentActivity("wrong activity", ThemeSettingsActivity.class);
    solo.clickOnText(solo.getString(com.yp2012g4.vision.R.string.large_text_size_button));
    solo.clickOnView(solo.getView(R.id.home_button));
    assertEquals(Float.valueOf(VisionApplication.LARGE_FONT_SIZE), Float.valueOf(VisionApplication.getTextSize()));
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    solo.clickOnView(solo.getView(R.id.setting_button));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    assert ((Button) solo.getView(R.id.WhiteRed)).getTextSize() == VisionApplication.LARGE_FONT_SIZE;
  }
  
  @MediumTest public void testSetFontSizeToNormal() {
    solo.assertCurrentActivity("wrong activity", ThemeSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.Normal_text_size_button));
    solo.clickOnView(solo.getView(R.id.home_button));
    assertEquals(Float.valueOf(VisionApplication.NORMAL_FONT_SIZE), Float.valueOf(VisionApplication.getTextSize()));
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    solo.clickOnView(solo.getView(R.id.setting_button));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    assert ((Button) solo.getView(R.id.WhiteRed)).getTextSize() == VisionApplication.NORMAL_FONT_SIZE;
  }
  
  @MediumTest public void testSetFontSizeToSmall() {
    solo.assertCurrentActivity("wrong activity", ThemeSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.Small_text_size_button));
    solo.clickOnView(solo.getView(R.id.home_button));
    assertEquals(Float.valueOf(VisionApplication.SMALL_FONT_SIZE), Float.valueOf(VisionApplication.getTextSize()));
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    solo.clickOnView(solo.getView(R.id.setting_button));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    assert ((Button) solo.getView(R.id.WhiteRed)).getTextSize() == VisionApplication.SMALL_FONT_SIZE;
  }
}
