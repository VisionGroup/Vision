/**
 * An activity offering the option to change the text size
 * 
 * @author Maytal
 * 
 */
package com.yp2012g4.vision;

import android.os.Bundle;
import android.view.MotionEvent;

import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;

public class ThemeSettingsActivity extends VisionActivity {
  /**
   * get the activity's main view ID
   * 
   */
  @Override
  public int getViewId() {
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
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    if (curr_view instanceof TalkingButton)
      speakOut(((TalkingButton) curr_view).getReadText());
    switch (curr_view.getId()) {
      case R.id.Small_text_size_button:
        VisionApplication.textSize = VisionApplication.SMALL;
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.Normal_text_size_button:
        VisionApplication.textSize = VisionApplication.NORMAL;
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.Large_text_size_button:
        VisionApplication.textSize = VisionApplication.LARGE;
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      default:
        break;
    }
    return false;
  }
  
  /**
   * Called when the activity is first created.
   * */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_theme_settings);
    init(0, getString(R.string.theme_settings_screen), getString(R.string.size_setting_help));
  }
}