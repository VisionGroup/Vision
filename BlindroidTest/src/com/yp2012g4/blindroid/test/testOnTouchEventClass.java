package com.yp2012g4.blindroid.test;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Rect;
import android.test.ActivityInstrumentationTestCase2;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.onTouchEventClass;

public class testOnTouchEventClass extends
		ActivityInstrumentationTestCase2<onTouchEventClass> {
	private Map<TalkingButton, Rect> button_to_rect;
	private Map<TalkingImageButton, Rect> imageButton_to_rect;

	public testOnTouchEventClass() {
		super("com.yp2012g4.blindroid.utils", onTouchEventClass.class);
		// TODO Auto-generated constructor stub
	}

	protected void setUp() throws Exception {
		super.setUp();
		button_to_rect = new HashMap<TalkingButton, Rect>();
		imageButton_to_rect = new HashMap<TalkingImageButton, Rect>();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testEmptyHashMap(){
		assertTrue(button_to_rect.isEmpty());
		assertTrue(imageButton_to_rect.isEmpty());
	}
	
	public void testGetButtonsPosition(){
	    Rect rect1 = new Rect(0,0,10,10);
	    Rect rect2 = new Rect(10,0,20,10);
	    Rect rect3 = new Rect(0,10,10,20);
	    TalkingButton b1 = null,b2 = null,b3 = null;
	    button_to_rect.put(b1, rect1);
	    button_to_rect.put(b2, rect2);
	    button_to_rect.put(b3, rect3);
	    assertTrue(button_to_rect.size()==3);
	    

	}

}
