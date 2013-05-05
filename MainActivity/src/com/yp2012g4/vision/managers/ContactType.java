package com.yp2012g4.vision.managers;

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
  private String lookUpKey = "";
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param context
   */
  public ContactType(Cursor cur, Context context) {
    contactName = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)).toString();
    ContactManager cm = new ContactManager(context);
    lookUpKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)).toString();
    phone = cm.lookupPhoneNumbers(lookUpKey);
  }
  
  /**
   * constructor with fields
   * 
   * @param phone1
   * @param contactName1
   */
  public ContactType(String phone1, String contactName1) {
    phone = phone1;
    contactName = contactName1;
  }
  
  public synchronized String getPhone() {
    return phone;
  }
  
  public synchronized String getContactName() {
    return contactName;
  }
  
  public synchronized String getLookUpKey() {
    return lookUpKey;
  }
}