package com.yp2012g4.vision.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.ImageButton;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsSender.QuickSMSActivity;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.tools.CallUtils;

/**
 * Tests for QuickSMSActivity
 * 
 * @author Amir
 * @version 1.0
 */
public class QuickSMSActivityTest extends ActivityInstrumentationTestCase2<QuickSMSActivity> {
  private Solo solo;
  private Activity activity;
  
  public QuickSMSActivityTest() {
    super("com.yp2012g4.vision.apps.smsSender", QuickSMSActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
    solo.assertCurrentActivity("Check on first activity", QuickSMSActivity.class);
  }
  
  @MediumTest public void testNumOfButtons() {
    final ArrayList<Button> btnList = solo.getCurrentButtons();
    assertEquals(8, btnList.size());
  }
  
  @MediumTest public void testNumOfImageButtons() {
    final ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
    assertEquals(4, btnList.size());
  }
  
  @MediumTest public void testClickOnBackButton() {
    final ArrayList<ImageButton> btnList = solo.getCurrentImageButtons();
    final TalkingImageButton btn = (TalkingImageButton) btnList.get(0);
    assertTrue(btn.getId() == R.id.back_button);
    solo.clickOnImageButton(0);
    solo.assertCurrentActivity("Back to main screen", QuickSMSActivity.class);
  }
  
  @MediumTest public void testSMSText() {
    final String[] string = { activity.getString(R.string.SMS_number_1), "", activity.getString(R.string.SMS_number_3), "",
        activity.getString(R.string.SMS_number_5), "", activity.getString(R.string.SMS_number_7), "",
        activity.getString(R.string.SMS_number_9) };
    final ArrayList<Button> btnList = solo.getCurrentButtons();
    for (int i = 0; i < btnList.size(); i += 2) {
      final TalkingButton btn = (TalkingButton) btnList.get(i);
      assertEquals(string[i], btn.getText().toString());
    }
  }
  
  @MediumTest public void testToNumberString() {
    final Intent i = new Intent(activity, QuickSMSActivity.class);
    i.putExtra(CallUtils.NUMBER_KEY, "0578135813");
    activity.startActivity(i);
    solo.assertCurrentActivity("Check on first activity", QuickSMSActivity.class);
    final Bundle extras = solo.getCurrentActivity().getIntent().getExtras();
    assertEquals(extras.getString(CallUtils.NUMBER_KEY), "0578135813");
  }
}
