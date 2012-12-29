package com.yp2012g4.blindroid.test;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageButton;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.QuickSMSActivity;
import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

/**
 * Tests for QuickSMSActivity
 * 
 * @author Amir
 * @version 1.0
 */
public class QuickSMSActivityTest extends
		ActivityInstrumentationTestCase2<QuickSMSActivity> {
	private Solo solo;
	private Activity activity;

	public QuickSMSActivityTest() {
		super("com.yp2012g4.blindroid", QuickSMSActivity.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}

	public void testNumOfButtons() {
		solo.assertCurrentActivity("Check on first activity",
				QuickSMSActivity.class);
		ArrayList<Button> btnList = solo.getCurrentButtons();
		assertEquals(9, btnList.size());
	}

	public void testNumOfImageButtons() {
		solo.assertCurrentActivity("Check on first activity",
				QuickSMSActivity.class);
		ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
		assertEquals(4, btnList.size());
	}

	public void testClickOnBackButton() {
		solo.assertCurrentActivity("Check on first activity",
				QuickSMSActivity.class);
		ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
		TalkingImageButton btn = (TalkingImageButton) btnList.get(0);
		assertTrue(btn.getId() == R.id.back_button);
		solo.clickOnImageButton(0);
		solo.assertCurrentActivity("Back to main screen",
				QuickSMSActivity.class);

	}

	public void testSMSText() {
		String[] string = { "Will call you back", "", "Where have you been?",
				"", "See you tomorrow", "", "Where are you?", "", "Bla Bla Bla" };
		ArrayList<Button> btnList = solo.getCurrentButtons();
		for (int i = 0; i < btnList.size(); i += 2) {
			TalkingButton btn = (TalkingButton) btnList.get(i);
			assertTrue(btn.getText().toString().equals(string[i]));
		}

	}

}
