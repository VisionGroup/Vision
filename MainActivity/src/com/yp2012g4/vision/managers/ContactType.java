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
  public final String phone;
  public final String name;
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param c
   */
  public ContactType(final Cursor cur, final Context c) {
    name = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
    final String lookUpKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
    phone = new ContactManager(c).lookupPhoneNumbers(lookUpKey);
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
    phone = ps;
    name = cs;
  }
}