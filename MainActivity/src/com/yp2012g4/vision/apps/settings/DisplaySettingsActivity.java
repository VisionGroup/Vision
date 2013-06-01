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
import com.yp2012g4.vision.apps.calculator.CalcActivity;
import com.yp2012g4.vision.apps.main.MainActivity;
import com.yp2012g4.vision.apps.sos.SOSconfig;
import com.yp2012g4.vision.tools.IncomingCallReceiver;
import com.yp2012g4.vision.tools.OutgoingCallReceiver;
import com.yp2012g4.vision.tools.VisionActivity;

public class DisplaySettingsActivity extends VisionActivity {
  private static final String VISION_CALL_ENABLE_ENTRY = "VISION CALL ENABLE";
  private static final String DISABLE_PREF = "disable";
  private static final String ENABLE_PREF = "enable";
  /**
   * get the activity's main view ID
   * 
   */
  private static final String TAG = "vision:DisplaySettingsActivity";
  
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
    Log.i(TAG, "display setting");
    final View button = getButtonByMode();
    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    final PackageManager pm = getPackageManager();
    switch (button.getId()) {
      case R.id.SOS_Change_contact:
        startActivity(new Intent(DisplaySettingsActivity.this, SOSconfig.class));
        break;
      case R.id.button_set_colors:
        startActivity(new Intent(DisplaySettingsActivity.this, ColorSettingsActivity.class));
        break;
      case R.id.button_set_theme:
        startActivity(new Intent(DisplaySettingsActivity.this, ThemeSettingsActivity.class));
        break;
      case R.id.button_exit_launcher:
        pm.clearPackagePreferredActivities(getPackageName());
        vibrate(VIBRATE_DURATION);
        break;
      case R.id.locale:
        pressedLocalSelectButton(sp);
        break;
      // case R.id.button_selecting_mode: TODO: add this button and fixed bugs
      // pressedButtonSelectButton(sp);
      // break;
      case R.id.vision_call_enable_button:
        pressedButtonCallEnable(sp);
        break;
      case R.id.calculator:
        startActivity(new Intent(this, CalcActivity.class));
        break;
      default:
        break;
    }
    return false;
  }
  
  private void pressedButtonCallEnable(final SharedPreferences sp) {
    final String buttonMode = sp.getString(VISION_CALL_ENABLE_ENTRY, ENABLE_PREF);
    boolean enable = false;
    if (buttonMode.equals(ENABLE_PREF)) {
      VisionApplication.savePrefs(VISION_CALL_ENABLE_ENTRY, DISABLE_PREF, this);
      speakOutAsync(getString(R.string.disable_call_service));
    } else {
      VisionApplication.savePrefs(VISION_CALL_ENABLE_ENTRY, ENABLE_PREF, this);
      enable = true;
      speakOutAsync(getString(R.string.enable_call_service));
    }
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
    _myLocale = Locale.getDefault(); // get xml strings file
    _config = new Configuration();
    String defaultLang = "HEBREW";
    if (_myLocale.equals(Locale.US))
      defaultLang = "ENGLISH";
    final String language = sp.getString("LANGUAGE", defaultLang);
    if (language.equals("ENGLISH")) {
      VisionApplication.savePrefs("LANGUAGE", "HEBREW", this);
      final Locale locale = new Locale("iw");
      Locale.setDefault(locale);
      _config.locale = locale;
      speakOutAsync(getString(R.string.switched_to_hebrew));
    } else {
      VisionApplication.savePrefs("LANGUAGE", "ENGLISH", this);
      Locale.setDefault(Locale.US);
      _config.locale = Locale.US;
      speakOutAsync(getString(R.string.switched_to_english));
    }
    getBaseContext().getResources().updateConfiguration(_config, getBaseContext().getResources().getDisplayMetrics());
    // empty activity stack
    startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    finish();
  }
  
//  /**
//   * @param sp
//   */
//  private void pressedButtonSelectButton(final SharedPreferences sp) {
//    final String buttonMode = sp.getString("BUTTON MODE", "regular");
//    if (buttonMode.equals("regular")) {
//      VisionApplication.savePrefs("BUTTON MODE", "sticky", this);
//      speakOutAsync(getString(R.string.sticky_buttons_mode));
//    } else {
//      VisionApplication.savePrefs("BUTTON MODE", "regular", this);
//      speakOutAsync(getString(R.string.regular_buttons_mode));
//    }
//  }
  /**
   * Called when the activity is first created.
   */
  /** */
  @Override public void onCreate(final Bundle savedInstanceState) {
    Log.i(TAG, "DisplaySettings:: onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_settings);
    init(0, getString(R.string.display_settings_screen), getString(R.string.settings_help));
  }
}
