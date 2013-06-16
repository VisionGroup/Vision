package com.yp2012g4.vision.test;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.contacts.AddContactActivity;
import com.yp2012g4.vision.apps.contacts.ContactsActivity;
import com.yp2012g4.vision.apps.contacts.ContactsMenuActivity;
import com.yp2012g4.vision.apps.main.MainActivity;
import com.yp2012g4.vision.apps.smsReader.DeleteConfirmation;
import com.yp2012g4.vision.apps.smsSender.SendSMSActivity;
import com.yp2012g4.vision.customUI.TalkingButton;

public class ContactsActivityTest extends ActivityInstrumentationTestCase2<ContactsActivity> {
  private Solo solo;
  private Activity activity;
  String firstName = "John Doe";
  String firstPhone = "1234567890";
  String secondName = "Anonymous";
  String secondPhone = "000000";
  String thirdPhone = "666";
  
  public ContactsActivityTest() {
    super("com.yp2012g4.vision.apps.contacts", ContactsActivity.class);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    final Intent i = new Intent();
    i.putExtra(ContactsActivity.LIST_TYPE, ContactsActivity.ALL_CONTACTS);
    setActivityIntent(i);
    activity = getActivity();
    solo = new Solo(getInstrumentation(), activity);
  }
  
  @MediumTest public void testAddContact() {
    // try to add a contact without all required fields
    goToAddContact();
    fillAddContactFormAndConfirm(firstName, "");
    solo.assertCurrentActivity("wrong activity", AddContactActivity.class);
    fillAddContactFormAndConfirm("", firstPhone);
    solo.assertCurrentActivity("wrong activity", AddContactActivity.class);
    fillAddContactFormAndConfirm("", "");
    solo.assertCurrentActivity("wrong activity", AddContactActivity.class);
    // Add first contact
    fillAddContactFormAndConfirm(firstName, firstPhone);
    goToContact(true, firstName);
    assertEquals(firstPhone, ((TalkingButton) solo.getView(R.id.contact_phone)).getText());
    // Add second contact
    goToAddContact();
    fillAddContactFormAndConfirm(secondName, secondPhone);
    goToContact(true, secondName);
    assertEquals(secondPhone, ((TalkingButton) solo.getView(R.id.contact_phone)).getText());
    // try to add a contact with the same name
    goToAddContact();
    fillAddContactFormAndConfirm(firstName, thirdPhone);
    goToContact(true, firstName);
    assertEquals(firstPhone, ((TalkingButton) solo.getView(R.id.contact_phone)).getText());
  }
  
//this test should run after testAddContact() - because I assume the
  // 2 contacts were added successfully
  @MediumTest public void testDeleteContacts() {
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    goToContact(true, firstName);
    deleteCurrentContact(false);
    assertEquals(firstName, ((TalkingButton) solo.getView(R.id.contact_name)).getText());
    deleteCurrentContact(true);
    assertFalse(firstName.equals(((TalkingButton) solo.getView(R.id.contact_name)).getText()));
  }
  
  // this test should run after testDeleteContacts() - because I assume the
  // first added contact has been deleted
  @MediumTest public void testEditContacts() {
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    // first we change the phone number
    goToContact(true, secondName);
    goToEditContact();
    fillAddContactFormAndConfirm(null, thirdPhone);
    goToContact(true, secondName);
    assertEquals(thirdPhone, ((TalkingButton) solo.getView(R.id.contact_phone)).getText());
    // secondly we change the contact name
    goToEditContact();
    fillAddContactFormAndConfirm(firstName, null);
    goToContact(true, firstName);
    assertEquals(thirdPhone, ((TalkingButton) solo.getView(R.id.contact_phone)).getText());
    deleteCurrentContact(true);
  }
  
  private void goToContact(boolean exist, String name) {
    goToStart();
    assertEquals(exist, findContact(name));
  }
  
  private void fillAddContactFormAndConfirm(String name, String phone) {
    if (name != null) {
      EditText nameText = (EditText) solo.getView(R.id.contact_name);
      solo.clearEditText(nameText);
      solo.enterText(nameText, name);
    }
    if (phone != null) {
      EditText phoneText = (EditText) solo.getView(R.id.phoneNumber);
      solo.clearEditText(phoneText);
      solo.enterText(phoneText, phone);
    }
    solo.clickOnView(solo.getView(R.id.confirmation_button));
  }
  
  @MediumTest private void goToAddContact() {
    // Go into contact configuration screen by clicking the add contact button
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    solo.clickOnView(solo.getView(R.id.add_contact));
    solo.assertCurrentActivity("wrong activity", AddContactActivity.class);
  }
  
  @MediumTest private void goToEditContact() {
    // Go into contact configuration screen by clicking the add contact button
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    solo.clickOnView(solo.getView(R.id.edit_contact));
    solo.assertCurrentActivity("wrong activity", AddContactActivity.class);
  }
  
  @MediumTest private void deleteCurrentContact(boolean confirmDelete) {
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    solo.clickOnView(solo.getView(R.id.delete_contact));
    solo.assertCurrentActivity("wrong activity", DeleteConfirmation.class);
    if (confirmDelete)
      flingRight(this);
    else
      solo.clickOnView(solo.getView(R.id.Delete_Confirmation_Button));
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
  }
  
  @MediumTest public void testSendSmsToContact() throws Exception {
    goToAddContact();
    fillAddContactFormAndConfirm(secondName, secondPhone);
    goToContact(true, secondName);
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    solo.clickOnView(solo.getView(R.id.contacts_sms));
    solo.assertCurrentActivity("wrong activity", SendSMSActivity.class);
    solo.clickOnView(solo.getView(R.id.phoneNumber));
    assertEquals(((EditText) solo.getView(R.id.phoneNumber)).getText().toString(), secondPhone);
    solo.goBack();
//TODO: Do we really want to send SMS in test?!?!
//    try {
//      solo.clickOnView(solo.getView(R.id.sendMessageButton));
//    } catch (Error e) {
//      Log.i("MyLog", VisionGestureDetector._spokenString);
//      assertEquals(VisionGestureDetector._spokenString, "Message was not sent. Please check reception or sim card.");
//      return;
//    }
//    assertEquals(VisionGestureDetector._spokenString, "Message has been sent");
    solo.clickOnView(solo.getView(R.id.home_button));
    solo.assertCurrentActivity("wrong activity", MainActivity.class);
    solo.clickOnView(solo.getView(R.id.contacts_button));
    solo.assertCurrentActivity("wrong activity", ContactsMenuActivity.class);
    solo.clickOnView(solo.getView(R.id.contactsListButton));
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    goToContact(true, secondName);
    assertEquals(secondPhone, ((TalkingButton) solo.getView(R.id.contact_phone)).getText());
    deleteCurrentContact(true);
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
  
  // fling left until the start of the list
  @MediumTest public void goToStart() {
    solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
    String lastContact = (String) ((TalkingButton) solo.getView(R.id.contact_name)).getText();
    String currContact = lastContact;
    do {
      lastContact = currContact;
      flingLeft(this);
      currContact = (String) ((TalkingButton) solo.getView(R.id.contact_name)).getText();
    } while (!currContact.equals(lastContact));
  }
  
  public boolean findContact(String name) {
    String lastContact = (String) ((TalkingButton) solo.getView(R.id.contact_name)).getText();
    if (lastContact.equals(name))
      return true;
    flingRight(this);
    String currContact = (String) ((TalkingButton) solo.getView(R.id.contact_name)).getText();
    while (!currContact.equals(lastContact)) {
      if (currContact.equals(name))
        return true;
      lastContact = currContact;
      flingRight(this);
      currContact = (String) ((TalkingButton) solo.getView(R.id.contact_name)).getText();
    }
    return false;
  }
}
