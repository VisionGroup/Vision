/**
 * An activity offering the option to change the text size
 * 
 * @author Maytal
 * 
 */
package com.yp2012g4.vision.apps.settings;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;

public class ThemeSettingsActivity extends VisionActivity {
  /**
   * get the activity's main view ID
   * 
   */
  @Override public int getViewId() {
    return R.id.ThemeSettingsActivity;
  }
  
  /**
   * Adds onSingleTapUp events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   * 
   * @param e
   *          - motion event
   * 
   */
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View v = getButtonByMode();
    if (v instanceof TalkingButton)
      speakOutSync(((TalkingButton) v).getReadText());
    switch (v.getId()) {
      case R.id.Small_text_size_button:
      case R.id.Normal_text_size_button:
      case R.id.Large_text_size_button:
        VisionApplication.savePrefs("TEXT SIZE", ((TalkingButton) v).getPrefsValue(), this);
        finish();
        break;
      default:
        break;
    }
    return false;
  }
  
  /**
   * Called when the activity is first created.
   * */
  @Override public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_theme_settings);
    init(0, getString(R.string.theme_settings_screen), getString(R.string.size_setting_help));
  }
}
