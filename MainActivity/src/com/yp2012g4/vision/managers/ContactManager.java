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
  private final String[] _projection = new String[] { BaseColumns._ID, ContactsContract.Contacts.DISPLAY_NAME,
      ContactsContract.Data.TIMES_CONTACTED, ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.LOOKUP_KEY };
  
  public ContactManager(final Context c) {
    _c = c;
  }
  
  /**
   * 
   * @return 10 favorite contacts.
   */
  public ArrayList<ContactType> getFavoriteContacts() {
    final Uri u = ContactsContract.Contacts.CONTENT_URI;
    final String ss = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
    final String[] sa = null;
    final String so = ContactsContract.Data.TIMES_CONTACTED + " DESC, " + ContactsContract.Contacts.DISPLAY_NAME
        + " COLLATE LOCALIZED ASC LIMIT 10";
    final Cursor cur = _c.getContentResolver().query(u, _projection, ss, sa, so);
    final ArrayList<ContactType> $ = new ArrayList<ContactType>();
    if (cur.moveToFirst())
      do
        try {
          $.add(new ContactType(cur, _c));
        } catch (final IllegalArgumentException e) {
          // Ignore and go next
        }
      while (cur.moveToNext());
    cur.close();
    return $;
  }
  
  /**
   * 
   * @return all contacts arranged alphabetically
   */
  public ArrayList<ContactType> getAllContacts() {
    final Uri uri = ContactsContract.Contacts.CONTENT_URI;
    final String ss = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
    final String[] sa = null;
    final String so = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
    final Cursor cur = _c.getContentResolver().query(uri, _projection, ss, sa, so);
    final ArrayList<ContactType> $ = new ArrayList<ContactType>();
    if (cur.moveToFirst())
      do
        try {
          $.add(new ContactType(cur, _c));
        } catch (final IllegalArgumentException e) {
          // Ignore and go next
        }
      while (cur.moveToNext());
    return $;
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
    final Cursor cs = _c.getContentResolver().query(Phone.CONTENT_URI, null, Phone.LOOKUP_KEY + " = ?", new String[] { lk }, null);
    if (cs.getCount() > 0) {
      cs.moveToFirst();
      return cs.getString(cs.getColumnIndex(Phone.NUMBER));
    }
    return "";// "No phone number";
  }
  
  static public ArrayList<ContactType> getTestContacts() {
    final ArrayList<ContactType> $ = new ArrayList<ContactType>();
    $.add(new ContactType("0544457141", "Roman Gurevitch"));
    $.add(new ContactType("0000000000", "John Doe"));
    return $;
  }
}
