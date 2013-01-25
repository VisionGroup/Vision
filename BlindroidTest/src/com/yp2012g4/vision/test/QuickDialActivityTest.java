package com.yp2012g4.vision.test;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageButton;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.QuickDialActivity;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;

/**
 * Tests for QuickDialActivity
 * 
 * @author Amir
 * @version 1.0
 */
public class QuickDialActivityTest extends
		ActivityInstrumentationTestCase2<QuickDialActivity> {
	private Solo solo;
	private Activity activity;

	
	public QuickDialActivityTest() {
		super("com.yp2012g4.vision", QuickDialActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}

	public void testNumOfButtons() {
		solo.assertCurrentActivity("Check on first activity",
				QuickDialActivity.class);
		ArrayList<Button> btnList = solo.getCurrentButtons();
		assertEquals(9, btnList.size());
	}

	public void testNumOfImageButtons() {
		solo.assertCurrentActivity("Check on first activity",
				QuickDialActivity.class);
		ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
		assertEquals(4, btnList.size());
	}

	public void testClickOnBackButton() {
		solo.assertCurrentActivity("Check on first activity",
				QuickDialActivity.class);
		ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
		TalkingImageButton btn = (TalkingImageButton) btnList.get(0);
		assertTrue(btn.getId() == R.id.back_button);
		solo.clickOnImageButton(0);
		solo.assertCurrentActivity("Back to main screen",
				QuickDialActivity.class);

	}

	public void testContactsText() {
		String[] string = { "Amit Yaffe", "Yaron Auster", "Roman Gurevitch" };
		ArrayList<Button> btnList = solo.getCurrentButtons();
		for (int i = 0; i < 2; i++) {
			TalkingButton btn = (TalkingButton) btnList.get(i);
			assertTrue(btn.getText().toString().equals(string[i]));
		}

	}
}