package com.yp2012g4.blindroid;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.yp2012g4.blindroid.PhoneNotifications.CallData;
import com.yp2012g4.blindroid.telephony.IncomingCallActivity;
import com.yp2012g4.blindroid.tools.BlindroidActivity;

/**
 * This is the activity for viewing and hearing phone status (currently battery,
 * charge and signal).
 * 
 * @version 1.0
 * @author Amit Yaffe
 * 
 */
public class PhoneStatusActivity extends BlindroidActivity {
  private static final String TAG = "bd:PhoneStatusActivity";
  
  /**
   * A signal strength listener. Updates _signal between 0-31.
   * 
   * @author Dell
   * 
   */
  private class SignalStrengthListener extends PhoneStateListener {
    protected SignalStrengthListener() {
    }
    
    @Override public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
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
//Battery Broadcast receiver.
  final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      _battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
      _status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
    }
  };
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
  
  public void getMissedCalls() {
    PhoneNotifications pn = new PhoneNotifications(this);
    ArrayList<CallData> calls = pn.getMissedCallsList();
    for (CallData c : calls) {
      speakOut(" called At: " + c.getHour() + " ,From: " + c.number);
      while (_t.isSpeaking()) {
        // Wait for message to finish playing and then finish the activity
      }
    }
//    String s = "";
//    boolean b = true;
//    while (c.moveToNext()) {
//      String name = c.getString(1);
//      if (!b)
//        s += " And from ";
//      else
//        b = false;
//      s += name;
//    }
//    if (c.getCount() == 0)
//      return "There are no missed calls";
//    return "You have " + c.getCount() + " missed calls from " + s;
  }
  
  @Override public int getViewId() {
    return R.id.phoneStatusActivity;
  }
  
  /**
   * Adds onClick events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  @Override public void onClick(View v) {
    final Resources res = getResources();
    switch (v.getId()) {
      case R.id.button_getBatteryStatus:
        speakOut(String.format(res.getString(R.string.phoneStatus_message_batteryStatus_read), Integer.valueOf(_battery),
            getChargeStatus(_status)));
        break;
      case R.id.button_getReceptionStatus:
        speakOut(signalToString(_signal));
        break;
      case R.id.button_getMissedCalls:
        getMissedCalls();
        break;
      case R.id.back_button:
        speakOut("Previous screen");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.settings_button:
        speakOut("Settings");
        // final Intent intent = new Intent(this,
        // DisplaySettingsActivity.class);
        final Intent intent = new Intent(this, IncomingCallActivity.class);
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
  
  @Override public void onDestroy() {
    super.onDestroy();
    unregisterReceiver(batteryReceiver);
  }
  
  /**
   * onCreate method.
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "IncomingCAllActivity starting");
    init(0/* TODO Check what icon goes here */, getString(R.string.phoneStatus_whereami), getString(R.string.phoneStatus_help));
    setContentView(R.layout.activity_phone_status);
    final IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    registerReceiver(batteryReceiver, filter);
    // start the signal strength listener
    signalStrengthListener = new SignalStrengthListener();
    ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(signalStrengthListener,
        PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
  }
  
  /**
   * Returns the signal strength in percentage.
   * 
   * @param signal
   * @return
   */
  public String signalToString(int signal) {
    Log.d(TAG, String.valueOf((int) (signal * 100.0f / MAX_SIGNAL)));
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
