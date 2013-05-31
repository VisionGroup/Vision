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
  public final int NUMBER_OF_CONTACTS_BATCH = 5;
  private int _size;
  private Cursor cur;
  public ArrayList<ContactType> _contactsArray;
  private final String[] _projection = new String[] { BaseColumns._ID, ContactsContract.Contacts.DISPLAY_NAME,
      ContactsContract.Data.TIMES_CONTACTED, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.LOOKUP_KEY };
  
  public ContactManager(final Context c) {
    _c = c;
    _contactsArray = new ArrayList<ContactType>();
  }
  
  /**
   * 
   * @return 10 favorite contacts.
   */
  public void getFavoriteContacts() {
    final Uri u = ContactsContract.Contacts.CONTENT_URI;
    final String ss = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
    final String[] sa = null;
    final String so = ContactsContract.Data.TIMES_CONTACTED + " DESC, " + ContactsContract.Contacts.DISPLAY_NAME
        + " COLLATE LOCALIZED ASC LIMIT 10";
    cur = _c.getContentResolver().query(u, _projection, ss, sa, so);
    cur.moveToFirst();
    _size = cur.getCount();
  }
  
  /**
   * 
   * @return all contacts arranged alphabetically
   */
  public void getAllContacts() {
    final Uri uri = ContactsContract.Contacts.CONTENT_URI;
    final String ss = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
    final String[] sa = null;
    final String so = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
    cur = _c.getContentResolver().query(uri, _projection, ss, sa, so);
    cur.moveToFirst();
    _size = cur.getCount();
  }
  
  public void getNextContacts() {
    for (int i = 0; i < NUMBER_OF_CONTACTS_BATCH && !cur.isAfterLast(); i++) {
      try {
        _contactsArray.add(new ContactType(cur, _c));
      } catch (final IllegalArgumentException e) {
        // Ignore and go next
      }
      cur.moveToNext();
    }
  }
  
  public int getNumOfContacts() {
    return _size;
  }
  
  public ContactType getContact(final int p) {
    if (getNumOfContacts() < p)
      return null;
    while (_contactsArray.size() <= p)
      getNextContacts();
    return _contactsArray.get(p);
  }
  
  /**
   * returns name if exists otherwise the phone.
   * 
   * @param ps
   *          phone number string
   * @return
   */
  public String getNameFromPhone(final String ps) {
    if (ps == null || ps.equals(""))
      return "";
    String $ = ps;
    final Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(ps));
    final Cursor cs = _c.getContentResolver().query(uri, new String[] { PhoneLookup.DISPLAY_NAME },
        PhoneLookup.NUMBER + "='" + ps + "'", null, null);
    if (cs.getCount() > 0) {
      cs.moveToFirst();
      $ = cs.getString(cs.getColumnIndex(PhoneLookup.DISPLAY_NAME));
    }
    return $;
  }
  
  /**
   * search number of contact by LookUpKey
   * 
   * @param lk
   *          lookupKey
   * @return
   */
  public String lookupPhoneNumbers(final String lk) {
    String $ = "";// "No phone number"
    final Cursor cs = _c.getContentResolver().query(Phone.CONTENT_URI, null, Phone.LOOKUP_KEY + " = ?", new String[] { lk }, null);
    if (cs.getCount() > 0) {
      cs.moveToFirst();
      $ = cs.getString(cs.getColumnIndex(Phone.NUMBER));
    }
    return $;
  }
  
  // only for the use of tests
  public void getTestContacts() {
    cur = null;
    _contactsArray.add(new ContactType("0544457141", "Roman Gurevitch"));
    _contactsArray.add(new ContactType("0000000000", "John Doe"));
    _size = _contactsArray.size();
  }
}
