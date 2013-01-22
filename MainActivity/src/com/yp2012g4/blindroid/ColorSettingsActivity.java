/**
 * An Activity allowing the user to choose between different color themes.
 * 
 * @author Maytal
 * 
 */
package com.yp2012g4.blindroid;

import android.os.Bundle;
import android.view.MotionEvent;
import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.tools.BlindroidActivity;

public class ColorSettingsActivity extends BlindroidActivity {
  /**
   * set the text and background colors for the entire application
   * 
   * @param int1
   *          - text Color
   * @param int2
   *          - background Color
   */
  private static void changeSettings(int int1, int int2) {
    DisplaySettings.setColors(int1, int2);
  }
  
  /**
   * get the activity's main view ID
   * 
   */
  @Override
  public int getViewId() {
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
    if (curr_view instanceof TalkingButton)
      speakOut(((TalkingButton) curr_view).getReadText());
    switch (curr_view.getId()) {
      case R.id.WhiteBlack:
        changeSettings(R.color.WHITE, R.color.BLACK);
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.WhiteRed:
        changeSettings(R.color.WHITE, R.color.RED);
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.RedBlack:
        changeSettings(R.color.RED, R.color.BLACK);
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.WhiteGreen:
        changeSettings(R.color.WHITE, R.color.GREEN);
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.GreenBlack:
        changeSettings(R.color.GREEN, R.color.BLACK);
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.WhiteBlue:
        changeSettings(R.color.WHITE, R.color.BLUE);
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.BlueBlack:
        changeSettings(R.color.BLUE, R.color.BLACK);
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      default:
        super.onSingleTapUp(e);
    }
    return false;
  }
  
  /**
   * onCreate method.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_color_settings);
    init(0, getString(R.string.color_settings_screen), getString(R.string.color_setting_help));
  }
  
}
