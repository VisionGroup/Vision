package com.yp2012g4.vision;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.sms.QuickSMSActivity;
import com.yp2012g4.vision.telephony.EndCallListener;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * A dialer which offers both SMS and phone calls to a dialed number;
 * 
 * @author Maytal
 * @version 1.1
 */
public class DialScreen extends VisionActivity {
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
   * the number of sequential buttons pressed without the user lifting his
   * finger
   */
  private int buttonPressed = 0;
  
  /**
   * get the id of the main layout
   */
  @Override public int getViewId() {
    return R.id.DialScreen;
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    if (_navigationBar || curr_view.getId() == R.id.dialer_sms_button)
      return _navigationBar = false;
    if (e.getAction() == MotionEvent.ACTION_UP) {
      for (Map.Entry<View, Rect> entry : getView_to_rect().entrySet())
        if (isButtonType(entry.getKey()) && entry.getValue().contains((int) e.getRawX(), (int) e.getRawY())
            && (last_button_view != entry.getKey() || buttonPressed == 0))
          speakOutAsync(textToRead(entry.getKey()));
      buttonPressed = 0;
    }
    return true;
  }
  
  /**
   * Handle the different actions available in this activity
   * 
   * @param v
   *          - the last button pressed before lifting the finger
   */
  @Override public void onActionUp(View v) {
    int buttonId = v.getId();
    switch (buttonId) {
      case R.id.dialer_dial_button: // make a phone call
        // no number was dialed
        if (dialed_number == "") {
          speakOutAsync(getString(R.string.dial_number));
          return;
        }
        startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + dialed_number)));
        break;
      case R.id.dialer_sms_button: // sms
        // no number was dialed
        if (dialed_number == "") {
          speakOutAsync(getString(R.string.dial_number));
          return;
        }
        startActivity(new Intent(getApplicationContext(), QuickSMSActivity.class).putExtra("number", dialed_number));
        break;
      case R.id.number: // user wished to hear the number, no action needed.
        return;
      case R.id.button_reset: // reset
        dialed_number = "";
        read_number = "";
        break;
      case R.id.button_delete: // delete
        if (dialed_number.length() == MAX_LENGTH || buttonId == R.id.button_delete) {
          dialed_number = dialed_number.substring(0, Math.max(0, dialed_number.length() - 1));
          read_number = read_number.substring(0, Math.max(0, read_number.length() - 2));
        }
        // a number or sign has been chosen
        if (((View) v.getParent().getParent()).getId() == R.id.DialScreenNumbers) {
          dialed_number += ((TalkingButton) v).getText();
          read_number = read_number + ((TalkingButton) v).getText() + " ";
        }
        break;
      default:
        break;
    }
    // Vibrate for 150 milliseconds
    vibrate(150);
    getTalkingButton(R.id.number).setText(dialed_number.toCharArray(), 0, dialed_number.length());
    getTalkingButton(R.id.number).setReadText(read_number);
  }
  
  /**
   * update the number of sequential buttons pressed
   */
  @Override public void onShowPress(MotionEvent e) {
    super.onShowPress(e);
    ++buttonPressed;
  }
  
  /**
   * Called when the activity is first created.
   * */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    telephone();
    setContentView(R.layout.dial_screen);
    init(0, getString(R.string.dial_screen), getString(R.string.dial_screen));
    // TODO: create help string
  }
  
  /**
   * In this overridden function the dialed number is initialized
   * 
   * @param hasFocus
   *          indicates whether a window has the focus
   */
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      getTalkingButton(R.id.number).setText("");
      getTalkingButton(R.id.number).setReadText("");
    }
  }
  
  public void telephone() {
    TelephonyManager telephonyManager;
    EndCallListener callListener = new EndCallListener(getApplicationContext());
    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    telephonyManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
  }
}