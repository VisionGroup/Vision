package com.yp2012g4.vision.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageButton;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.sms.QuickSMSActivity;

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
	super("com.yp2012g4.vision.sms", QuickSMSActivity.class);
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
	final ArrayList<Button> btnList = solo.getCurrentButtons();
	assertEquals(8, btnList.size());
    }

    public void testNumOfImageButtons() {
	solo.assertCurrentActivity("Check on first activity",
		QuickSMSActivity.class);
	final ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
	assertEquals(4, btnList.size());
    }

    public void testClickOnBackButton() {
	solo.assertCurrentActivity("Check on first activity",
		QuickSMSActivity.class);
	final ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
	final TalkingImageButton btn = (TalkingImageButton) btnList.get(0);
	assertTrue(btn.getId() == R.id.back_button);
	solo.clickOnImageButton(0);
	solo.assertCurrentActivity("Back to main screen",
		QuickSMSActivity.class);

    }

    public void testSMSText() {
	final String[] string = { activity.getString(R.string.SMS_number_1),
		"", activity.getString(R.string.SMS_number_3), "",
		activity.getString(R.string.SMS_number_5), "",
		activity.getString(R.string.SMS_number_7), "",
		activity.getString(R.string.SMS_number_9) };
	final ArrayList<Button> btnList = solo.getCurrentButtons();
	for (int i = 0; i < btnList.size(); i += 2) {
	    final TalkingButton btn = (TalkingButton) btnList.get(i);
	    // assertTrue(btn.getText().toString().equals(string[i]));
	    assertEquals(string[i], btn.getText().toString());
	}

    }

    public void testToNumberString() {

	final Intent i = new Intent(activity, QuickSMSActivity.class);
	i.putExtra(QuickSMSActivity.NUMBER_KEY, "0578135813");
	activity.startActivity(i);
	solo.assertCurrentActivity("Check on first activity",
		QuickSMSActivity.class);
	// TODO: Check the activity contains the same string as a phone number.
    }

}
