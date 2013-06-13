package com.yp2012g4.vision.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.contacts.ContactsActivity;
import com.yp2012g4.vision.apps.smsSender.QuickSMSActivity;
import com.yp2012g4.vision.apps.smsSender.SendSMSActivity;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.managers.ContactManager;
import com.yp2012g4.vision.managers.ContactType;
import com.yp2012g4.vision.tools.CallUtils;

public class ContactsActivityTest extends ActivityInstrumentationTestCase2<ContactsActivity> {
  private Solo solo;
  private Activity activity;
  
  public ContactsActivityTest() {
    super("com.yp2012g4.vision.apps.contacts", ContactsActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    final Intent i = new Intent();
    i.putExtra(ContactsActivity.LIST_TYPE, "test");
    setActivityIntent(i);
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  /**
   * Test the Contacts activity functionality
   */
  @MediumTest public void testContactActivity() {
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    // check intent
    final Bundle extras = solo.getCurrentActivity().getIntent().getExtras();
    String listType = "";
    if (extras != null)
      try {
        listType = extras.getString(ContactsActivity.LIST_TYPE);
      } catch (final Exception e) {
        listType = "";
      }
    assertEquals("test", listType);
    // check info
    final TalkingButton name = (TalkingButton) solo.getView(R.id.contact_name);
    final TalkingButton phone = (TalkingButton) solo.getView(R.id.contact_phone);
    ContactManager cm = new ContactManager(activity);
    cm.getTestContacts();
    assertEquals(cm.getContact(0).getContactName(), name.getText());
    assertEquals(cm.getContact(0).getPhone(), phone.getText());
    flingRight(this);
    assertEquals(cm.getContact(1).getContactName(), name.getText());
    assertEquals(cm.getContact(1).getPhone(), phone.getText());
    flingRight(this);
    assertEquals(cm.getContact(1).getContactName(), name.getText());
    assertEquals(cm.getContact(1).getPhone(), phone.getText());
    flingLeft(this);
    assertEquals(cm.getContact(0).getContactName(), name.getText());
    assertEquals(cm.getContact(0).getPhone(), phone.getText());
    flingLeft(this);
    assertEquals(cm.getContact(0).getContactName(), name.getText());
    assertEquals(cm.getContact(0).getPhone(), phone.getText());
    flingRight(this);
    solo.clickOnView(solo.getView(R.id.contacts_quick_sms));
    // test passing correct number to Quick SMS activity
    solo.assertCurrentActivity("wrong activity", QuickSMSActivity.class);
    final Bundle extras2 = solo.getCurrentActivity().getIntent().getExtras();
    String smsExtra = "";
    if (extras2 != null)
      try {
        smsExtra = extras2.getString(CallUtils.NUMBER_KEY);
      } catch (final Exception e) {
        smsExtra = "";
      }
    assertEquals(cm.getContact(1).getPhone(), smsExtra);
  }
  
  /**
   * Test contact manager
   */
  @MediumTest public void testManager() {
    final ContactManager cm = new ContactManager(activity.getApplicationContext());
    // should not contain empty contacts or phone numbers.
    cm.getAllContacts();
    for (int i = 0; i < cm.getNumOfContacts(); i++) {
      ContactType contactType = cm.getContact(i);
      assertTrue(contactType.getContactName() != "");
      if (contactType.getPhone() != "")
        assertEquals(contactType.getContactName(), cm.getNameFromPhone(contactType.getPhone()));
      // assertTrue(contactType.getPhone() != "");
      if (contactType.getPhone() != "")
        assertEquals(contactType.getPhone(), cm.lookupPhoneNumbers(contactType.getLookUpKey()));
    }
    // should not contain empty contacts or phone numbers.
    cm.getFavoriteContacts();
    for (int i = 0; i < cm.getNumOfContacts(); i++) {
      ContactType contactType = cm.getContact(i);
      assertTrue(contactType.getContactName() != "");
      assertEquals(contactType.getContactName(), cm.getNameFromPhone(contactType.getPhone()));
      assertTrue(contactType.getPhone() != "");
      assertEquals(contactType.getPhone(), cm.lookupPhoneNumbers(contactType.getLookUpKey()));
    }
  }
  
  @MediumTest public void testSendSmsToContact() throws Exception {
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    solo.clickOnView(solo.getView(R.id.contacts_sms));
    solo.assertCurrentActivity("wrong activity", SendSMSActivity.class);
    solo.clickOnView(solo.getView(R.id.phoneNumber));
    final Bundle extras = solo.getCurrentActivity().getIntent().getExtras();
    assertEquals(((EditText) solo.getView(R.id.phoneNumber)).getText().toString(), extras.getString(CallUtils.NUMBER_KEY));
//TODO: check why "click cannot be completed" error occurs????
    // try {
//      solo.clickOnView(solo.getView(R.id.sendMessageButton));
//    } catch (Error e) {
//      Log.i("MyLog", VisionGestureDetector._spokenString);
//      assertEquals(VisionGestureDetector._spokenString, "Message was not sent. Please check reception or sim card.");
//      return;
//    }
//    assertEquals(VisionGestureDetector._spokenString, "Message has been sent");
  }
  
  public static void flingRight(ActivityInstrumentationTestCase2<?> c) {
    final int screenHeight = c.getActivity().getWindowManager().getDefaultDisplay().getHeight();
    final int screenWidth = c.getActivity().getWindowManager().getDefaultDisplay().getWidth();
    TouchUtils.drag(c, screenWidth / 2, screenWidth / 2 - 150, screenHeight / 2, screenHeight / 2, 20);
  }
  
  public static void flingLeft(ActivityInstrumentationTestCase2<?> c) {
    final int screenHeight = c.getActivity().getWindowManager().getDefaultDisplay().getHeight();
    final int screenWidth = c.getActivity().getWindowManager().getDefaultDisplay().getWidth();
    TouchUtils.drag(c, screenWidth / 2, screenWidth / 2 + 150, screenHeight / 2, screenHeight / 2, 20);
  }
}
