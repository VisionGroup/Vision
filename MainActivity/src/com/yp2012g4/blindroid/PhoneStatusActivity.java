package com.yp2012g4.blindroid;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.yp2012g4.blindroid.utils.BlindroidActivity;

/**
 * This is the activity for viewing and hearing phone status (currently battery,
 * charge and signal).
 * 
 * @version 1.0
 * @author Amit Yaffe
 * 
 */
public class PhoneStatusActivity extends BlindroidActivity {
  /**
   * A signal strength listener. Updates _signal between 0-31.
   * 
   * @author Dell
   * 
   */
  private class SignalStrengthListener extends PhoneStateListener {
    protected SignalStrengthListener() {
    }
    
    @Override
    public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
      // get the signal strength (a value between 0 and 31)
      _signal = signalStrength.getGsmSignalStrength();
      super.onSignalStrengthsChanged(signalStrength);
    }
  }
  
  /**
   * Used to activate the onTouch button reading function.
   */
  static final int MAX_SIGNAL = 31; // Maximum signal strength of GSM
  int _battery = -1; // Battery status
  int _status = -1; // Charging Status
  int _signal = -1; // Reception status
  SignalStrengthListener signalStrengthListener;
  final ArrayList<String> contacts = new ArrayList<String>();
  
  /**
   * Returns the chargeStatus String according to charge status param. Empty
   * string if not charging.
   * 
   * @param status
   * @return
   */
  public String getChargeStatus(int status) {
    switch (status) {
      case BatteryManager.BATTERY_STATUS_CHARGING:
      case BatteryManager.BATTERY_STATUS_FULL:
        return getString(R.string.phoneStatus_message_charging_read);
      default:
        return "";
    }
  }
  
  public String getMissedCalls() {
    String[] projection = { CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.TYPE };
    String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND NEW = 1";
    Cursor c = managedQuery(CallLog.Calls.CONTENT_URI, projection, where, null, null);
    String s = "";
    boolean b = true;
    while (c.moveToNext()) {
      String name = c.getString(1);
      if (!b)
        s += " And from ";
      else
        b = false;
      s += name;
    }
    if (c.getCount() == 0)
      return "There are no missed calls";
    return "You have " + c.getCount() + " missed calls from " + s;
  }
  
  public String getUnreadSMS() {
    final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    Cursor c = managedQuery(SMS_INBOX, null, "read = 0", null, null);
    int unreadMessagesCount = c.getCount();
    if (unreadMessagesCount == 0)
      return "There are no unread SMS messages";
    return "You have " + unreadMessagesCount + " unread SMS";
  }
  
  @Override
  public int getViewId() {
    return R.id.phoneStatusActivity;
  }
  
  /**
   * Adds onClick events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  @Override
  public void onClick(View v) {
    Resources res = getResources();
    switch (v.getId()) {
      case R.id.button_getBatteryStatus:
        speakOut(String.format(res.getString(R.string.phoneStatus_message_batteryStatus_read), Integer.valueOf(_battery),
            getChargeStatus(_status)));
        break;
      case R.id.button_getReceptionStatus:
        speakOut(signalToString(_signal));
        break;
      case R.id.button_getMissedCalls:
        speakOut(getMissedCalls());
        break;
      case R.id.button_getUnreadSMS:
        speakOut(getUnreadSMS());
        break;
      case R.id.back_button:
        speakOut("Previous screen");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.settings_button:
        speakOut("Settings");
        Intent intent = new Intent(this, DisplaySettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.home_button:
        speakOut("Home");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.current_menu_button:
        speakOut("This is " + getString(R.string.phoneStatus_whereami));
        break;
      default:
        break;
    }
  }
  
  /**
   * onCreate method.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init(this, 0/* TODO Check what icon goes here */, getString(R.string.phoneStatus_whereami),
        getString(R.string.phoneStatus_help));
    setContentView(R.layout.activity_phone_status);
    // Battery Broadcast receiver.
    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        _battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        _status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
      }
    };
    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    registerReceiver(batteryReceiver, filter);
    // start the signal strength listener
    signalStrengthListener = new SignalStrengthListener();
    ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(signalStrengthListener,
        PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
  }
  
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus)
      speakOut("Phone status screen");
  }
  
  /**
   * Returns the signal strength in percentage.
   * 
   * @param signal
   * @return
   */
  public String signalToString(int signal) {
    Log.d("Signal", String.valueOf((int) (signal * 100.0f / MAX_SIGNAL)));
    if (signal <= 2 || signal == 99)
      return getString(R.string.phoneStatus_message_noSignal_read);
    else if (signal >= 12)
      return getString(R.string.phoneStatus_message_veryGoodSignal_read);
    else if (signal >= 8)
      return getString(R.string.phoneStatus_message_goodSignal_read);
    else if (signal >= 5)
      return getString(R.string.phoneStatus_message_poorSignal_read);
    else
      return getString(R.string.phoneStatus_message_veryPoorSignal_read);
  }
}
