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
  public static ArrayList<CallType> getAllCallsList(Context c) {
//		final String where = CallLog.Calls.TYPE + "="
//				+ CallLog.Calls.MISSED_TYPE + " AND NEW = 1";
    Cursor cr;
    try {
      cr = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, null, null, null);
    } catch (final Exception e) {
      return new ArrayList<CallType>();
    }
    return copyToList(cr, new ArrayList<CallType>());
  }
  
  private static ArrayList<CallType> copyToList(Cursor cr, final ArrayList<CallType> al) {
    while (cr.moveToNext())
      al.add(new CallType(cr));
    return al;
  }
  
  /**
   * Get a list of all the missed calls with the date, name and phone number of
   * each
   * 
   * @return list of missed calls
   */
  public static ArrayList<CallType> getMissedCallsList(Context c) {
    final String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND NEW = 1";
    Cursor cur;
    try {
      cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, where, null, null);
    } catch (final Exception e) {
      return new ArrayList<CallType>();
    }
    // final ArrayList<CallType> al = ;
    return copyToList(cur, new ArrayList<CallType>());
  }
  
  /**
   * Delete all the entries of a number from the missed call list - require
   * WRITE_CONTACTS permission
   * 
   * @param c
   * @param ns
   *          - string of the phone number in the call log
   */
  static public void DeleteCallLogByNumber(Context c, String ns) {
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
  public CallType getLastOutgoingCall(Context c) {
    try {
      _cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, null, null, null);
    } catch (Exception e) {
      return null;
    }
    if (_cur.moveToNext())
      return new CallType(_cur);
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
  public ArrayList<CallType> getNextMissedCallsList(Context c, int ns) {
    String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND NEW = 1";
    try {
      _cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, where, null, null);
    } catch (Exception e) {
      return new ArrayList<CallType>();
    }
    ArrayList<CallType> al = new ArrayList<CallType>();
    for (int i = 0; i < ns && _cur.moveToNext(); i++)
      al.add(new CallType(_cur));
    return al;
  }
  
  /**
   * Get the number of missed calls
   * 
   * @return the missed calls number
   */
  public static int getMissedCallsNum(Context c) {
    final String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND NEW = 1";
    Cursor cur;
    try {
      cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, where, null, null);
    } catch (final Exception e) {
      return 0;
    }
    return cur.getCount();
  }
}
