package com.yp2012g4.vision;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.yp2012g4.vision.alarm.AlarmActivity;
import com.yp2012g4.vision.clock.SpeakingClockActivity;
import com.yp2012g4.vision.contacts.ContactsMenuActivity;
import com.yp2012g4.vision.managers.CallManager;
import com.yp2012g4.vision.managers.SmsManager;
import com.yp2012g4.vision.settings.DisplaySettingsActivity;
import com.yp2012g4.vision.sms.ReadSmsActivity;
import com.yp2012g4.vision.sms.SOSActivity;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * 
 * The Main activity.
 * 
 */
public class MainActivity extends VisionActivity {
//  private static String TAG = "vision:MainActivity";
  @Override public int getViewId() {
    return R.id.MainActivityView;
  }
  
  /** Called when the activity is first created. */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i("MyLog", "MainActivity:: onCreate");
    setContentView(R.layout.activity_main);
    adjustLayoutSize(4);
    final PhoneNotifications pn = new PhoneNotifications(this);
    init(0, getString(R.string.MainActivity_wai), getString(R.string.MainActivity_help));
    pn.startSignalLisener();
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    if (_navigationBar)
      return _navigationBar = false;
    Intent intent = getIntentByButtonId(getButtonByMode().getId());
    if (intent != null) {
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
      startActivity(intent);
    }
    return false;
  }
  
//Cannot be static because of MainActivity.this
  @SuppressWarnings("static-method") private Intent getIntentByButtonId(int id) {
    switch (id) {
      case R.id.sos_button:
        return new Intent(MainActivity.this, SOSActivity.class);
      case R.id.time_button:
        return new Intent(MainActivity.this, SpeakingClockActivity.class);
      case R.id.where_am_i_button:
        return new Intent(MainActivity.this, WhereAmIActivity.class);
      case R.id.phone_status_button:
        return new Intent(MainActivity.this, PhoneStatusActivity.class);
      case R.id.alarm_clock_button:
        return new Intent(MainActivity.this, AlarmActivity.class);
      case R.id.contacts_button:
        return new Intent(MainActivity.this, ContactsMenuActivity.class);
      case R.id.setting_button:
        return new Intent(MainActivity.this, DisplaySettingsActivity.class);
      case R.id.read_sms_button:
        return new Intent(MainActivity.this, ReadSmsActivity.class);
        // intent = new Intent(MainActivity.this, CallListActivity.class);
      default:
        return null;
    }
  }
  
  /**
   * Perform actions when the window get into focus we start the activity by
   * reading out loud the current title
   */
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    while (_tts.isSpeaking()) {
      // Wait for message to finish playing and then finish the activity
    }
    if (!hasFocus)
      return;
    VoiceNotify();
  }
  
  public void VoiceNotify() {
    String s = "";
    final PhoneNotifications pn = new PhoneNotifications(this);
    final float batteryLevel = pn.getBatteryLevel();
    final boolean isCharging = pn.getChargerStatus();
    if (!isCharging && batteryLevel < 0.3)
      s += getString(R.string.low_battery);
    final int signalS = PhoneNotifications.getSignalStrength();
    // TODO sparta constants
    if (signalS <= PhoneStatusActivity.signal_noSignalThreshold)
      s += getString(R.string.phoneStatus_message_noSignal_read) + "\n";
    else if (signalS <= PhoneStatusActivity.signal_poor)
      s += getString(R.string.phoneStatus_message_veryPoorSignal_read) + "\n";
    final int numOfMissedCalls = CallManager.getMissedCallsNum(this); // pn.getMissedCallsNum();
    if (numOfMissedCalls > 0) {
      s += numOfMissedCalls + " " + getString(R.string.missed_call);
      if (numOfMissedCalls > 1)
        s += "s";
    }
    final int numOfSms = SmsManager.getUnreadSMS(this);
    if (numOfSms > 0)
      s += numOfSms + getString(R.string.new_sms);
    speakOutAsync(s);
    while (_tts.isSpeaking()) {
      // Wait for message to finish playing and then finish the activity
    }
  }
  
  @Override public void onBackPressed() {
    speakOutAsync(getString(R.string.in_main_screen));
  }
}