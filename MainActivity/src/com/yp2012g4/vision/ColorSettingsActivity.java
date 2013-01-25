/**
 * An Activity allowing the user to choose between different color themes.
 * 
 * @author Maytal
 * 
 */
package com.yp2012g4.vision;

import android.os.Bundle;
import android.view.MotionEvent;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;

public class ColorSettingsActivity extends VisionActivity {
  /**
   * set the text and background colors for the entire application
   * 
   * @param int1
   *          - text Color
   * @param int2
   *          - background Color
   */
  private static void changeSettings(int int1, int int2) {
    VisionApplication.setColors(int1, int2);
  }
  
  /**
   * get the activity's main view ID
   * 
   */
  @Override public int getViewId() {
    return R.id.ColorSettingsActivity;
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
    if (clickFlag){
      clickFlag = false;
      return false;
    }
    if (curr_view instanceof TalkingButton)
      speakOut(((TalkingButton) curr_view).getReadText());
    switch (curr_view.getId()) {
      case R.id.WhiteBlack:
        changeSettings(R.color.WHITE, R.color.BLACK);
        break;
      case R.id.WhiteRed:
        changeSettings(R.color.WHITE, R.color.RED);
        break;
      case R.id.RedBlack:
        changeSettings(R.color.RED, R.color.BLACK);
        break;
      case R.id.WhiteGreen:
        changeSettings(R.color.WHITE, R.color.GREEN);
        break;
      case R.id.GreenBlack:
        changeSettings(R.color.GREEN, R.color.BLACK);
        break;
      case R.id.WhiteBlue:
        changeSettings(R.color.WHITE, R.color.BLUE);
        break;
      case R.id.BlueBlack:
        changeSettings(R.color.BLUE, R.color.BLACK);
        break;
      default:
        break;
    }
    mHandler.postDelayed(mLaunchTask, 1000);
    return false;
  }
  
  /**
   * onCreate method.
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_color_settings);
    init(0, getString(R.string.color_settings_screen), getString(R.string.color_setting_help));
  }
  
}