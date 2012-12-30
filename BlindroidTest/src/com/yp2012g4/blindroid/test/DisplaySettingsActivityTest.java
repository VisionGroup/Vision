/***
 * @author Maytal
 * @version 1.0 
 */

package com.yp2012g4.blindroid.test;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageButton;
import com.yp2012g4.blindroid.R;
import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.ColorSettingsActivity;
import com.yp2012g4.blindroid.DisplaySettings;
import com.yp2012g4.blindroid.DisplaySettingsActivity;
import com.yp2012g4.blindroid.MainActivity;
import com.yp2012g4.blindroid.ThemeSettingsActivity;

public class DisplaySettingsActivityTest extends
		ActivityInstrumentationTestCase2<DisplaySettingsActivity> {
	private Solo solo;
	private Activity activity;

	
	public DisplaySettingsActivityTest() {
		super("com.yp2012g4.blindroid", DisplaySettingsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}
	
	public void testNumOfImageButtons() {
		solo.assertCurrentActivity("Check on first activity",
				DisplaySettingsActivity.class);
		ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
		assertEquals(6, btnList.size());
	}
	
	public void testSetFontSizeToLarge() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_theme));
		solo.assertCurrentActivity("wrong activity",ThemeSettingsActivity.class);
		solo.clickOnText(solo.getString(com.yp2012g4.blindroid.R.string.large_text_size_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(DisplaySettings.SIZE, "LARGE");
	}
	
	public void testSetFontSizeToNormal() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_theme));
		solo.assertCurrentActivity("wrong activity",ThemeSettingsActivity.class);
		solo.clickOnText(solo.getString(com.yp2012g4.blindroid.R.string.normal_text_size_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(DisplaySettings.SIZE, "NORMAL");
	}
	
	public void testSetFontSizeToSmall() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_theme));
		solo.assertCurrentActivity("wrong activity",ThemeSettingsActivity.class);
		solo.clickOnText(solo.getString(com.yp2012g4.blindroid.R.string.small_text_size_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(DisplaySettings.SIZE, "SMALL");
	}
	
	public void testSetColorsWhiteBlue() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.WhiteBlue));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(DisplaySettings.getTextColor(), R.color.WHITE);
		assertEquals(DisplaySettings.getBackgroundColor(), R.color.BLUE);
	}
	
	public void testSetColorsWhiteBlack() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.WhiteBlack));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(DisplaySettings.getTextColor(), R.color.WHITE);
		assertEquals(DisplaySettings.getBackgroundColor(), R.color.BLACK);
	}
	
	public void testSetColorsWhiteRed() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.WhiteRed));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(DisplaySettings.getTextColor(), R.color.WHITE);
		assertEquals(DisplaySettings.getBackgroundColor(), R.color.RED);
	}
	
	public void testSetColorsWhiteGreen() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.WhiteGreen));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(DisplaySettings.getTextColor(), R.color.WHITE);
		assertEquals(DisplaySettings.getBackgroundColor(), R.color.GREEN);
	}
	
	public void testSetColorsBlueBlack() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.BlueBlack));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(DisplaySettings.getTextColor(), R.color.BLUE);
		assertEquals(DisplaySettings.getBackgroundColor(), R.color.BLACK);
	}
	
	public void testSetColorsRedBlack() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.RedBlack));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(DisplaySettings.getTextColor(), R.color.RED);
		assertEquals(DisplaySettings.getBackgroundColor(), R.color.BLACK);
	}
	
	public void testSetColorsGreenBlack() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.GreenBlack));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		assertEquals(DisplaySettings.getTextColor(), R.color.GREEN);
		assertEquals(DisplaySettings.getBackgroundColor(), R.color.BLACK);
	}
	
	public void testClickOnBackButton() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.back_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		
		solo.clickOnView(solo.getView(R.id.button_set_theme));
		solo.assertCurrentActivity("wrong activity",ThemeSettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.back_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
	}
	
//	public void testClickOnHomeButton() {
//		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
//		solo.clickOnView(solo.getView(R.id.home_button));
//		solo.assertCurrentActivity("wrong activity",MainActivity.class);
//	}
	
	public void testClickOnSettingsButton() {
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
		solo.clickOnView(solo.getView(R.id.settings_button));
		solo.assertCurrentActivity("wrong activity",DisplaySettingsActivity.class);
	}
}
