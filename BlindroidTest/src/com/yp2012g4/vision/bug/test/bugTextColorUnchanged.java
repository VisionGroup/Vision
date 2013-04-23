package com.yp2012g4.vision.bug.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.MainActivity;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.settings.VisionApplication;

import android.widget.TextView;;

/**
 * How to reproduce the following bug: when changing the color theme, the text color in the
 * Speaking Clock activity remains unchanged.
 * 
 * @author Maytal
 * @version 1.1
 * 
 */
public class bugTextColorUnchanged extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;
	private Activity activity;
	
	public bugTextColorUnchanged() {
		super("com.yp2012g4.vision.settings", MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(),activity);
	}	
	
	/*
     * Reproducing the bug:
     * 1. Run the application
     * 2. Go to Display Settings.
     * 3. Go to Color settings.
     * 4. Choose "White & Green".
     * 5. Go to Main Screen.
     * 6. Go to Speaking Clock.
     * 7. Click the "Home" button.
     * 8. Go to Display Settings.
     * 9. Go to Color Settings.
     * 10. Choose "Red & Black".
     * 11. Go to Main Screen.
     * 12. Go to "Speaking Clock".
     * 
     * Expected: Text Color is now red.
     */	
	
	public void testBug() {
		solo.assertCurrentActivity("wrong activity",MainActivity.class);
		solo.clickOnView(solo.getView(R.id.setting_button));
		solo.waitForActivity("DisplaySettingsActivity", 60000);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.waitForActivity("ColorSettingsActivity", 60000);
		solo.clickOnView(solo.getView(R.id.WhiteGreen));
		solo.waitForActivity("DisplaySettingsActivity", 60000);
		solo.clickOnView(solo.getView(R.id.home_button));
		solo.waitForActivity("MainActivity", 60000);
		solo.clickOnView(solo.getView(R.id.time_button));
		solo.waitForActivity("SpeakingClockActivity", 60000);
		solo.clickOnView(solo.getView(R.id.home_button));
		solo.waitForActivity("MainActivity", 60000);
		solo.clickOnView(solo.getView(R.id.setting_button));
		solo.waitForActivity("DisplaySettingsActivity", 60000);
		solo.clickOnView(solo.getView(R.id.button_set_colors));
		solo.waitForActivity("ColorSettingsActivity", 60000);
		solo.clickOnView(solo.getView(R.id.RedBlack));
		solo.waitForActivity("DisplaySettingsActivity", 60000);
		solo.clickOnView(solo.getView(R.id.home_button));
		solo.waitForActivity("MainActivity", 60000);
		solo.clickOnView(solo.getView(R.id.time_button));
		solo.waitForActivity("SpeakingClockActivity", 60000);
		//assertEquals(((TextView)(solo.getView(R.id.TimeButton))).getCurrentTextColor(),
				//VisionApplication.color_to_string.get(R.color.RED));
		
	}

}

