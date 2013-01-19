package com.yp2012g4.blindroid;

import com.yp2012g4.blindroid.tools.BlindroidActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class ContactsMenuActivity extends BlindroidActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contacts_menu);
  }
    
  @Override
  public void onClick(View v) {
    super.onClick(v);
    Intent intent;
    switch (v.getId()) {
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
  }
  
  @Override public int getViewId() {
    return R.id.ContactsMenuActivity;
  }
  
}
