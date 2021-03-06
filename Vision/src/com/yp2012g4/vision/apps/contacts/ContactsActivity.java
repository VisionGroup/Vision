package com.yp2012g4.vision.apps.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsSender.QuickSMSActivity;
import com.yp2012g4.vision.apps.smsSender.SendSMSActivity;
import com.yp2012g4.vision.apps.telephony.CallUtils;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.managers.ContactManager;
import com.yp2012g4.vision.managers.ContactType;
import com.yp2012g4.vision.tools.DeleteConfirmation;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * This class is an activity that enables to send a quick SMS message
 * 
 * @author Roman
 * @version 1.1
 */
public class ContactsActivity extends VisionActivity {
  public static final String CONTACT_NAME_FLAG = "ContactName";
  public static final String FAVORITS_CONTACTS = "favorits";
  public static final String ALL_CONTACTS = "all";
  public static final String LIST_TYPE = "list_type";
  private static final int REQUEST_CODE = 666;
  private String listType = ALL_CONTACTS;
  private ContactManager contactManager;
  private int currentContact = 0;
  private String currentName = "";
  private String currentPhone = "";
  private static final String TAG = "vision:ContactsActivity";
  
  @Override public int getViewId() {
    return R.id.ContactsActivity;
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View button = getButtonByMode();
    final Intent intent;
    ContactType ct;
    try {
      ct = contactManager.getContact(currentContact);
    } catch (final Exception ex) {
      Log.d(TAG, "Unable to get new contact.");
      ct = null;
    }
    if (ct == null)
      return false;
    switch (button.getId()) {
      case R.id.contacts_call:
        intent = setIntentFlags(new Intent(Intent.ACTION_CALL));
        intent.setData(Uri.parse(CallUtils.CALL_TEL_STRING + ct.phone));
        startActivity(intent);
        break;
      case R.id.contacts_sms:
        intent = newFlaggedIntent(ContactsActivity.this, SendSMSActivity.class);
        intent.putExtra(CallUtils.NUMBER_KEY, ct.phone);
        startActivity(intent);
        break;
      case R.id.contacts_quick_sms:
        intent = newFlaggedIntent(ContactsActivity.this, QuickSMSActivity.class);
        intent.putExtra(CallUtils.NUMBER_KEY, ct.phone);
        intent.putExtra(CONTACT_NAME_FLAG, ct.name);
        startActivity(intent);
        break;
      case R.id.add_contact:
        try {
          intent = newFlaggedIntent(ContactsActivity.this, AddContactActivity.class);
          startActivityForResult(intent, REQUEST_CODE);
        } catch (final Exception ex) {
          Log.d(TAG, "Unable to start add contact activity.");
        }
        break;
      case R.id.edit_contact:
        intent = newFlaggedIntent(ContactsActivity.this, AddContactActivity.class);
        intent.putExtra(CONTACT_NAME_FLAG, ct.name);
        startActivityForResult(intent, REQUEST_CODE);
        break;
      case R.id.delete_contact:
        intent = newFlaggedIntent(this, DeleteConfirmation.class);
        intent.putExtra(DeleteConfirmation.ACTIVITY_EXTRA, this.getClass().getName());
        startActivity(intent);
        break;
      default:
        break;
    }
    return false;
  }
  
  /* 
  *//**
   * This will be called when the add contact is finished
   */
  /*
   * @Override protected void onActivityResult(final int requestCode, final int
   * resultCode, final Intent data) { super.onActivityResult(requestCode,
   * resultCode, data); if (requestCode == REQUEST_CODE) {
   * selectCorrespondingContactsList(); setContact(); } }
   */
  @Override protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);
    final Bundle extras = getIntent().getExtras();
    if (extras != null && extras.getString(ACTION_EXTRA) != null)
      if (extras.getString(ACTION_EXTRA).equals(DeleteConfirmation.DELETE_FLAG)) {
        final ContactType ct = contactManager.getContact(currentContact);
        if (ContactManager.deleteContact(ct.name, this)) {
          selectCorrespondingContactsList();
          setContact();
          speakOutAsync(getString(R.string.delete_contact_success) + ct.name);
        } else
          speakOutAsync(R.string.delete_contact_failed);
        vibrate();
      } else if (extras.getString(ACTION_EXTRA).equals(AddContactActivity.ADD_FLAG)) {
        selectCorrespondingContactsList();
        setContact();
      }
  }
  
  private void changeToPreviousContact() {
    if (currentContact > 0) {
      currentContact--;
      setContact();
    } else
      speakOutAsync(R.string.no_more_contacts);
    vibrate();
  }
  
  private void changeToNextContact() {
    if (currentContact < contactManager.getNumOfContacts() - 1) {
      currentContact++;
      setContact();
    } else
      speakOutAsync(R.string.no_more_contacts);
    vibrate();
  }
  
  @Override public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float f1, final float f2) {
    final float diffX = e2.getX() - e1.getX();
    if (Math.abs(diffX) > Math.abs(e2.getY() - e1.getY()))
      if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(f1) > SWIPE_VELOCITY_THRESHOLD)
        if (diffX > 0)
          changeToPreviousContact();
        else
          changeToNextContact();
    return super.onFling(e1, e2, f1, f2);
  }
  
  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contacts);
    init(0, getString(R.string.contacts_screen), getString(R.string.contacts_screen_help));
    final Bundle extras = getIntent().getExtras();
    if (extras != null)
      try {
        listType = extras.getString(ContactsActivity.LIST_TYPE);
      } catch (final Exception e) {
        listType = ALL_CONTACTS;
      }
    selectCorrespondingContactsList();
    setContact();
  }
  
  private void selectCorrespondingContactsList() {
    contactManager = new ContactManager(getApplicationContext());
    if (listType.equalsIgnoreCase(ALL_CONTACTS))
      contactManager.getAllContacts();
    else
      contactManager.getFavoriteContacts();
    findViewById(getViewId()).setContentDescription(getString(R.string.contact_list_screen));
  }
  
  private void setContact() {
    final TalkingButton contactNameButton = getTalkingButton(R.id.contact_name);
    final TalkingButton conactPhoneButton = getTalkingButton(R.id.contact_phone);
    final TalkingImageButton callPhoneButton = (TalkingImageButton) findViewById(R.id.contacts_call);
    final TalkingImageButton smsPhoneButton = (TalkingImageButton) findViewById(R.id.contacts_quick_sms);
    if (contactManager.getNumOfContacts() == 0) {
      speakOutSync(R.string.no_contacts);
      finish();
    } else {
      final ContactType curContect = contactManager.getContact(currentContact);
      currentName = curContect.name;
      currentPhone = curContect.phone;
      callPhoneButton.setReadText(getString(R.string.call_message) + currentName);
      smsPhoneButton.setReadText(getString(R.string.send_quick_sms_message) + currentName);
      contactNameButton.setText(currentName);
      contactNameButton.setReadText(currentName);
      conactPhoneButton.setText(currentPhone);
      conactPhoneButton.setReadText(currentPhone);
      speakOutAsync(currentName);
    }
  }
}
