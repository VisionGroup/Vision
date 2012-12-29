package com.yp2012g4.blindroid.test;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageButton;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.QuickDialActivity;
import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

public class QuickDialActivityTest extends
		ActivityInstrumentationTestCase2<QuickDialActivity> {
	private Solo solo;
	private Activity activity;

	public QuickDialActivityTest() {
		super("com.yp2012g4.blindroid", QuickDialActivity.class);
	}

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
//		TalkingImageButton back = (TalkingImageButton)activity.findViewById(R.id.back_button);
		ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
		 TalkingImageButton btn = (TalkingImageButton) btnList.get(0);
		 assertTrue(btn.getId()==R.id.back_button);
		solo.clickOnImageButton(0);
		solo.assertCurrentActivity("Back to main screen", QuickDialActivity.class);
//		TalkingImageButton back = (TalkingImageButton)activity.findViewById(R.id.back_button);
		

	}

}