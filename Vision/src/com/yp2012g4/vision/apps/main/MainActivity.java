package com.yp2012g4.vision.apps.main;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.VisionApplication;
import com.yp2012g4.vision.apps.alarm.AlarmActivity;
import com.yp2012g4.vision.apps.clock.SpeakingClockActivity;
import com.yp2012g4.vision.apps.contacts.ContactsMenuActivity;
import com.yp2012g4.vision.apps.phoneStatus.PhoneNotifications;
import com.yp2012g4.vision.apps.phoneStatus.PhoneStatusActivity;
import com.yp2012g4.vision.apps.settings.Language;
import com.yp2012g4.vision.apps.settings.SettingsActivity;
import com.yp2012g4.vision.apps.settings.SetupSettingsString;
import com.yp2012g4.vision.apps.smsReader.ReadSmsActivity;
import com.yp2012g4.vision.apps.sos.SOSActivity;
import com.yp2012g4.vision.apps.telephony.CallService;
import com.yp2012g4.vision.apps.whereAmI.WhereAmIActivity;
import com.yp2012g4.vision.managers.CallsManager;
import com.yp2012g4.vision.managers.SmsManager;
import com.yp2012g4.vision.tools.TTS;
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
    programSetup();
    setContentView(R.layout.activity_main);
    init(0, getString(R.string.MainActivity_wheramai), getString(R.string.MainActivity_help));
//    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//      getActionBar().hide();
  }
  
  private void programSetup() {
    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    final Locale l = new Locale(sp.getString(SetupSettingsString.Language, Language.getDefaultLocale().getLanguage()));
    Locale.setDefault(l);
    Log.d(TAG, "Language: " + l.getLanguage());
    final int ret = TTS.setLanguage(l);
    Log.d(TAG, "SetLanguage result=" + ret);
    _config = new Configuration();
    _config.locale = l;
    VisionApplication.savePrefs(SetupSettingsString.Language, l.getLanguage(), this);
    getBaseContext().getResources().updateConfiguration(_config, getBaseContext().getResources().getDisplayMetrics());
    final PhoneNotifications pn = new PhoneNotifications(this);
    pn.startSignalLisener();
    CallService.initialise(getApplicationContext());
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final Intent intent = getIntentByButtonId(MainActivity.this, getButtonByMode().getId());
    if (intent != null) {
      setIntentFlags(intent);
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
        nextActivity = SettingsActivity.class;
        break;
      case R.id.read_sms_button:
        nextActivity = ReadSmsActivity.class;
//        nextActivity = SendSMSActivity.class;
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
    // TTS.waitUntilFinishTalking(); // <- removing this increases speed, but
    // may cause other problems
    if (!hasFocus)
      return;
    VoiceNotify(this);
  }
  
  public static void VoiceNotify(final Context c) {
    final CallsManager callsManager = new CallsManager(c);
    String s = "";
    final PhoneNotifications pn = new PhoneNotifications(c);
    final float batteryLevel = pn.getBatteryLevel();
    final boolean isCharging = pn.getChargerStatus();
    if (!isCharging && batteryLevel < LOW_BATTERY_LEVEL)
      s += c.getString(R.string.low_battery);
    final int signalS = PhoneNotifications.getSignalStrength();
    if (signalS <= PhoneStatusActivity.signal_noSignalThreshold)
      s += c.getString(R.string.phoneStatus_message_noSignal_read) + "\n";
    else if (signalS <= PhoneStatusActivity.signal_poor)
      s += c.getString(R.string.phoneStatus_message_veryPoorSignal_read) + "\n";
    final int numOfMissedCalls = callsManager.getMissedCallsNum();
    final Resources res = c.getResources();
    if (numOfMissedCalls > 0)
      s += res.getQuantityString(R.plurals.numberOfMissedCalls, numOfMissedCalls, Integer.valueOf(numOfMissedCalls));
    final int numOfSms = SmsManager.getUnreadSMS(c);
    if (numOfSms > 0)
      s += res.getQuantityString(R.plurals.numberOfNewSMS, numOfSms, Integer.valueOf(numOfSms));
    TTS.speak(s, TextToSpeech.QUEUE_ADD);
  }
  
  @Override public void onBackPressed() {
    speakOutAsync(R.string.in_main_screen);
  }
}
