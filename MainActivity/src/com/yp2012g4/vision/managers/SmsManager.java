package com.yp2012g4.vision.managers;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.yp2012g4.vision.tools.StackTraceToString;

public class SmsManager {
  public static final String CONTENT_SMS_INBOX = "content://sms/inbox";
  public static final String TAG = "vision:SmsManager";
  
  public static ArrayList<SmsType> getIncomingMessages(final Context c) {
    final ArrayList<SmsType> $ = new ArrayList<SmsType>();
    final Uri uri = Uri.parse(CONTENT_SMS_INBOX);
    final Cursor cur = c.getContentResolver().query(uri, null, null, null, null);
    if (cur.moveToFirst())
      do
        try {
          $.add(new SmsType(cur, c));
        } catch (final IllegalArgumentException e) {
          // Ignore and go next
        }
      while (cur.moveToNext());
    return $;
  }
  
  /**
   * 
   * @param c
   *          context
   * @param ns
   *          name string
   * @param bs
   *          body string
   */
  static public void markMessageRead(final Context c, final String ns, final String bs) {
    final Uri uri = Uri.parse(CONTENT_SMS_INBOX);
    final Cursor cursor = c.getContentResolver().query(uri, null, null, null, null);
    try {
      while (cursor.moveToNext()) {
        final String ad = cursor.getString(cursor.getColumnIndex("address"));
        final int n = cursor.getInt(cursor.getColumnIndex("read"));
        if (ad.equals(ns) && n == 0)
          if (cursor.getString(cursor.getColumnIndex("body")).startsWith(bs)) {
            final String SmsMessageId = cursor.getString(cursor.getColumnIndex("_id"));
            final ContentValues v = new ContentValues();
            v.put("read", Boolean.TRUE);
            c.getContentResolver().update(Uri.parse(CONTENT_SMS_INBOX), v, "_id=" + SmsMessageId, null);
            return;
          }
      }
    } catch (final Exception e) {
      Log.e("Mark Read", "Error in Read: " + e.toString());
    }
  }
  
  /**
   * Get the number of SMS messages the user didn't read yet
   * 
   * @return the unread SMS number
   */
  public static int getUnreadSMS(final Context c) {
    final Uri SMS_INBOX = Uri.parse(CONTENT_SMS_INBOX);
    final Cursor cur = c.getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);
    return cur.getCount();
  }
  
  /**
   * 
   * @param c
   *          context
   * @param ms
   *          Massage body
   * @param ma
   *          Massage address
   */
  public static void deleteSMS(final Context c, final String ms, final String ma) {
    Log.i("SmsManager", "deleting msg: " + ms + " from: " + ma);
    try {
      final Uri uri = Uri.parse(CONTENT_SMS_INBOX);
      final Cursor cur = c.getContentResolver().query(uri,
          new String[] { "_id", "thread_id", "address", "person", "date", "body" }, null, null, null);
      if (cur != null && cur.moveToFirst())
        do {
          final String address = cur.getString(2);
          final String body = cur.getString(5);
          if (ms.equals(body) && address.equals(ma)) {
            c.getContentResolver().delete(Uri.parse("content://sms/" + cur.getLong(0)), null, null);
            cur.close();
            break;
          }
        } while (cur.moveToNext());
    } catch (final Exception e) {
      Log.e(TAG, "deleteSMS " + StackTraceToString.toString(e));
    }
  }
}
