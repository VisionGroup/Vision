package com.yp2012g4.blindroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yp2012g4.blindroid.tools.BlindroidActivity;

public class MainActivity extends BlindroidActivity {
  @Override
  public int getViewId() {
    return R.id.MainActivityView;
  }
  
  @Override
  public void onClick(View v) {
    Intent intent;
    // speakOut(((Button) v).getText().toString());
    switch (v.getId()) {
      case R.id.sos_button:
        intent = new Intent(MainActivity.this, SOSActivity.class);
        startActivity(intent);
        break;
      case R.id.time_button:
        intent = new Intent(MainActivity.this, SpeakingClockActivity.class);
        startActivity(intent);
        break;
      case R.id.where_am_i_button:
        intent = new Intent(MainActivity.this, WhereAmIActivity.class);
        startActivity(intent);
        break;
      case R.id.phone_status_button:
        intent = new Intent(MainActivity.this, PhoneStatusActivity.class);
        startActivity(intent);
        break;
      case R.id.alarm_clock_button:
        intent = new Intent(MainActivity.this, AlarmActivity.class);
        startActivity(intent);
        break;
      case R.id.quick_dial_button:
        speakOut("Quick dial");
        intent = new Intent(MainActivity.this, QuickDialActivity.class);
        startActivity(intent);
        break;
      case R.id.quick_sms_button:
        speakOut("Quick SMS");
        intent = new Intent(MainActivity.this, QuickSMSActivity.class);
        startActivity(intent);
        break;
      case R.id.read_sms_button:
        intent = new Intent(MainActivity.this, TalkingSmsList.class);
        startActivity(intent);
        break;
      case R.id.back_button:
        break;
      case R.id.settings_button:
        // intent = new Intent(MainActivity.this,
        // DisplaySettingsActivity.class);
        intent = new Intent(MainActivity.this, DialScreen.class);
        startActivity(intent);
        break;
      case R.id.home_button:
        break;
      case R.id.current_menu_button:
        speakOut("This is " + "the home screen");
        break;
      default:
        break;
    }
  }
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    PhoneNotifications pn = new PhoneNotifications(this);
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
}