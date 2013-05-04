/***
 * @author Maytal
 * @version 1.0 
 */

package com.yp2012g4.vision.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.settings.ColorSettingsActivity;
import com.yp2012g4.vision.settings.DisplaySettingsActivity;
import com.yp2012g4.vision.settings.ThemeSettingsActivity;
import com.yp2012g4.vision.settings.VisionApplication;

public class VisionApplicationTest extends
		ActivityInstrumentationTestCase2<DisplaySettingsActivity> {
	private Solo solo;
	private Activity activity;

	
	public VisionApplicationTest() {
		super("com.yp2012g4.vision.settings", DisplaySettingsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}
	
	public static void testGetBackgroundColor() {
		VisionApplication.setColors("WHITE", "BLACK");
		assertEquals(VisionApplication.getBackgroundColor(), "BLACK");
	}
	
	public static void testGetTextColor() {
		VisionApplication.setColors("RED", "BLACK");
		assertEquals(VisionApplication.getTextColor(), "RED");
	}
	
	public static void testSetColors() {
		VisionApplication.setColors("BLUE", "BLACK");
		assertEquals(VisionApplication.getBackgroundColor(), "BLACK");
		assertEquals(VisionApplication.getTextColor(), "BLUE");
	}
	
	
	///////////////////////////////SYSTEM TESTS//////////////////////////////////
	
	@SuppressWarnings("boxing")
	public void testSetFontSizeToLarge() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		float size = ((Button)(solo.getView(R.id.WhiteRed))).getTextSize();
		solo.clickOnView(solo.getView(R.id.back_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_theme));
		solo.assertCurrentActivity("wrong activity",ThemeSettingsActivity.class);
		solo.clickOnText(solo.getString(com.yp2012g4.vision.R.string.large_text_size_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(VisionApplication.textSize, "LARGE");
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		assert(((Button)(solo.getView(R.id.WhiteRed))).getTextSize() > size);
	}
	
	@SuppressWarnings("boxing")
	public void testSetFontSizeToNormal() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		float size = ((Button)(solo.getView(R.id.WhiteRed))).getTextSize();
		solo.clickOnView(solo.getView(R.id.back_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_theme));
		solo.assertCurrentActivity("wrong activity",ThemeSettingsActivity.class);
		solo.clickOnText(solo.getString(com.yp2012g4.vision.R.string.normal_text_size_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(VisionApplication.textSize, "NORMAL");
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		assert(((Button)(solo.getView(R.id.WhiteRed))).getTextSize() == size);
	}
	
	@SuppressWarnings("boxing")
	public void testSetFontSizeToSmall() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		float size = ((Button)(solo.getView(R.id.WhiteRed))).getTextSize();
		solo.clickOnView(solo.getView(R.id.back_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_theme));
		solo.assertCurrentActivity("wrong activity",ThemeSettingsActivity.class);
		solo.clickOnText(solo.getString(com.yp2012g4.vision.R.string.small_text_size_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(VisionApplication.textSize, "SMALL");
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		assert(((Button)(solo.getView(R.id.WhiteRed))).getTextSize() < size);
	}
	
	public void testWhiteBlueSmall() {
		testSetFontSizeToSmall();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsWhiteBlue();
	}
	
	public void testWhiteBlackSmall() {
		testSetFontSizeToSmall();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsWhiteBlack();
	}
	
	public void testWhiteRedSmall() {
		testSetFontSizeToSmall();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsWhiteRed();
	}
	
	public void testWhiteGreenSmall() {
		testSetFontSizeToSmall();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsWhiteGreen();
	}
	
	public void testBlueBlackSmall() {
		testSetFontSizeToSmall();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsBlueBlack();
	}
	
	public void testRedBlackSmall() {
		testSetFontSizeToSmall();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsRedBlack();
	}
	
	public void testGreenBlackSmall() {
		testSetFontSizeToSmall();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsGreenBlack();
	}
	
	
	public void testSetColorsWhiteBlue() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.WhiteBlue));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(VisionApplication.getTextColor(), "WHITE");
		assertEquals(VisionApplication.getBackgroundColor(), "BLUE");
	}
	
	public void testSetColorsWhiteBlack() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.WhiteBlack));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(VisionApplication.getTextColor(), "WHITE");
		assertEquals(VisionApplication.getBackgroundColor(), "BLACK");
	}
	
	public void testSetColorsWhiteRed() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.WhiteRed));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(VisionApplication.getTextColor(), "WHITE");
		assertEquals(VisionApplication.getBackgroundColor(), "RED");
	}
	
	public void testSetColorsWhiteGreen() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.WhiteGreen));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(VisionApplication.getTextColor(), "WHITE");
		assertEquals(VisionApplication.getBackgroundColor(), "GREEN");
	}
	
	public void testSetColorsBlueBlack() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.BlueBlack));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(VisionApplication.getTextColor(), "BLUE");
		assertEquals(VisionApplication.getBackgroundColor(), "BLACK");
	}
	
	public void testSetColorsRedBlack() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.RedBlack));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(VisionApplication.getTextColor(), "RED");
		assertEquals(VisionApplication.getBackgroundColor(), "BLACK");
	}
	
	public void testSetColorsGreenBlack() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.GreenBlack));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(VisionApplication.getTextColor(), "GREEN");
		assertEquals(VisionApplication.getBackgroundColor(), "BLACK");
	}
	
	public void testWhiteBlueLarge() {
		testSetFontSizeToLarge();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsWhiteBlue();
	}
	
	public void testWhiteBlackLarge() {
		testSetFontSizeToLarge();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsWhiteBlack();
	}
	
	public void testWhiteRedLarge() {
		testSetFontSizeToLarge();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsWhiteRed();
	}
	
	public void testWhiteGreenLarge() {
		testSetFontSizeToLarge();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsWhiteGreen();
	}
	
	public void testBlueBlackLarge() {
		testSetFontSizeToLarge();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsBlueBlack();
	}
	
	public void testRedBlackLarge() {
		testSetFontSizeToLarge();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsRedBlack();
	}
	
	public void testGreenBlackLarge() {
		testSetFontSizeToLarge();
		solo.clickOnView(solo.getView(R.id.back_button));
		testSetColorsGreenBlack();
	}
	
}
