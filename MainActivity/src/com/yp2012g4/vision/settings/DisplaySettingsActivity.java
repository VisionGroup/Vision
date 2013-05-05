/**
 * @author Maytal
 * 
 */
package com.yp2012g4.vision.settings;

import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.CalcActivity;
import com.yp2012g4.vision.MainActivity;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.tools.VisionActivity;

public class DisplaySettingsActivity extends VisionActivity {
  /**
   * get the activity's main view ID
   * 
   */
  @Override public int getViewId() {
    return R.id.displaySettingsActivity;
  }
  
  /**
   * finish activity when back button pressed
   */
  @Override public void onBackPressed() {
    // TODO Auto-generated method stub
    super.onBackPressed();
    DisplaySettingsActivity.this.finish();
  }
  
  /**
   * Adds onSingleTapUp events to buttons in this view.
   * 
   * @param e
   *          - motion event
   * 
   */
  @Override public boolean onSingleTapUp(MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    Intent intent;
    View button = getButtonByMode();
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    switch (button.getId()) {
      case R.id.button_set_colors:
        intent = new Intent(DisplaySettingsActivity.this, ColorSettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.button_set_theme:
        intent = new Intent(DisplaySettingsActivity.this, ThemeSettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.button_exit_launcher:
        PackageManager pm = getPackageManager();
        pm.clearPackagePreferredActivities(getPackageName());
        vibrate(300);
        break;
      case R.id.locale:
        myLocale = Locale.getDefault(); // get xml strings file
        config = new Configuration();
        String defaultLang = "HEBREW";
        if (myLocale.equals(Locale.US))
          defaultLang = "ENGLISH";
        String language = sp.getString("LANGUAGE", defaultLang);
        if (language.equals("ENGLISH")) {
          VisionApplication.savePrefs("LANGUAGE", "HEBREW", this);
          Locale locale = new Locale("iw");
          Locale.setDefault(locale);
          config.locale = locale;
          speakOut(getString(R.string.switched_to_hebrew));
        } else {
          VisionApplication.savePrefs("LANGUAGE", "ENGLISH", this);
          Locale.setDefault(Locale.US);
          config.locale = Locale.US;
          speakOut(getString(R.string.switched_to_english));
        }
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        intent = new Intent(this, MainActivity.class);
        // setResult(RESULT_OK, null);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); // empty
                                                                        // activity
                                                                        // stack
        finish();
        break;
      case R.id.button_selecting_mode:
        String buttonMode = sp.getString("BUTTON MODE", "regular");
        if (buttonMode.equals("regular")) {
          VisionApplication.savePrefs("BUTTON MODE", "sticky", this);
          speakOut(getString(R.string.sticky_buttons_mode));
        } else {
          VisionApplication.savePrefs("BUTTON MODE", "regular", this);
          speakOut(getString(R.string.regular_buttons_mode));
        }
        break;
      case R.id.calculator:
        intent = new Intent(this, CalcActivity.class);
        startActivity(intent/* .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) */);
    }
    return false;
  }
  
  /**
   * Called when the activity is first created.
   */
  /** */
  @Override public void onCreate(Bundle savedInstanceState) {
    Log.i("MyLog", "DisplaySettings:: onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_settings);
    // adjustLayoutSize(3);
    init(0, getString(R.string.display_settings_screen), getString(R.string.settings_help));
  }
}
