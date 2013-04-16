package com.yp2012g4.vision.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.DialScreen;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.tools.VisionActivity;

public class ContactsMenuActivity extends VisionActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contacts_menu);
    init(0, getString(R.string.title_activity_contacts_menu), getString(R.string.contacts_help));
  }
  
  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    Intent intent;
    View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.contactsListButton:
        intent = new Intent(ContactsMenuActivity.this, ContactsActivity.class);
        intent.putExtra(ContactsActivity.LIST_TYPE, "all");
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.dialerButton:
        intent = new Intent(ContactsMenuActivity.this, DialScreen.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.quickDialButton:
        intent = new Intent(ContactsMenuActivity.this, ContactsActivity.class);
        intent.putExtra(ContactsActivity.LIST_TYPE, "favorits");
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      default:
        break;
    }
    return false;
  }
  
  @Override
  public int getViewId() {
    return R.id.ContactsMenuActivity;
  }
}
