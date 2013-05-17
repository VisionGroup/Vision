/**
 * An Activity allowing the user to choose between different color themes.
 * 
 * @author Maytal
 * 
 */
package com.yp2012g4.vision.settings;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;

public class ColorSettingsActivity extends VisionActivity {
  private final static int BUTTON_NUM = 7;
  
  /**
   * set the text and background colors for the entire application
   * 
   * @param int1
   *          - text Color
   * @param int2
   *          - background Color
   */
  private void changeSettings(final String s) {
    final String[] c = s.split("-");
    VisionApplication.savePrefs("TEXT COLOR", c[0], this);
    VisionApplication.savePrefs("BG COLOR", c[1], this);
    VisionApplication.loadPrefs(this);
  }
  
  /**
   * get the activity's main view ID
   * 
   */
  @Override public int getViewId() {
    return R.id.ColorSettingsActivity;
  }
  
  /**
   * Adds onSingleTapUp events to buttons in this view.
   * 
   * @param e
   *          - motion event
   */
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    if (_navigationBar)
      return _navigationBar = false;
    final View v = getButtonByMode();
    if (v instanceof TalkingButton) {
      speakOutSync(((TalkingButton) v).getReadText());
      final String s = ((TalkingButton) v).getPrefsValue();
      if (!s.equals(""))
        changeSettings(s);
      finish();
      return true;
    }
    return false;
  }
  
  /**
   * onCreate method.
   */
  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_color_settings);
    adjustLayoutSize(BUTTON_NUM);
    init(0, getString(R.string.color_settings_screen), getString(R.string.color_setting_help));
  }
}
