package com.yp2012g4.blindroid.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.QuickSMSActivity;

public class QuickSMSActivityTest extends
		ActivityInstrumentationTestCase2<QuickSMSActivity> {
	private Solo solo;
	private Activity activity;
	
	public QuickSMSActivityTest() {
		super("com.yp2012g4.blindroid", QuickSMSActivity.class);
		// TODO Auto-generated constructor stub
	}

	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}
	
	public void testDialogBox(){
		solo.assertCurrentActivity("Check on first activity",
				QuickSMSActivity.class);
//		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		solo.clickOnButton("Will call you back");
		}

}
