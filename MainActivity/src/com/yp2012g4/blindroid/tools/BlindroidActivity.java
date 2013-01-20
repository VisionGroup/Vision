package com.yp2012g4.blindroid.tools;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.yp2012g4.blindroid.DisplaySettingsActivity;
import com.yp2012g4.blindroid.R;

/*
 * TODO: Code review More documentation
 */
public abstract class BlindroidActivity extends VisionGestureDetector {
  private int _icon;
  private String _name;
  private String _toolTip;
  
  public int getIcon() {
    return _icon;
  }
  
  public String getName() {
    return _name;
  }
  
  public String getToolTip() {
    return _toolTip;
  }
  
  /**
   * replaced by void init(int icon, String name, String toolTip)
   * 
   * @param activity
   * @param icon
   * @param name
   * @param toolTip
   */
  @Deprecated public void init(Activity activity, int icon, String name, String toolTip) {
    // _t = new TTS(activity, (OnInitListener) activity);
    _icon = icon;
    _name = name;
    _toolTip = toolTip;
  }
  
  public void init(int icon, String name, String toolTip) {
    _icon = icon;
    _name = name;
    _toolTip = toolTip;
  }
  
  /**
   * Dealing control bar on clicks
   */
  @Override public boolean onSingleTapUp(MotionEvent e) {
    switch (curr_view.getId()) {
      case R.id.back_button:
        speakOut("Previous screen");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.settings_button:
        speakOut("Settings");
        if (isSettingsScreen()) {
          speakOut("you are in settings screen");
          break;
        }
        Intent intent = new Intent(this, DisplaySettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.home_button:
        speakOut("Home");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.current_menu_button:
        speakOut("This is " + findViewById(getViewId()).getContentDescription().toString());
        break;
      default:
        break;
    }
    return false;
  }
  
  private boolean isSettingsScreen() {
    String s = findViewById(getViewId()).getContentDescription().toString();
    
    if (s.equals("Color settings screen") || s.equals("Display settings screen") || s.equals("Theme settings screen")) {
      return true;
    }
    return false;
  }
  
  @Override public int getViewId() {
    // TODO Auto-generated method stub
    return 0;
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // hide titlebar of application
    // must be before setting the layout
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    // hide statusbar of Android
    // could also be done later
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
  }
}
