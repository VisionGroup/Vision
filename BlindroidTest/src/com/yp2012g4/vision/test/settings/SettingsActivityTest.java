package com.yp2012g4.vision.test.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.VisionApplication;
import com.yp2012g4.vision.apps.settings.SettingsActivity;
import com.yp2012g4.vision.apps.settings.SetupSettingsString;

/**
 * 
 * @author Amit
 * 
 */
public class SettingsActivityTest extends ActivityInstrumentationTestCase2<SettingsActivity> {
  private Solo solo;
  private Activity activity;
  
  public SettingsActivityTest() {
    super("com.yp2012g4.vision.apps.settings", SettingsActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  @MediumTest public void testMute() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    boolean mute = VisionApplication.muted;
    solo.clickOnView(solo.getView(R.id.Mute_Sound));
    assertEquals(!mute, VisionApplication.muted);
    solo.clickOnView(solo.getView(R.id.Mute_Sound));
    assertEquals(mute, VisionApplication.muted);
  }
  
  @MediumTest public void testChangeLocale() {
    solo.assertCurrentActivity("wrong activity", SettingsActivity.class);
    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
    final boolean enable = sp.getBoolean(SetupSettingsString.VisionCallEnableEntry, true);
    solo.clickOnView(solo.getView(R.id.vision_call_enable_button));
    assertEquals(!enable, sp.getBoolean(SetupSettingsString.VisionCallEnableEntry, true));
  }
}
