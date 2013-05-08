package com.yp2012g4.vision.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.yp2012g4.vision.MainActivity;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;

/*
 * write by Yaron Auster
 * 
 * The VisionActivity extend the normal activity. it's give extra functionality
 * to this app.
 */
public abstract class VisionActivity extends VisionGestureDetector {
  private static final String TAG = "vision:VisionActivity";
  private int _icon;
  private String _name;
  private String _toolTip;
  protected Vibrator vibrator = null;
  
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
   * @param activity
   *          The activity it's run
   * @param icon
   *          The icon for this activity
   * @param name
   *          The name of the activity
   * @param toolTip
   *          The tool user manual
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
   * @param icon
   *          The icon for this activity
   * @param name
   *          The name of the activity
   * @param toolTip
   *          The tool user manual
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
    final Intent intent = new Intent(this, MainActivity.class);
    View tempLast = last_button_view;
    View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.back_button:
        _navigationBar = true;
        Log.i(TAG, _name);
        if (_name.equals("Main screen")) {
          speakOutAsync(getString(R.string.in_main_screen));
          break;
        }
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.tool_tip_button:
        _navigationBar = true;
        speakOutAsync(getToolTip());
        break;
      case R.id.home_button:
        _navigationBar = true;
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
        break;
      case R.id.current_menu_button:
        _navigationBar = true;
        speakOutAsync(getString(R.string.this_is) + " " + _name);
        break;
      default:
        last_button_view = tempLast;
        return false;
    }
    return true;
  }
  
  @Override public int getViewId() {
    // TODO Auto-generated method stub
    return 0;
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    // hide titlebar of application
    // must be before setting the layout
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    // hide statusbar of Android
    // could also be done later
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
  }
  
  @Override public void onBackPressed() {
    speakOutAsync(getString(R.string.previous_screen));
    mHandler.postDelayed(mLaunchTask, 1000);
  }
  
  /***
   * 
   * @param numOfLayouts
   *          - the number of rows in the activity
   */
  public void adjustLayoutSize(int numOfLayouts) {
    Display display = getWindowManager().getDefaultDisplay();
    float density = getResources().getDisplayMetrics().density;
    int height = display.getHeight() - (int) (60 * density);
    for (int i = 1; i <= numOfLayouts; i++) {
      int resID = getResources().getIdentifier("layout" + i, "id", getPackageName());
      LinearLayout ll = (LinearLayout) findViewById(resID);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ll.getLayoutParams());
      params.height = height / numOfLayouts;
      ll.setLayoutParams(params);
    }
  }
  
  public View getButtonByMode() {
    View returnButton = curr_view;
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    String buttonMode = sp.getString("BUTTON MODE", "regular");
    if (buttonMode.equals("sticky") && last_button_view != null) {
      returnButton = last_button_view;
      last_button_view = null;
    }
    return returnButton;
  }
  
  /**
   * Make the phone vibrate.
   * 
   * @param milliseconds
   *          The time to vibrate.
   */
  protected void vibrate(long milliseconds) {
    vibrator.vibrate(milliseconds);
  }
  
  /**
   * Return a Talking button by it's id.
   * 
   * @param id
   *          The id of the button
   * @return The talking button
   */
  public TalkingButton getTalkingButton(int id) {
    return getTalkingButton(id);
  }
}
