/***
 * @author Maytal
 * @version 1.0
 */
package com.yp2012g4.vision.test;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.VisionApplication;
import com.yp2012g4.vision.apps.settings.ColorSettingsActivity;
import com.yp2012g4.vision.apps.settings.SettingsActivity;
import com.yp2012g4.vision.apps.settings.ThemeSettingsActivity;

public class VisionApplicationTest extends ActivityInstrumentationTestCase2<SettingsActivity> {
  private Solo solo;
  private Activity activity;
  private static HashMap<String, Integer> _colorToString = new HashMap<String, Integer>();
  
  public VisionApplicationTest() {
    super("com.yp2012g4.vision.apps.settings", SettingsActivity.class);
    _colorToString.put("BLACK", Integer.valueOf(Color.parseColor("#000000")));
    _colorToString.put("WHITE", Integer.valueOf(Color.parseColor("#FFFFFF")));
    _colorToString.put("RED", Integer.valueOf(Color.parseColor("#B40404")));
    _colorToString.put("GREEN", Integer.valueOf(Color.parseColor("#04B45F")));
    _colorToString.put("BLUE", Integer.valueOf(Color.parseColor("#0489B1")));
    _colorToString.put("LIGHT_PURPLE", Integer.valueOf(Color.parseColor("#A901DB")));
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  @MediumTest public static void testGetBackgroundColor() {
    VisionApplication.setColors("WHITE", "BLACK");
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#000000"));
  }
  
  @MediumTest public static void testGetTextColor() {
    VisionApplication.setColors("RED", "BLACK");
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#B40404"));
  }
  
  @MediumTest public static void testSetColors() {
    VisionApplication.setColors("BLUE", "BLACK");
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#000000"));
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#0489B1"));
  }
  
  @MediumTest// /////////////////////////////SYSTEM
  // TESTS//////////////////////////////////
  public void testSetFontSizeToLarge() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    float size = ((Button) solo.getView(R.id.WhiteRed)).getTextSize();
    solo.clickOnView(solo.getView(R.id.back_button));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_theme));
    solo.assertCurrentActivity("wrong activity", ThemeSettingsActivity.class);
    solo.clickOnText(solo.getString(com.yp2012g4.vision.R.string.large_text_size_button));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    assertEquals(VisionApplication._textSize, "LARGE");
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    assert ((Button) solo.getView(R.id.WhiteRed)).getTextSize() > size;
  }
  
  @MediumTest public void testSetFontSizeToNormal() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    float size = ((Button) solo.getView(R.id.WhiteRed)).getTextSize();
    solo.clickOnView(solo.getView(R.id.back_button));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_theme));
    solo.assertCurrentActivity("wrong activity", ThemeSettingsActivity.class);
    solo.clickOnText(solo.getString(com.yp2012g4.vision.R.string.normal_text_size_button));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    assertEquals(VisionApplication._textSize, "NORMAL");
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    assert ((Button) solo.getView(R.id.WhiteRed)).getTextSize() == size;
  }
  
  @MediumTest public void testSetFontSizeToSmall() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    float size = ((Button) solo.getView(R.id.WhiteRed)).getTextSize();
    solo.clickOnView(solo.getView(R.id.back_button));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_theme));
    solo.assertCurrentActivity("wrong activity", ThemeSettingsActivity.class);
    solo.clickOnText(solo.getString(com.yp2012g4.vision.R.string.small_text_size_button));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    assertEquals(VisionApplication._textSize, "SMALL");
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    assert ((Button) solo.getView(R.id.WhiteRed)).getTextSize() < size;
  }
  
  @MediumTest public void testWhiteBlueSmall() {
    testSetFontSizeToSmall();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsWhiteBlue();
  }
  
  @MediumTest public void testWhiteBlackSmall() {
    testSetFontSizeToSmall();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsWhiteBlack();
  }
  
  @MediumTest public void testWhiteRedSmall() {
    testSetFontSizeToSmall();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsWhiteRed();
  }
  
  @MediumTest public void testWhiteGreenSmall() {
    testSetFontSizeToSmall();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsWhiteGreen();
  }
  
  @MediumTest public void testBlueBlackSmall() {
    testSetFontSizeToSmall();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsBlueBlack();
  }
  
  @MediumTest public void testRedBlackSmall() {
    testSetFontSizeToSmall();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsRedBlack();
  }
  
  @MediumTest public void testGreenBlackSmall() {
    testSetFontSizeToSmall();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsGreenBlack();
  }
  
  @MediumTest public void testSetColorsWhiteBlue() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.WhiteBlue));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#FFFFFF"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#0489B1"));
  }
  
  @MediumTest public void testSetColorsWhiteBlack() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.WhiteBlack));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#FFFFFF"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#000000"));
  }
  
  @MediumTest public void testSetColorsWhiteRed() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.WhiteRed));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#FFFFFF"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#B40404"));
  }
  
  @MediumTest public void testSetColorsWhiteGreen() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.WhiteGreen));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#FFFFFF"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#04B45F"));
  }
  
  @MediumTest public void testSetColorsBlueBlack() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.BlueBlack));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#0489B1"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#000000"));
  }
  
  @MediumTest public void testSetColorsRedBlack() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.RedBlack));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#B40404"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#000000"));
  }
  
  @MediumTest public void testSetColorsGreenBlack() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.button_set_colors));
    solo.assertCurrentActivity("wrong activity", ColorSettingsActivity.class);
    solo.clickOnView(solo.getView(R.id.GreenBlack));
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    assertEquals(VisionApplication.getTextColor(), Color.parseColor("#04B45F"));
    assertEquals(VisionApplication.getBackgroundColor(), Color.parseColor("#000000"));
  }
  
  @MediumTest public void testWhiteBlueLarge() {
    testSetFontSizeToLarge();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsWhiteBlue();
  }
  
  @MediumTest public void testWhiteBlackLarge() {
    testSetFontSizeToLarge();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsWhiteBlack();
  }
  
  @MediumTest public void testWhiteRedLarge() {
    testSetFontSizeToLarge();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsWhiteRed();
  }
  
  @MediumTest public void testWhiteGreenLarge() {
    testSetFontSizeToLarge();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsWhiteGreen();
  }
  
  @MediumTest public void testBlueBlackLarge() {
    testSetFontSizeToLarge();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsBlueBlack();
  }
  
  @MediumTest public void testRedBlackLarge() {
    testSetFontSizeToLarge();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsRedBlack();
  }
  
  @MediumTest public void testGreenBlackLarge() {
    testSetFontSizeToLarge();
    solo.clickOnView(solo.getView(R.id.back_button));
    testSetColorsGreenBlack();
  }
}
