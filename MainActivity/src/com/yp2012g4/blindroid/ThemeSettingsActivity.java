/**
 * An activity offering the option to change the text size
 * 
 * @author Maytal
 * 
 */
package com.yp2012g4.blindroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.tools.BlindroidActivity;

public class ThemeSettingsActivity extends BlindroidActivity {
  /**
   * get the activity's main view ID
   * 
   */
  @Override public int getViewId() {
    return R.id.ThemeSettingsActivity;
  }
  
  /**
   * Adds onClick events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   * 
   * @param v
   *          - a View object on the screen
   */
  @Override public void onClick(View v) {
    Intent intent = new Intent(ThemeSettingsActivity.this, MainActivity.class);
    if (v instanceof TalkingButton)
      speakOut(((TalkingButton) v).getReadText());
    switch (v.getId()) {
      case R.id.Small_text_size_button:
        DisplaySettings.THEME = "SMALL";
        DisplaySettings.textSize = DisplaySettingsApplication.SMALL;
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.Normal_text_size_button:
        DisplaySettings.THEME = "DEFAULT";
        DisplaySettings.textSize = DisplaySettingsApplication.NORMAL;
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.Large_text_size_button:
        DisplaySettings.THEME = "LARGE";
        DisplaySettings.textSize = DisplaySettingsApplication.LARGE;
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.settings_button:
        speakOut("Settings");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.back_button:
        speakOut("Previous screen");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.home_button:
        speakOut("Home");
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.current_menu_button:
        speakOut("This is " + getString(R.string.title_activity_theme_settings));
        break;
      default:
        super.onClick(v);
    }
  }
  
  /**
   * Called when the activity is first created.
   * */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_theme_settings);
  }
}
