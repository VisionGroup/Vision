package com.yp2012g4.vision.managers;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;

public class ContactManager {
  Context c;
  String[] projection = new String[] { BaseColumns._ID, ContactsContract.Contacts.DISPLAY_NAME,
      ContactsContract.Data.TIMES_CONTACTED, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.LOOKUP_KEY };
  
  public ContactManager(Context c) {
    this.c = c;
  }
  
  /**
   * 
   * @return 10 favorite contacts.
   */
  public ArrayList<ContactType> getFavoriteContacts() {
    Uri uri = ContactsContract.Contacts.CONTENT_URI;
    String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
    String[] selectionArgs = null;
    String sortOrder = ContactsContract.Data.TIMES_CONTACTED + " DESC, " + ContactsContract.Contacts.DISPLAY_NAME
        + " COLLATE LOCALIZED ASC LIMIT 10";
    Cursor cur = c.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    ArrayList<ContactType> slist = new ArrayList<ContactType>();
    if (cur.moveToFirst()) {
      do {
        try {
          slist.add(new ContactType(cur, c));
        } catch (IllegalArgumentException e) {
          // Ignore and go next
        }
      } while (cur.moveToNext());
    }
    return slist;
  }
  
  /**
   * 
   * @return all contacts arranged alphabetically
   */
  public ArrayList<ContactType> getAllContacts() {
    Uri uri = ContactsContract.Contacts.CONTENT_URI;
    String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
    String[] selectionArgs = null;
    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
    Cursor cur = c.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    ArrayList<ContactType> slist = new ArrayList<ContactType>();
    if (cur.moveToFirst()) {
      do {
        try {
          slist.add(new ContactType(cur, c));
        } catch (IllegalArgumentException e) {
          // Ignore and go next
        }
      } while (cur.moveToNext());
    }
    return slist;
  }
  
  /**
   * returns name if exists otherwise the phone.
   * 
   * @param phone
   * @return
   */
  public String getNameFromPhone(String phone) {
    String name = phone;
    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
    Cursor cs = c.getContentResolver().query(uri, new String[] { PhoneLookup.DISPLAY_NAME },
        PhoneLookup.NUMBER + "='" + phone + "'", null, null);
    if (cs.getCount() > 0) {
      cs.moveToFirst();
      name = cs.getString(cs.getColumnIndex(PhoneLookup.DISPLAY_NAME));
    }
    return name;
  }
  
  /**
   * search number of contact by LookUpKey
   * 
   * @param lookupKey
   * @return
   */
  public String lookupPhoneNumbers(String lookupKey) {
    Cursor cs = c.getContentResolver().query(Phone.CONTENT_URI, null, Phone.LOOKUP_KEY + " = ?", new String[] { lookupKey }, null);
    if (cs.getCount() > 0) {
      cs.moveToFirst();
      return cs.getString(cs.getColumnIndex(Phone.NUMBER));
    }
    return "No phone number";
  }
  
  static public ArrayList<ContactType> getTestContacts() {
    ArrayList<ContactType> slist = new ArrayList<ContactType>();
    slist.add(new ContactType("0544457141", "Roman Gurevitch"));
    slist.add(new ContactType("0000000000", "John Doe"));
    return slist;
  }
}
