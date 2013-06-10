package com.yp2012g4.vision.managers;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.CallLog.Calls;

/**
 * Call type container
 * 
 * @author Yaron
 * 
 */
public class CallsManager {
  private Cursor _cur = null;
  private final Context _context;
  private static final String[] _projection = { CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE };
  
  public CallsManager(final Context c) {
    _context = c;
  }
  
  /**
   * Get a list of all the calls with the date, name and phone number of each
   * 
   * @return list of all calls
   */
  public ArrayList<CallType> getAllCallsList() {
    Cursor cr;
    try {
      cr = _context.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, null, null, null);
    } catch (final Exception e) {
      return new ArrayList<CallType>();
    }
    return copyToList(_context, cr, new ArrayList<CallType>());
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
  public ArrayList<CallType> getMissedCallsList() {
    Cursor cur;
    try {
      cur = _context.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, missedCallWhere(), null, null);
    } catch (final Exception e) {
      return new ArrayList<CallType>();
    }
    return copyToList(_context, cur, new ArrayList<CallType>());
  }
  
  /**
   * Delete all the entries of a number from the missed call list - require
   * WRITE_CONTACTS permission
   * 
   * @param c
   * @param ns
   *          - string of the phone number in the call log
   */
  public void UnmarkCallLFromMissedCallList(final String phoneNumber) {
    try {
      final ContentValues values = new ContentValues();
      values.put(Calls.NEW, Integer.valueOf(0));
      final StringBuilder where = new StringBuilder();
      where.append(Calls.NEW + " = 1");
      where.append(" AND ");
      where.append(Calls.TYPE + " = ?");// + Calls.MISSED_TYPE);
      where.append(" AND ");
      where.append(Calls.NUMBER + " = ? ");
      _context.getContentResolver().update(Calls.CONTENT_URI, values, where.toString(),
          new String[] { Integer.toString(Calls.MISSED_TYPE), phoneNumber });
    } catch (final Exception e) {
      e.getMessage();
    }
  }
  
//  public void MarkCallLFromMissedCallList() {
//    int i;
//    try {
//      final ContentValues values = new ContentValues();
//      values.put(Calls.NEW, 1);
//      final StringBuilder where = new StringBuilder();
//      where.append(Calls.NEW + " = 0");
//      where.append(" AND ");
//      where.append(Calls.TYPE + " = ?");
//      i = _context.getContentResolver().update(Calls.CONTENT_URI, values, where.toString(),
//          new String[] { Integer.toString(Calls.MISSED_TYPE) });
//      i++;
//    } catch (final Exception e) {
//      e.getMessage();
//    }
//  }
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
  public CallType getLastOutgoingCall() {
    try {
      _cur = _context.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, null, null, null);
    } catch (final Exception e) {
      return null;
    }
    if (_cur.moveToNext())
      return new CallType(_context, _cur);
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
  public ArrayList<CallType> getNextMissedCallsList(final int ns) {
    try {
      if (_cur == null)
        _cur = _context.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, missedCallWhere(), null, null);
    } catch (final Exception e) {
      return new ArrayList<CallType>();
    }
    final ArrayList<CallType> $ = new ArrayList<CallType>();
    for (int i = 0; i < ns && _cur.moveToNext(); i++)
      $.add(new CallType(_context, _cur));
    return $;
  }
  
  /**
   * Get the number of missed calls
   * 
   * @return the missed calls number
   */
  public int getMissedCallsNum() {
    Cursor $;
    try {
      $ = _context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, missedCallWhere(), null, null);
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
