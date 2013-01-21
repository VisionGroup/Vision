package com.yp2012g4.blindroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.yp2012g4.blindroid.tools.BlindroidActivity;

public class MainActivity extends BlindroidActivity {
  @Override
  public int getViewId() {
    return R.id.MainActivityView;
  }
  
  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    Intent intent;
    switch (curr_view.getId()) {
      case R.id.sos_button:
        intent = new Intent(MainActivity.this, SOSActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.time_button:
        intent = new Intent(MainActivity.this, SpeakingClockActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.where_am_i_button:
        intent = new Intent(MainActivity.this, WhereAmIActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.phone_status_button:
        intent = new Intent(MainActivity.this, PhoneStatusActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.alarm_clock_button:
        intent = new Intent(MainActivity.this, AlarmActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.contacts_button:
        intent = new Intent(MainActivity.this, ContactsMenuActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.setting_button:
        intent = new Intent(MainActivity.this, DisplaySettingsActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.read_sms_button:
        intent = new Intent(MainActivity.this, ReadSmsActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      default:
        break;
    }
    return false;
  }
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    PhoneNotifications pn = new PhoneNotifications(this);
    init(0, getString(R.string.MainActivity_wai), getString(R.string.MainActivity_help));
    pn.startSignalLisener();
  }
  
  /**
   * Perform actions when the window get into focus we start the activity by
   * reading out loud the current title
   */
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    while (_t.isSpeaking()) {
      // Wait for message to finish playing and then finish the activity
    }
    if (!hasFocus)
      return;
    VoiceNotify();
  }
  
  public void VoiceNotify() {
    String s = "";
    PhoneNotifications pn = new PhoneNotifications(this);
    float batteryLevel = pn.getBatteryLevel();
    boolean isCharging = pn.getChargerStatus();
    if (!isCharging && batteryLevel < 0.3)
      s += " Low Battery! \n";
    int signalS = PhoneNotifications.getSignalStrength();
    if (signalS < 2)
      s += getString(R.string.phoneStatus_message_noSignal_read) + "\n";
    else if (signalS < 5)
      s += getString(R.string.phoneStatus_message_veryPoorSignal_read) + "\n";
    int numOfMissedCalls = pn.getMissedCallsNum();
    if (numOfMissedCalls > 0) {
      s += numOfMissedCalls + " missed call";
      if (numOfMissedCalls > 1)
        s += "s";
    }
    int numOfSms = pn.getUnreadSMS();
    if (numOfSms > 0)
      s += numOfSms + " new S M S\n";
    speakOut(s);
    while (_t.isSpeaking()) {
      // Wait for message to finish playing and then finish the activity
    }
  }
  
  @Override
  public void onBackPressed() {
    // do nothing
  }
}