package com.yp2012g4.vision.tools;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.yp2012g4.vision.MainActivity;
import com.yp2012g4.vision.R;

/*
 * write by Yaron Auster
 * 
 * The VisionActivity extend the normal activity.
 * it's give extra functionality to this app.
 */
public abstract class VisionActivity extends VisionGestureDetector {
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
   * Initialization the class
   * 
   * @param activity The activity it's run
   * @param icon The icon for this activity
   * @param name The name of the activity
   * @param toolTip The tool user manual 
   */
  @Deprecated public void init(Activity activity, int icon, String name, String toolTip) {
    // _t = new TTS(activity, (OnInitListener) activity);
    _icon = icon;
    _name = name;
    _toolTip = toolTip;
  }
  
  /**
   * Initialization the class
   * 
   * @param icon The icon for this activity
   * @param name The name of the activity
   * @param toolTip The tool user manual 
   */
  public void init(int icon, String name, String toolTip) {
    _icon = icon;
    _name = name;
    _toolTip = toolTip;
  }
  
  /**
   * Dealing control bar on clicks
   */
  @Override public boolean onSingleTapUp(MotionEvent e) {
    Intent intent = new Intent(this, MainActivity.class);
    switch (curr_view.getId()) {
      case R.id.back_button:
        clickFlag = true;
        Log.i("MyLog", _name);
        if (_name.equals("Main screen")) {
          speakOut("In main screen");
          break;
        }
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.tool_tip_button:
        clickFlag = true;
        speakOut(getToolTip());
        break;
      case R.id.home_button:
        clickFlag = true;
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.current_menu_button:
        clickFlag = true;
        speakOut("This is " + _name);
        break;
      default:
        break;
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
  
  @Override public void onBackPressed() {
    speakOut("Previous screen");
    mHandler.postDelayed(mLaunchTask, 1000);
  }
}
