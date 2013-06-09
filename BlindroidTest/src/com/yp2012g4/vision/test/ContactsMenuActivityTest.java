package com.yp2012g4.vision.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.Suppress;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.contacts.ContactsActivity;
import com.yp2012g4.vision.apps.contacts.ContactsMenuActivity;
import com.yp2012g4.vision.apps.contacts.DialScreen;

public class ContactsMenuActivityTest extends ActivityInstrumentationTestCase2<ContactsMenuActivity> {
  private Solo solo;
  private Activity activity;
  
  public ContactsMenuActivityTest() {
    super("com.yp2012g4.vision.apps.contacts", ContactsMenuActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  /**
   * Basic test to test liveliness of the list with contacts from Android
   */
  @MediumTest public void testAllContactsAlive() {
    solo.assertCurrentActivity("wrong activity", ContactsMenuActivity.class);
    solo.clickOnView(solo.getView(R.id.quickDialButton));
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    ContactsActivityTest.flingRight(this);
    ContactsActivityTest.flingLeft(this);
    solo.clickOnView(solo.getView(R.id.back_button));
    solo.assertCurrentActivity("wrong activity", ContactsMenuActivity.class);
  }
  
  /**
   * Basic test to test liveliness of the list with contacts from Android
   */
  @MediumTest @Suppress public void testFavoritsContactsAlive() {
    solo.assertCurrentActivity("wrong activity", ContactsMenuActivity.class);
    solo.clickOnView(solo.getView(R.id.contactsListButton));
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    ContactsActivityTest.flingRight(this);
    ContactsActivityTest.flingLeft(this);
    solo.clickOnView(solo.getView(R.id.back_button));
    solo.assertCurrentActivity("wrong activity", ContactsMenuActivity.class);
  }
  
  /**
   * Basic test to test liveliness of Dial screen.
   */
  @MediumTest public void testDialerAlive() {
    solo.assertCurrentActivity("wrong activity", ContactsMenuActivity.class);
    solo.clickOnView(solo.getView(R.id.dialerButton));
    solo.assertCurrentActivity("wrong activity", DialScreen.class);
    solo.clickOnView(solo.getView(R.id.back_button));
    solo.assertCurrentActivity("wrong activity", ContactsMenuActivity.class);
  }
}
