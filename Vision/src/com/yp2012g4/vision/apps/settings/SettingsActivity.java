/**
 * @author Maytal
 * 
 */
package com.yp2012g4.vision.apps.settings;

import java.util.Locale;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.VisionApplication;
import com.yp2012g4.vision.apps.calculator.CalcActivity;
import com.yp2012g4.vision.apps.main.MainActivity;
import com.yp2012g4.vision.apps.sos.SOSconfig;
import com.yp2012g4.vision.apps.telephony.IncomingCallReceiver;
import com.yp2012g4.vision.apps.telephony.OutgoingCallReceiver;
import com.yp2012g4.vision.tools.TTS;
import com.yp2012g4.vision.tools.VisionActivity;

public class SettingsActivity extends VisionActivity {
  /**
   * get the activity's main view ID
   * 
   */
  private static final String TAG = "vision:SettingsActivity";
  
  @Override public int getViewId() {
    return R.id.displaySettingsActivity;
  }
  
  /**
   * Adds onSingleTapUp events to buttons in this view.
   * 
   * @param e
   *          - motion event
   * 
   */
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    Intent intent;
    final View button = getButtonByMode();
    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    final PackageManager pm = getPackageManager();
    switch (button.getId()) {
      case R.id.SOS_Change_contact:
        intent = newFlaggedIntent(SettingsActivity.this, SOSconfig.class);
        startActivity(intent);
        break;
      case R.id.Mute_Sound:
        Log.i(TAG, "Muting/Unmuting sound");
        if (VisionApplication.muted) {
          VisionApplication.muted = false;
          speakOutSync(R.string.sound_Activated);
          break;
        }
        // note that the ordering between these two lines is not innocent
        speakOutSync(R.string.sound_muted);
        VisionApplication.muted = true;
        break;
      case R.id.button_set_colors:
        startActivity(newFlaggedIntent(SettingsActivity.this, ColorSettingsActivity.class));
        break;
      case R.id.button_set_theme:
        startActivity(newFlaggedIntent(SettingsActivity.this, ThemeSettingsActivity.class));
        break;
      case R.id.button_exit_launcher:
        pm.clearPackagePreferredActivities(getPackageName());
        vibrate();
        break;
      case R.id.locale:
        pressedLocalSelectButton(sp);
        break;
      // case R.id.button_selecting_mode: TODO: add this button and fix bugs
      // pressedButtonSelectButton(sp);
      // break;
      case R.id.vision_call_enable_button:
        pressedButtonCallEnable(sp);
        vibrate();
        break;
      case R.id.calculator:
        startActivity(newFlaggedIntent(this, CalcActivity.class));
        break;
      default:
        break;
    }
    return false;
  }
  
  private void pressedButtonCallEnable(final SharedPreferences sp) {
    final boolean enable = sp.getBoolean(SetupSettingsString.VisionCallEnableEntry, true);
    // final boolean enable = !buttonMode.equals(ENABLE_PREF);
    VisionApplication.savePrefs(SetupSettingsString.VisionCallEnableEntry, !enable, this);
    if (enable)
      speakOutAsync(R.string.enable_call_service);
    else
      speakOutAsync(R.string.disable_call_service);
    changeEnableState(IncomingCallReceiver.class, enable);
    changeEnableState(OutgoingCallReceiver.class, enable);
  }
  
  private void changeEnableState(final Class<?> cl, final boolean enable) {
    final PackageManager pm = getPackageManager();
    final ComponentName componentName = new ComponentName(getPackageName(), cl.getName());
    final int state = enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    pm.setComponentEnabledSetting(componentName, state, PackageManager.DONT_KILL_APP);
  }
  
  /**
   * @param sp
   */
  private void pressedLocalSelectButton(final SharedPreferences sp) {
    _config = new Configuration();
    // TODO Try to check availability of TTS engines
    final String lang = sp.getString(SetupSettingsString.Language, Language.getDefaultLocale().getLanguage());
    Log.d(TAG, "Current lang: " + lang);
    final Locale nextLocale = Language.availableLocals().get(
        (Language.availableLocals().indexOf(new Locale(lang)) + 1) % Language.availableLocals().size());
    Log.d(TAG, "Next lang: " + nextLocale.getLanguage());
    if (TTS.isLanguageAvailable(nextLocale)) {
      VisionApplication.savePrefs(SetupSettingsString.Language, nextLocale.getLanguage(), this);
      Locale.setDefault(nextLocale);
      speakOutSync(getString(R.string.switched_to) + " " + nextLocale.getLanguage());
      _config.locale = nextLocale;
      getBaseContext().getResources().updateConfiguration(_config, getBaseContext().getResources().getDisplayMetrics());
      Log.d(TAG, "changed to locale: " + nextLocale.getLanguage());
    } else
      speakOutSync(R.string.one_lang_avail);
    // empty activity stack
    final Intent intent = newFlaggedIntent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }
  
//  /**
//   * @param sp
//   */
//  private void pressedButtonSelectButton(final SharedPreferences sp) {
//    final String buttonMode = sp.getString("BUTTON MODE", "regular");
//    if (buttonMode.equals("regular")) {
//      VisionApplication.savePrefs("BUTTON MODE", "sticky", this);
//      speakOutAsync(R.string.sticky_buttons_mode);
//    } else {
//      VisionApplication.savePrefs("BUTTON MODE", "regular", this);
//      speakOutAsync(R.string.regular_buttons_mode);
//    }
//  }
  /**
   * Called when the activity is first created.
   */
  /** */
  @Override public void onCreate(final Bundle savedInstanceState) {
    Log.i(TAG, "DisplaySettings:: onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    init(0, getString(R.string.settings_screen), getString(R.string.settings_help));
  }
}
