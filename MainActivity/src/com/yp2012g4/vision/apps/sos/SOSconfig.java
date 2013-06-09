package com.yp2012g4.vision.apps.sos;

import java.util.Map;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * Number configuration for SOS screen;
 * 
 * @author Roman
 * @version 1.1
 */
public class SOSconfig extends VisionActivity {
  /**
   * max length of dialed number
   */
  public final static int MAX_LENGTH = 20;
  /**
   * the number dialed
   */
  private String dialed_number = "";
  /**
   * a string representing the number to be read
   */
  private String read_number = "";
  
  /**
   * get the id of the main layout
   */
  @Override public int getViewId() {
    return R.id.SosConfig;
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    if (e.getAction() == MotionEvent.ACTION_UP)
      for (final Map.Entry<View, Rect> entry : getView_to_rect())
        if (checkIfButtonPressed(e, entry))
          speakOutAsync(textToRead(entry.getKey()));
    return true;
  }
  
  /**
   * Handle the different actions available in this activity
   * 
   * @param v
   *          - the last button pressed before lifting the finger
   */
  @Override public void onActionUp(final View v) {
    final int buttonId = v.getId();
    if (isNavigationMenuButton(buttonId))
      return;
    switch (buttonId) {
      case R.id.button_ok:
        if (dialed_number == "") {
          speakOutAsync(getString(R.string.SOS_number_empty));
          break;
        }
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString(getString(R.string.sos_number), dialed_number).commit();
        finish();
        break;
      case R.id.number: // user wished to hear the number, no action needed.
        return;
      case R.id.button_reset: // reset
        pressedResetButton();
        break;
      case R.id.button_delete: // delete
        pressedDeleteButton(buttonId);
        break;
      default:
        break;
    }
    if (dialed_number.length() == MAX_LENGTH) {
      dialed_number = dialed_number.substring(0, Math.max(0, dialed_number.length() - 1));
      read_number = read_number.substring(0, Math.max(0, read_number.length() - 2));
    }
    // a number or sign has been chosen
    if (((View) v.getParent().getParent()).getId() == R.id.DialScreenNumbers) {
      dialed_number += ((TalkingButton) v).getText();
      read_number = read_number + ((TalkingButton) v).getText() + " ";
    }
    // Vibrate for 150 milliseconds
    vibrate(VIBRATE_DURATION);
    getTalkingButton(R.id.number).setText(dialed_number.toCharArray(), 0, dialed_number.length());
    getTalkingButton(R.id.number).setReadText(read_number);
  }
  
  /**
   * 
   */
  private void pressedResetButton() {
    dialed_number = "";
    read_number = "";
  }
  
  /**
   * @param buttonId
   */
  private void pressedDeleteButton(final int buttonId) {
    if (dialed_number.length() == MAX_LENGTH || buttonId == R.id.button_delete) {
      dialed_number = dialed_number.substring(0, Math.max(0, dialed_number.length() - 1));
      read_number = read_number.substring(0, Math.max(0, read_number.length() - 2));
    }
  }
  
  /**
   * Called when the activity is first created.
   * */
  @Override public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sosconfig);
    init(0, getString(R.string.sos_config_screen_whereami), getString(R.string.sos_config_screen_whereami));
  }
  
  /**
   * In this overridden function the dialed number is initialized
   * 
   * @param hasFocus
   *          indicates whether a window has the focus
   */
  @Override public void onWindowFocusChanged(final boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      getTalkingButton(R.id.number).setText("");
      getTalkingButton(R.id.number).setReadText("");
    }
  }
}
