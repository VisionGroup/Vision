package com.yp2012g4.vision;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.managers.ContactManager;
import com.yp2012g4.vision.managers.ContactType;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * This class is an activity that enables to send a quick SMS message
 * 
 * @author Roman
 * @version 1.1
 */
public class ContactsActivity extends VisionActivity {
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
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    super.onSingleTapUp(e);
    switch (curr_view.getId()) {
      case R.id.contact_next:
        if (currentContact < contacts.size() - 1) {
          currentContact++;
          setContact();
        } else {
          speakOut("no more contacts");
        }
        vb.vibrate(VIBRATE_TIME);
        break;
      case R.id.contact_prev:
        if (currentContact > 0) {
          currentContact--;
          setContact();
        } else {
          speakOut("no more contacts");
        }
        vb.vibrate(VIBRATE_TIME);
        break;
      case R.id.contacts_call:
        Intent call = new Intent(Intent.ACTION_CALL);
        call.setData(Uri.parse("tel:" + contacts.get(currentContact).getPhone()));
        startActivity(call);
        break;
      case R.id.contacts_sms:
        Intent sms = new Intent(ContactsActivity.this, QuickSMSActivity.class);
        sms.putExtra(QuickSMSActivity.NUMBER_KEY, contacts.get(currentContact).getPhone());
        startActivity(sms);
        break;
    }
    return false;
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contacts);
    init(0,"Contacts screen","Touch on screen to select contact with next and previous buttons... Call or send message to current contact");
    ContactManager contactManager = new ContactManager(getApplicationContext());
    final Bundle extras = getIntent().getExtras();
    String listType = "all";
    if (extras != null)
      try {
        listType = extras.getString(ContactsActivity.LIST_TYPE);
      } catch (final Exception e) {
        listType = "all";
      }
    if (listType.equalsIgnoreCase("all")) {
      contacts = contactManager.getAllContacts();
      findViewById(getViewId()).setContentDescription("Contact list screen");
    } else if (listType.equalsIgnoreCase("favorits")){
      contacts = contactManager.getFavoriteContacts();
      findViewById(getViewId()).setContentDescription("Favorit contacts screen");
    }
    setContact();
  }
  
//  @Override public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.activity_read_sms, menu);
//    return true;
//  }
  
  private void setContact() {
    TalkingButton contactNameButton = (TalkingButton) findViewById(R.id.contact_name);
    TalkingButton conactPhoneButton = (TalkingButton) findViewById(R.id.contact_phone);
    TalkingImageButton callPhoneButton = (TalkingImageButton) findViewById(R.id.contacts_call);
    TalkingImageButton smsPhoneButton = (TalkingImageButton) findViewById(R.id.contacts_sms);
    if (contacts.size() != 0) {
      currentName = contacts.get(currentContact).getContactName();
      currentPhone = contacts.get(currentContact).getPhone();
      callPhoneButton.setReadText("Call " + currentName);
      smsPhoneButton.setReadText("Send quick sms to " + currentName);
      contactNameButton.setText(currentName);
      contactNameButton.setReadText(currentName);
      conactPhoneButton.setText(currentPhone);
      conactPhoneButton.setReadText(currentPhone);
      speakOut(currentName);
    } else {
      speakOut("No messages");
    }
  }
}