package com.yp2012g4.blindroid;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
 * @version 1.1
 * @author Amir Blumental
 * 
 */
public class PhoneStatusActivity extends BlindroidActivity {
  /**
   * A signal strength listener. Updates _signal between 0-31.
   * 
   * @author Dell
   * 
   */
  private static final String TAG = "bd:PhoneStatusActivity";
  /**
   * Used to activate the onTouch button reading function.
   */
  static final int MAX_SIGNAL = 31; // Maximum signal strength of GSM
  PhoneNotifications pn;
  final ArrayList<String> contacts = new ArrayList<String>();
  
  /**
   * format the current battery level
   * 
   * @return the percentage of the battery
   */
  public int getBatteryLevel() {
    return Float.valueOf(pn.getBatteryLevel() * (float) 100.0).intValue();
  }
  
  /**
   * Returns the chargeStatus String according to charge status param. Empty
   * string if not charging.
   * 
   * @return
   */
  public String getChargeStatus() {
    if (pn.getChargerStatus())
      return getString(R.string.phoneStatus_message_charging_read);
    return "";
  }
  
  /**
   * read the call log of the missed calls
   */
  public void getMissedCalls() {
    ArrayList<CallData> calls = pn.getMissedCallsList();
    String s = "";
    if (calls.isEmpty())
      s = "There are no missed calles";
    else
      for (CallData c : calls)
        s += " called At: " + c.getHour() + " ,From: " + c.number + "\n";
    speakOut(s);
    while (_t.isSpeaking()) {
      // Wait for message to finish playing and then finish the activity
    }
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
    final Resources res = getResources();
    switch (v.getId()) {
      case R.id.button_getBatteryStatus:
        speakOut(String.format(res.getString(R.string.phoneStatus_message_batteryStatus_read), Integer.valueOf(getBatteryLevel()),
            getChargeStatus()));
        break;
      case R.id.button_getReceptionStatus:
        speakOut(signalToString());
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
  
  /**
   * onCreate method.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "IncomingCAllActivity starting");
    init(0/* TODO Check what icon goes here */, getString(R.string.phoneStatus_whereami), getString(R.string.phoneStatus_help));
    setContentView(R.layout.activity_phone_status);
    pn = new PhoneNotifications(this);
  }
  
  /**
   * Returns the signal strength in percentage.
   * 
   * @return
   */
  public String signalToString() {
    int signal = PhoneNotifications.getSignalStrength();
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
