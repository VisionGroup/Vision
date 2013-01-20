package com.yp2012g4.blindroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.yp2012g4.blindroid.tools.BlindroidActivity;

public class ContactsMenuActivity extends BlindroidActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contacts_menu);
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    Intent intent;
    switch (curr_view.getId()) {
      case R.id.contactsListButton:
        break;
      case R.id.dialerButton:
        intent = new Intent(ContactsMenuActivity.this, DialScreen.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.quickDialButton:
        intent = new Intent(ContactsMenuActivity.this, QuickDialActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
    }
    return false;
  }
  
  @Override public int getViewId() {
    return R.id.ContactsMenuActivity;
  }
}
