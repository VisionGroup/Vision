package com.yp2012g4.vision.tools;

import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.main.MainActivity;
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
   * @param icon
   *          The icon for this activity
   * @param name
   *          The name of the activity
   * @param toolTip
   *          The tool user manual
   */
  public void init(final int icon, final String name, final String toolTip) {
    _icon = icon;
    _name = name;
    _toolTip = toolTip;
  }
  
  /**
   * Dealing control bar on clicks
   */
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    final Intent _intent = new Intent(this, MainActivity.class);
    final View tempLast = last_button_view;
    final View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.back_button:
        _navigationBar = true;
        Log.i(TAG, _name);
        if (_name.equals("Main screen")) {
          speakOutSync(getString(R.string.in_main_screen));
          break;
        }
        speakOutSync(getString(R.string.back_button));
        finish();
        break;
      case R.id.tool_tip_button:
        _navigationBar = true;
        speakOutAsync(getToolTip());
        break;
      case R.id.home_button:
        _navigationBar = true;
        startActivity(_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
  
  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // hide title-bar of application. Must be before setting the layout
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    // hide status-bar of Android. Could also be done later
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    TTS.init(getApplicationContext());
  }
  
  @Override public void onBackPressed() {
    speakOutSync(getString(R.string.back_button));
    finish();
  }
  
  /**
   * 
   * @return
   */
  public View getButtonByMode() {
    View _returnButton = curr_view;
    final SharedPreferences _sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    final String buttonMode = _sp.getString("BUTTON MODE", "regular");
    if (buttonMode.equals("sticky") && last_button_view != null) {
      _returnButton = last_button_view;
      last_button_view = null;
    }
    return _returnButton;
  }
  
  /**
   * Return a Talking button by it's id.
   * 
   * @param id
   *          The id of the button
   * @return The talking button
   */
  public TalkingButton getTalkingButton(final int id) {
    return (TalkingButton) findViewById(id);
  }
  
  /**
   * @param e
   * @param entry
   * @return
   */
  public static boolean checkIfButtonPressed(final MotionEvent e, final Map.Entry<View, Rect> entry) {
    return isButtonType(entry.getKey()) && entry.getValue().contains((int) e.getRawX(), (int) e.getRawY());
  }
}
