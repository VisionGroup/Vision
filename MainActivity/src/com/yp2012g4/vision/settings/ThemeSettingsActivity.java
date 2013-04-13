/**
 * An activity offering the option to change the text size
 * 
 * @author Maytal
 * 
 */
package com.yp2012g4.vision.settings;

import android.os.Bundle;
import android.view.MotionEvent;

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
   * @param e - motion event
   * 
   */
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    if (curr_view instanceof TalkingButton)
      speakOut(((TalkingButton) curr_view).getReadText());
    while (_t.isSpeaking() == true) {
      // Wait for message to finish playing and then finish the activity
    }
    if (curr_view.getId() == R.id.Small_text_size_button ||
    	curr_view.getId() == R.id.Normal_text_size_button ||
    	curr_view.getId() == R.id.Large_text_size_button) {
    	VisionApplication.savePrefs("TEXT_SIZE", ((TalkingButton) curr_view).getReadText(), this);
        mHandler.postDelayed(mLaunchTask, 1000);
    }
    return false;
  }
  
  /**
   * Called when the activity is first created.
   * */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_theme_settings);
    adjustLayoutSize(3);
    init(0, getString(R.string.theme_settings_screen), getString(R.string.size_setting_help));
  }
}
