/***
 * @author Olivier Hofman
 * @version 1.0
 */
package com.yp2012g4.vision.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.main.MainActivity;
import com.yp2012g4.vision.apps.whereAmI.WhereAmIActivity;

public class WhereAmITest extends ActivityInstrumentationTestCase2<WhereAmIActivity> {
  private Solo solo;
  private Activity activity;
  
  public WhereAmITest() {
    super("com.yp2012g4.vision.apps.whereAmI", WhereAmIActivity.class);
    // TODO Auto-generated constructor stub
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  public void testSetUp() {
    // Just to check it doesn't crash
  }
  
  @MediumTest public void testClickOnTooltip() {
    solo.assertCurrentActivity("wrong activity", WhereAmIActivity.class);
    solo.clickOnView(solo.getView(R.id.tool_tip_button));
    solo.clickOnView(solo.getView(R.id.current_menu_button));
    solo.assertCurrentActivity("wrong activity", WhereAmIActivity.class);
  }
  
  @MediumTest public void testClickOnHomeButton() {
    solo.assertCurrentActivity("wrong activity", WhereAmIActivity.class);
    solo.clickOnView(solo.getView(R.id.home_button));
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
  }
}
