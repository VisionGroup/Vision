package com.yp2012g4.blindroid.tools;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/*
 * TODO: Code review More documentation
 */
public abstract class BlindroidActivity extends onTouchEventClass {
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
  
  @Override public void onClick(View arg0) {
    // TODO Auto-generated method stub
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
