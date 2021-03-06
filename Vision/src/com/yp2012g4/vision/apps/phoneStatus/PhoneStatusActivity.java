package com.yp2012g4.vision.apps.phoneStatus;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.managers.CallType;
import com.yp2012g4.vision.managers.CallsManager;
import com.yp2012g4.vision.tools.TTS;
import com.yp2012g4.vision.tools.VisionActivity;

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
public class PhoneStatusActivity extends VisionActivity {
  private static final String TAG = "vision:PhoneStatusActivity";
  final public static int signal_veryGood = 12, signal_good = 8, signal_poor = 5, signal_noSignalThreshold = 2,
      signal_noSignalValue = 99;
  // Used to activate the onTouch button reading function.
  static final int MAX_SIGNAL = 31; // Maximum signal strength of GSM
  PhoneNotifications pn;
  final ArrayList<String> contacts = new ArrayList<String>();
  private CallsManager callsManager;
  
  /**
   * format the current battery level
   * 
   * @return the percentage of the battery
   */
  public int getBatteryLevel() {
    return Double.valueOf(pn.getBatteryLevel() * 100.0).intValue();
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
    final ArrayList<CallType> calls = callsManager.getMissedCallsList();
    String s = "";
    if (calls.isEmpty())
      s = getString(R.string.no_missed_calls);
    else
      for (final CallType c : calls) {
        s += getString(R.string.called_at) + " " + c.date.toLocaleString() + ". " + getString(R.string.from) + " ";
        s += (TTS.isPureEnglish(c.name) ? c.name : c.number) + ".\n";
        // TODO: Remove when Hebrew is detected.
      }
    speakOutSync(s);
  }
  
  @Override public int getViewId() {
    return R.id.phoneStatusActivity;
  }
  
  /**
   * Adds onClick events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final Resources res = getResources();
    final View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.button_getBatteryStatus:
        speakOutAsync(String.format(res.getString(R.string.phoneStatus_message_batteryStatus_read),
            Integer.valueOf(getBatteryLevel()), getChargeStatus()));
        break;
      case R.id.button_getReceptionStatus:
        speakOutAsync(signalToString());
        break;
      case R.id.button_getMissedCalls:
        startActivity(newFlaggedIntent(this, CallListActivity.class));
        break;
      default:
        break;
    }
    return false;
  }
  
  /**
   * onCreate method.
   */
  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "IncomingCAllActivity starting");
    callsManager = new CallsManager(this);
    init(0, getString(R.string.phoneStatus_whereami), getString(R.string.phoneStatus_help));
    setContentView(R.layout.activity_phone_status);
    pn = new PhoneNotifications(this);
  }
  
  /**
   * Returns the signal strength in percentage.
   * 
   * @return
   */
  public String signalToString() {
    final int signal = PhoneNotifications.getSignalStrength();
    Log.d(TAG, String.valueOf((int) (signal * 100.0f / MAX_SIGNAL)));
    if (signal <= signal_noSignalThreshold || signal == signal_noSignalValue)
      return getString(R.string.phoneStatus_message_noSignal_read);
    if (signal >= signal_veryGood)
      return getString(R.string.phoneStatus_message_veryGoodSignal_read);
    if (signal >= signal_good)
      return getString(R.string.phoneStatus_message_goodSignal_read);
    if (signal >= signal_poor)
      return getString(R.string.phoneStatus_message_poorSignal_read);
    return getString(R.string.phoneStatus_message_veryPoorSignal_read);
  }
}
