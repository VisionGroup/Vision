package com.yp2012g4.vision.test;

import java.util.Map.Entry;

import android.app.Activity;
import android.graphics.Rect;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.SOSActivity;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.tools.VisionGestureDetector;

/**
 * Tests for SOSActivity
 * 
 * @author Amir
 * @version 1.0
 */
public class SOSActivityTest extends
	ActivityInstrumentationTestCase2<SOSActivity> {
    private Solo solo;
    private Activity activity;

    public SOSActivityTest() {
	super("com.yp2012g4.vision", SOSActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	activity = getActivity();
	solo = new Solo(getInstrumentation(), activity);
    }

    public void testSOSButton() {
	solo.assertCurrentActivity("Check on first activity", SOSActivity.class);
	final TalkingImageButton sos = (TalkingImageButton) activity
		.findViewById(R.id.Send_SOS_Message);
	assertTrue(sos.isShown());
	for (final Entry<View, Rect> entry : ((VisionGestureDetector) activity)
		.getView_to_rect().entrySet())
	    if (entry.getKey().equals(sos))
		assertEquals(entry.getKey(), sos);
	    else
		assertFalse(entry.getKey().equals(sos));
    }

    @Override
    protected void tearDown() throws Exception {
	super.tearDown();
    }

}