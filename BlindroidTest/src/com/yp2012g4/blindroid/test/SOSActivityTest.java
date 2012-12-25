package com.yp2012g4.blindroid.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.SOSActivity;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

public class SOSActivityTest extends
		ActivityInstrumentationTestCase2<SOSActivity> {
	private Solo solo;
	private Activity activity;

	public SOSActivityTest() {
		super("com.yp2012g4.blindroid", SOSActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(),activity);
	}
	
	public void testSOSButtonClick(){
		solo.assertCurrentActivity("Check on first activity", SOSActivity.class);
		TalkingImageButton sos = (TalkingImageButton)activity.findViewById(R.id.Send_SOS_Message);
		assertTrue(sos.isShown());
//		solo.clickOnButton(0);
//		solo.assertCurrentActivity("Back to main screen", MainActivity.class);
	}
//
//	protected void tearDown() throws Exception {
//		super.tearDown();
//	}

}
