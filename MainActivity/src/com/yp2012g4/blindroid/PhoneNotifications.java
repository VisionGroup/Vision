package com.yp2012g4.blindroid;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.provider.CallLog;

/**
 * 
 * @author amir b
 * @version 1.1
 */
public class PhoneNotifications {
  public class CallData {
    String name;
    String number;
    long date;
    
    public CallData(String name, String number, long date) {
      this.name = name;
      this.number = number;
      this.date = date;
    }
    
    public String getHour() {
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(date);
      return SpeakingClockActivity.parseTime(calendar);
    }
  }
  
  private final Context c;
  
  public PhoneNotifications(Context c) {
    this.c = c;
  }
  
  /**
   * Get the phone current battery level
   * 
   * @return battery level between 0 to 1
   */
  public float getBatteryLevel() {
    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent batteryStatus = c.registerReceiver(null, ifilter);
    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    float batteryPct = level / (float) scale;
    return batteryPct;
  }
  
  /**
   * Check if the charger is connected
   * 
   * @return true if charger is connected false otherwise
   */
  public boolean getChargerStatus() {
    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent batteryStatus = c.registerReceiver(null, ifilter);
    // Are we charging / charged?
    int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
    boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
    return isCharging;
  }
  
  public ArrayList<CallData> getMissedCallsList() {
    String[] projection = { CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE };
    String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND NEW = 1";
    Cursor cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, where, null, null);
    ArrayList<CallData> al = new ArrayList<CallData>();
    while (cur.moveToNext()) {
      CallData call = new CallData(cur.getString(0), cur.getString(1), cur.getLong(2));
      al.add(call);
    }
    return al;
  }
  
  public int getMissedCallsNum() {
    String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND NEW = 1";
    Cursor cur = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, where, null, null);
    return cur.getCount();
  }
  
  public int getUnreadSMS() {
    final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    Cursor cur = c.getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);
    return cur.getCount();
  }
}
