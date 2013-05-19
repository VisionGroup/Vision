package com.yp2012g4.vision.apps.contacts;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsSender.QuickSMSActivity;
import com.yp2012g4.vision.apps.smsSender.SendSMSActivity;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.managers.CallManager;
import com.yp2012g4.vision.managers.ContactManager;
import com.yp2012g4.vision.managers.ContactType;
import com.yp2012g4.vision.tools.CallUtils;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * This class is an activity that enables to send a quick SMS message
 * 
 * @author Roman
 * @version 1.1
 */
public class ContactsActivity extends VisionActivity {
  public static final String FAVORITS_CONTACTS = "favorits";
  public static final String ALL_CONTACTS = "all";
  public static final String LIST_TYPE = "list_type";
  TalkingButton b;
  private final int VIBRATE_TIME = 100;
  private ArrayList<ContactType> contacts;
  private int currentContact = 0;
  private String currentName = "";
  private String currentPhone = "";
  
  @Override public int getViewId() {
    return R.id.ContactsActivity;
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.contact_next:
        if (currentContact < contacts.size() - 1) {
          currentContact++;
          setContact();
        } else
          speakOutAsync(getString(R.string.no_more_contacts));
        vibrate(VIBRATE_TIME);
        break;
      case R.id.contact_prev:
        if (currentContact > 0) {
          currentContact--;
          setContact();
        } else
          speakOutAsync(getString(R.string.no_more_contacts));
        vibrate(VIBRATE_TIME);
        break;
      case R.id.contacts_call:
        final Intent call = new Intent(Intent.ACTION_CALL);
        call.setData(Uri.parse("tel:" + contacts.get(currentContact).getPhone()));
        startActivity(call);
        CallManager.DeleteCallLogByNumber(this, contacts.get(currentContact).getPhone());
        break;
      case R.id.contacts_sms:
        final Intent _sms = new Intent(ContactsActivity.this, SendSMSActivity.class);
        _sms.putExtra(CallUtils.NUMBER_KEY, contacts.get(currentContact).getPhone());
        startActivity(_sms);
        break;
      case R.id.contacts_quick_sms:
        final Intent _quickSms = new Intent(ContactsActivity.this, QuickSMSActivity.class);
        _quickSms.putExtra(CallUtils.NUMBER_KEY, contacts.get(currentContact).getPhone());
        startActivity(_quickSms);
        break;
      default:
        break;
    }
    return false;
  }
  
  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contacts);
    init(0, getString(R.string.contacts_screen), getString(R.string.contacts_screen_help));
    final Bundle extras = getIntent().getExtras();
    String listType = ALL_CONTACTS;
    if (extras != null)
      try {
        listType = extras.getString(ContactsActivity.LIST_TYPE);
      } catch (final Exception e) {
        listType = ALL_CONTACTS;
      }
    selectCorrespondingContactsList(listType);
    setContact();
  }
  
  private void selectCorrespondingContactsList(final String listType) {
    final ContactManager contactManager = new ContactManager(getApplicationContext());
    if (listType.equalsIgnoreCase(ALL_CONTACTS)) {
      contacts = contactManager.getAllContacts();
      findViewById(getViewId()).setContentDescription(getString(R.string.contact_list_screen));
      return;
    }
    if (listType.equalsIgnoreCase(FAVORITS_CONTACTS)) {
      contacts = contactManager.getFavoriteContacts();
      findViewById(getViewId()).setContentDescription(getString(R.string.favorite_list_screen));
      return;
    }
    findViewById(getViewId()).setContentDescription("Test contacts screen");
    contacts = ContactManager.getTestContacts();
  }
  
  private void setContact() {
    final TalkingButton contactNameButton = getTalkingButton(R.id.contact_name);
    final TalkingButton conactPhoneButton = getTalkingButton(R.id.contact_phone);
    final TalkingImageButton callPhoneButton = (TalkingImageButton) findViewById(R.id.contacts_call);
    final TalkingImageButton smsPhoneButton = (TalkingImageButton) findViewById(R.id.contacts_quick_sms);
    if (contacts.size() == 0)
      speakOutAsync(getString(R.string.no_messages));
    else {
      currentName = contacts.get(currentContact).getContactName();
      currentPhone = contacts.get(currentContact).getPhone();
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
