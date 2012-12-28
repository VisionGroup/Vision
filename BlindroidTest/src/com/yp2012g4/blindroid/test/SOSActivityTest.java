package com.yp2012g4.blindroid.test;

import java.util.Map;

import android.app.Activity;
import android.graphics.Rect;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.SOSActivity;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.onTouchEventClass;

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
		solo = new Solo(getInstrumentation(), activity);
	}

	public void testSOSButton(){
		solo.assertCurrentActivity("Check on first activity", SOSActivity.class);
		TalkingImageButton sos = (TalkingImageButton)activity.findViewById(R.id.Send_SOS_Message);
		assertTrue(sos.isShown());
		for (Map.Entry<TalkingImageButton, Rect> entry : ((onTouchEventClass) activity).getImageButton_to_rect().entrySet()){
			if (entry.getKey().equals(sos)){
				assertEquals(entry.getKey(), sos);
			}
			else assertFalse(entry.getKey().equals(sos));
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
