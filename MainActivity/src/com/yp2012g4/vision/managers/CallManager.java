package com.yp2012g4.vision.managers;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

/**
 * Call type container
 * 
 * @author Yaron
 * 
 */
public class CallManager {
  private Cursor _cur = null;
  private static final String[] _projection = { CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE };
  
  /**
   * Get a list of all the calls with the date, name and phone number of each
   * 
   * @return list of all calls
   */
  public static ArrayList<CallType> getAllCallsList(final Context c) {
    Cursor cr;
    try {
      cr = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, null, null, null);
    } catch (final Exception e) {
      return new ArrayList<CallType>();
    }
    return copyToList(c, cr, new ArrayList<CallType>());
  }
  
  private static ArrayList<CallType> copyToList(final Context c, final Cursor cr, final ArrayList<CallType> al) {
    while (cr.moveToNext())
      al.add(new CallType(c, cr));
    return al;
  }
  
  /**
   * Get a list of all the missed calls with the date, name and phone number of
   * each
   * 
   * @return list of missed calls
   */
  public static ArrayList<CallType> getMissedCallsList(final Context c) {
    Cursor cur;
    try {
      cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, missedCallWhere(), null, null);
    } catch (final Exception e) {
      return new ArrayList<CallType>();
    }
    return copyToList(c, cur, new ArrayList<CallType>());
  }
  
  /**
   * Delete all the entries of a number from the missed call list - require
   * WRITE_CONTACTS permission
   * 
   * @param c
   * @param ns
   *          - string of the phone number in the call log
   */
  static public void UnmarkCallLFromMissedCallList(final Context c, final String ns) {
    try {
      c.getContentResolver().delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER + "=?", new String[] { ns });
    } catch (final Exception e) {
      e.getMessage();
    }
  }
  
  /**
   * Get a list of all the missed calls with the date, name and phone number of
   * each
   * 
   * @param c
   *          Context
   * @param number
   *          how may call to get back
   * @return list of missed calls
   */
  public CallType getLastOutgoingCall(final Context c) {
    try {
      _cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, null, null, null);
    } catch (final Exception e) {
      return null;
    }
    if (_cur.moveToNext())
      return new CallType(c, _cur);
    return null;
  }
  
  /**
   * Get a list of all the missed calls with the date, name and phone number of
   * each
   * 
   * @param c
   *          Context
   * @param ns
   *          how may call to get back
   * @return list of missed calls
   */
  public ArrayList<CallType> getNextMissedCallsList(final Context c, final int ns) {
    try {
      _cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, missedCallWhere(), null, null);
    } catch (final Exception e) {
      return new ArrayList<CallType>();
    }
    final ArrayList<CallType> $ = new ArrayList<CallType>();
    for (int i = 0; i < ns && _cur.moveToNext(); i++)
      $.add(new CallType(c, _cur));
    return $;
  }
  
  /**
   * Get the number of missed calls
   * 
   * @return the missed calls number
   */
  public static int getMissedCallsNum(final Context c) {
    Cursor $;
    try {
      $ = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, missedCallWhere(), null, null);
    } catch (final Exception e) {
      return 0;
    }
    return $.getCount();
  }
  
  /**
   * @return where string for a SQL query for missed calls list
   */
  private static String missedCallWhere() {
    return CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND NEW = 1";
  }
}
