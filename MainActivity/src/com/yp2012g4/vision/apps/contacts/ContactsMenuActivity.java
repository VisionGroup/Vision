package com.yp2012g4.vision.apps.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.tools.VisionActivity;

public class ContactsMenuActivity extends VisionActivity {
  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contacts_menu);
    init(0, getString(R.string.title_activity_contacts_menu), getString(R.string.contacts_help));
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    Intent intent;
    final View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.contactsListButton:
        intent = new Intent(ContactsMenuActivity.this, ContactsActivity.class);
        intent.putExtra(ContactsActivity.LIST_TYPE, ContactsActivity.ALL_CONTACTS);
        setIntentFlags(intent);
        startActivity(intent);
        break;
      case R.id.dialerButton:
        intent = new Intent(ContactsMenuActivity.this, DialScreen.class);
        setIntentFlags(intent);
        startActivity(intent);
        break;
      case R.id.quickDialButton:
        intent = new Intent(ContactsMenuActivity.this, ContactsActivity.class);
        intent.putExtra(ContactsActivity.LIST_TYPE, ContactsActivity.FAVORITS_CONTACTS);
        setIntentFlags(intent);
        startActivity(intent);
        break;
      default:
        break;
    }
    return false;
  }
  
  @Override public int getViewId() {
    return R.id.ContactsMenuActivity;
  }
}
