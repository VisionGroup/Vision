package com.yp2012g4.vision.managers;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class ContactManager {
  private static final String TAG = "vision:ContactManager";
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
    final String ss = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
    final String so = ContactsContract.Data.TIMES_CONTACTED + " DESC, " + ContactsContract.Contacts.DISPLAY_NAME
        + " COLLATE LOCALIZED ASC LIMIT 10";
    try {
      cur = _c.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, _projection, ss, null, so);
    } catch (final Exception e) {
      Log.d(TAG, "At getFavoriteContacts" + e.getMessage());
      cur = null;
    }
    if (cur != null) {
      cur.moveToFirst();
      _size = cur.getCount();
    } else
      _size = 0;
  }
  
  /**
   * 
   * @return all contacts arranged alphabetically
   */
  public void getAllContacts() {
    final String ss = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
    final String so = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
    try {
      cur = _c.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, _projection, ss, null, so);
    } catch (final Exception e) {
      Log.d(TAG, "At getAllContacts" + e.getMessage());
      cur = null;
    }
    if (cur != null) {
      cur.moveToFirst();
      _size = cur.getCount();
    } else
      _size = 0;
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
    if (getNumOfContacts() <= p)
      return null;
    while (_contactsArray.size() <= p)
      getNextContacts(); // TODO endless loop on empty phone.
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
      try {
        $ = cs.getString(cs.getColumnIndex(Phone.NUMBER));
      } catch (final Exception e) {
        Log.d(TAG, "At lookupphonenumber" + e.getMessage());
        $ = "";
      }
    }
    return $;
  }
  
  public static void addContactToPhone(final Context context, final String DisplayName, final String MobileNumber) {
    final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
        .build());
    // ------------------------------------------------------ Name
    if (DisplayName != null)
      ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
          .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
          .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName).build());
    // ------------------------------------------------------ Mobile Number
    if (MobileNumber != null)
      ops.add(ContentProviderOperation
          .newInsert(ContactsContract.Data.CONTENT_URI)
          .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
          .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
          .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
          .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
              Integer.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)).build());
    try {
      context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
    } catch (final Exception e) {
      e.printStackTrace();
      Log.e("ContactManager", "Exception: " + e.getMessage());
    }
  }
  
  public static boolean deleteContact(final String name, final Context c) {
    final Cursor curser = c.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
    try {
      if (curser.moveToFirst())
        do
          if (curser.getString(curser.getColumnIndex(PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
            final String lookupKey = curser.getString(curser.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            final Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
            c.getContentResolver().delete(uri, null, null);
            return true;
          }
        while (curser.moveToNext());
    } catch (final Exception e) {
      Log.e("ContactManager", "delete contact Exception: " + e.getMessage());
    }
    return false;
  }
  
  /**
   * returns name if exists otherwise the phone.
   * 
   * @param ps
   *          phone number string
   * @return
   */
  public static ContactType getContactFromName(final String name, final Context c) {
    ContactType $ = null;
    final String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like'%" + name + "%'";
    final Cursor cs = c.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selection, null, null);
    if (cs.moveToFirst())
      do
        if (name.equals(cs.getString(cs.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)))) {
          $ = new ContactType(cs, c);
          break;
        }
      while (cs.moveToNext());
    cs.close();
    return $;
  }
}
