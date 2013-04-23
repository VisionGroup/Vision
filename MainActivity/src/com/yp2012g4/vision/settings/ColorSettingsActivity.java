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
  /**
   * set the text and background colors for the entire application
   * 
   * @param int1
   *          - text Color
   * @param int2
   *          - background Color
   */
  private void changeSettings(String s1, String s2) {
    VisionApplication.savePrefs("TEXT COLOR", s1, this);
    VisionApplication.savePrefs("BG COLOR", s2, this);
    VisionApplication.loadPrefs(this);
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
   * Adds onSingleTapUp events to buttons in this view.
   * 
   * @param e
   *          - motion event
   */
  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    if (clickFlag) {
      clickFlag = false;
      return false;
    }
    View button = getButtonByMode();
    if (button instanceof TalkingButton)
      speakOut(((TalkingButton) button).getReadText());
    while (_t.isSpeaking() == true) {
      // Wait for message to finish playing and then finish the activity
    }
    switch (button.getId()) {
      case R.id.WhiteBlack:
        changeSettings("WHITE", "BLACK");
        break;
      case R.id.WhiteRed:
        changeSettings("WHITE", "RED");
        break;
      case R.id.RedBlack:
        changeSettings("RED", "BLACK");
        break;
      case R.id.WhiteGreen:
        changeSettings("WHITE", "GREEN");
        break;
      case R.id.GreenBlack:
        changeSettings("GREEN", "BLACK");
        break;
      case R.id.WhiteBlue:
        changeSettings("WHITE", "BLUE");
        break;
      case R.id.BlueBlack:
        changeSettings("BLUE", "BLACK");
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
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_color_settings);
    adjustLayoutSize(7);
    init(0, getString(R.string.color_settings_screen), getString(R.string.color_setting_help));
  }
}
