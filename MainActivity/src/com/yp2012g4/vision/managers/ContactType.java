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
  private String _phone = "";
  private String _contactName = "";
  private String _lookUpKey = "";
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param c
   */
  public ContactType(final Cursor cur, final Context c) {
    _contactName = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
    final ContactManager cm = new ContactManager(c);
    _lookUpKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
    _phone = cm.lookupPhoneNumbers(_lookUpKey);
  }
  
  /**
   * constructor with fields
   * 
   * @param ps
   *          phone string
   * @param cs
   *          contact name string
   */
  public ContactType(final String ps, final String cs) {
    _phone = ps;
    _contactName = cs;
  }
  
  public synchronized String getPhone() {
    return _phone;
  }
  
  public synchronized String getContactName() {
    return _contactName;
  }
  
  public synchronized String getLookUpKey() {
    return _lookUpKey;
  }
}