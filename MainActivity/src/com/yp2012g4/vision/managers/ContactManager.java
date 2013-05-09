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
  private final Context _c;
  String[] projection = new String[] { BaseColumns._ID, ContactsContract.Contacts.DISPLAY_NAME,
      ContactsContract.Data.TIMES_CONTACTED, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.LOOKUP_KEY };
  
  public ContactManager(Context c) {
    _c = c;
  }
  
  /**
   * 
   * @return 10 favorite contacts.
   */
  public ArrayList<ContactType> getFavoriteContacts() {
    Cursor cur = _c.getContentResolver().query(
        ContactsContract.Contacts.CONTENT_URI,
        projection,
        ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'",
        null,
        ContactsContract.Data.TIMES_CONTACTED + " DESC, " + ContactsContract.Contacts.DISPLAY_NAME
            + " COLLATE LOCALIZED ASC LIMIT 10");
    ArrayList<ContactType> slist = new ArrayList<ContactType>();
    if (cur.moveToFirst())
      do
        try {
          slist.add(new ContactType(cur, _c));
        } catch (IllegalArgumentException e) {
          continue;
        }
      while (cur.moveToNext());
    return slist;
  }
  
  /**
   * 
   * @return all contacts arranged alphabetically
   */
  public ArrayList<ContactType> getAllContacts() {
    Cursor cur = _c.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection,
        ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'", null,
        ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    ArrayList<ContactType> slist = new ArrayList<ContactType>();
    if (cur.moveToFirst())
      do
        try {
          slist.add(new ContactType(cur, _c));
        } catch (IllegalArgumentException e) {
          continue;
        }
      while (cur.moveToNext());
    return slist;
  }
  
  /**
   * 
   * @param phone
   * @return name if exists in contacts otherwise return phone.
   */
  public String getNameFromPhone(String phone) {
    String name = phone;
    Cursor cs = _c.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone)),
        new String[] { PhoneLookup.DISPLAY_NAME }, PhoneLookup.NUMBER + "='" + phone + "'", null, null);
    if (cs.getCount() > 0) {
      cs.moveToFirst();
      name = cs.getString(cs.getColumnIndex(PhoneLookup.DISPLAY_NAME));
    }
    return name;
  }
  
  /**
   * 
   * @param lookupKey
   * @return the phone number of a contact by its LookUpKey
   */
  public String lookupPhoneNumbers(String lookupKey) {
    Cursor cs = _c.getContentResolver().query(Phone.CONTENT_URI, null, Phone.LOOKUP_KEY + " = ?", new String[] { lookupKey }, null);
    if (cs.getCount() > 0) {
      cs.moveToFirst();
      return cs.getString(cs.getColumnIndex(Phone.NUMBER));
    }
    return "";
  }
  
  static public ArrayList<ContactType> getTestContacts() {
    ArrayList<ContactType> slist = new ArrayList<ContactType>();
    slist.add(new ContactType("0544457141", "Roman Gurevitch"));
    slist.add(new ContactType("0000000000", "John Doe"));
    return slist;
  }
}