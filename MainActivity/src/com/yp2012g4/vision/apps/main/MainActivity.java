package com.yp2012g4.vision.apps.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.SOS.SOSActivity;
import com.yp2012g4.vision.apps.alarm.AlarmActivity;
import com.yp2012g4.vision.apps.clock.SpeakingClockActivity;
import com.yp2012g4.vision.apps.contacts.ContactsMenuActivity;
import com.yp2012g4.vision.apps.phoneStatus.PhoneNotifications;
import com.yp2012g4.vision.apps.phoneStatus.PhoneStatusActivity;
import com.yp2012g4.vision.apps.settings.DisplaySettingsActivity;
import com.yp2012g4.vision.apps.smsSender.SendSMSActivity;
import com.yp2012g4.vision.apps.whereAmI.WhereAmIActivity;
import com.yp2012g4.vision.managers.CallManager;
import com.yp2012g4.vision.managers.SmsManager;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * 
 * The Main activity.
 * 
 */
public class MainActivity extends VisionActivity {
  private static final double LOW_BATTERY_LEVEL = 0.3;
  private static String TAG = "vision:MainActivity";
  
  @Override public int getViewId() {
    return R.id.MainActivityView;
  }
  
  /** Called when the activity is first created. */
  @Override public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "MainActivity:: onCreate");
    setContentView(R.layout.activity_main);
    adjustLayoutSize(4);
    final PhoneNotifications pn = new PhoneNotifications(this);
    init(0, getString(R.string.MainActivity_wheramai), getString(R.string.MainActivity_help));
    pn.startSignalLisener();
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    if (_navigationBar)
      return _navigationBar = false;
    final Intent intent = getIntentByButtonId(MainActivity.this, getButtonByMode().getId());
    if (intent != null) {
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
      startActivity(intent);
    }
    return false;
  }
  
  static private Intent getIntentByButtonId(final Context c, final int id) {
    final Class<?> nextActivity;
    switch (id) {
      case R.id.sos_button:
        nextActivity = SOSActivity.class;
        break;
      case R.id.time_button:
        nextActivity = SpeakingClockActivity.class;
        break;
      case R.id.where_am_i_button:
        nextActivity = WhereAmIActivity.class;
        break;
      case R.id.phone_status_button:
        nextActivity = PhoneStatusActivity.class;
        break;
      case R.id.alarm_clock_button:
        nextActivity = AlarmActivity.class;
        break;
      case R.id.contacts_button:
        nextActivity = ContactsMenuActivity.class;
        break;
      case R.id.setting_button:
        nextActivity = DisplaySettingsActivity.class;
        break;
      case R.id.read_sms_button:
//        nextActivity = ReadSmsActivity.class;
        nextActivity = SendSMSActivity.class;
        break;
      default:
        return null;
    }
    return new Intent(c, nextActivity);
  }
  
  /**
   * Perform actions when the window get into focus we start the activity by
   * reading out loud the current title
   */
  @Override public void onWindowFocusChanged(final boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    _tts.waitUntilFinishTalking();
    if (!hasFocus)
      return;
    VoiceNotify();
  }
  
  public void VoiceNotify() {
    String s = "";
    final PhoneNotifications pn = new PhoneNotifications(this);
    final float batteryLevel = pn.getBatteryLevel();
    final boolean isCharging = pn.getChargerStatus();
    if (!isCharging && batteryLevel < LOW_BATTERY_LEVEL)
      s += getString(R.string.low_battery);
    final int signalS = PhoneNotifications.getSignalStrength();
    if (signalS <= PhoneStatusActivity.signal_noSignalThreshold)
      s += getString(R.string.phoneStatus_message_noSignal_read) + "\n";
    else if (signalS <= PhoneStatusActivity.signal_poor)
      s += getString(R.string.phoneStatus_message_veryPoorSignal_read) + "\n";
    final int numOfMissedCalls = CallManager.getMissedCallsNum(this);
    if (numOfMissedCalls > 0) {
      s += numOfMissedCalls + " " + getString(R.string.missed_call);
      if (numOfMissedCalls > 1)
        s += "s";
    }
    final int numOfSms = SmsManager.getUnreadSMS(this);
    if (numOfSms > 0)
      s += numOfSms + getString(R.string.new_sms);
    speakOutAsync(s);
    _tts.waitUntilFinishTalking();
  }
  
  @Override public void onBackPressed() {
    speakOutAsync(getString(R.string.in_main_screen));
  }
}