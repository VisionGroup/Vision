/***
 * @author Maytal
 * @version 1.0 
 */

package com.yp2012g4.vision.test;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageButton;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.ColorSettingsActivity;

/**
 * Tests for ColorSettingsActivity
 * 
 * @author Maytal
 * @version 1.0
 */
public class ColorSettingsActivityTest extends
		ActivityInstrumentationTestCase2<ColorSettingsActivity> {
	private Solo solo;
	private Activity activity;

	
	public ColorSettingsActivityTest() {
		super("com.yp2012g4.vision", ColorSettingsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}
	
	public void testNumOfButtons() {
		solo.assertCurrentActivity("Check on first activity",
				ColorSettingsActivity.class);
		ArrayList<Button> btnList = solo.getCurrentButtons();
		assertEquals(7, btnList.size());
	}
	
	public void testNumOfImageButtons() {
		solo.assertCurrentActivity("Check on first activity",
				ColorSettingsActivity.class);
		ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
		assertEquals(4, btnList.size());
	}
	
//	public void testClickOnHomeButton() {
//		solo.assertCurrentActivity("wrong activity",ColorSettingsActivity.class);
//		solo.clickOnView(solo.getView(R.id.home_button));
//		solo.assertCurrentActivity("wrong activity",MainActivity.class);
//	}
	
}
