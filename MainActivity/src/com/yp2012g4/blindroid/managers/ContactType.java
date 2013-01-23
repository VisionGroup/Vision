package com.yp2012g4.blindroid.managers;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * SMS type container
 * 
 * @author Roman
 * 
 */
public class ContactType {
  private String phone = "";
  private String contactName = "";
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param context
   */
  public ContactType(Cursor cur, Context context) {
    contactName = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)).toString();
    ContactManager cm = new ContactManager(context);
    String lookup = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)).toString();
    phone = cm.lookupPhoneNumbers(lookup);
  }
  
  public synchronized String getPhone() {
    return phone;
  }
  
  public synchronized String getContactName() {
    return contactName;
  }
}