package com.yp2012g4.vision.apps.contacts;

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

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsReader.DeleteConfirmation;
import com.yp2012g4.vision.apps.smsSender.QuickSMSActivity;
import com.yp2012g4.vision.apps.telephony.EndCallListener;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.CallUtils;
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
   * get the id of the main layout
   */
  @Override public int getViewId() {
    return R.id.DialScreen;
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    super.onSingleTapUp(e);
    if (_navigationBar || curr_view.getId() == R.id.dialer_sms_button)
      return _navigationBar = false;
    if (e.getAction() == MotionEvent.ACTION_UP)
      for (final Map.Entry<View, Rect> entry : getView_to_rect().entrySet())
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
    switch (buttonId) {
      case R.id.dialer_dial_button: // make a phone call
      case R.id.dialer_sms_button: // sms
        // no number was dialed
        if (dialed_number == "") {
          speakOutAsync(getString(R.string.dial_number));
          return;
        }
        if (buttonId == R.id.dialer_sms_button)
          startActivity(new Intent(getApplicationContext(), QuickSMSActivity.class).putExtra(CallUtils.NUMBER_KEY, dialed_number));
        else
          startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + dialed_number)).putExtra(CallUtils.NUMBER_KEY,
              dialed_number));
        dialed_number = "";
        break;
      case R.id.number: // user wished to hear the number, no action needed.
        return;
      case R.id.button_reset: // reset
        final Intent intent = new Intent(getApplicationContext(), DeleteConfirmation.class);
        intent.putExtra("activity", "com.yp2012g4.vision.apps.contacts.DialScreen");
        startActivity(intent);
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
  
  @Override protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);
    final Bundle extras = getIntent().getExtras();
    if (extras != null)
      if (extras.getString("ACTION").equals("DELETE"))
        pressedResetButton();
  }
  
  /**
   * 
   */
  private void pressedResetButton() {
    dialed_number = "";
    read_number = "";
    getTalkingButton(R.id.number).setText("");
    getTalkingButton(R.id.number).setReadText("");
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
    telephone();
    setContentView(R.layout.dial_screen);
    init(0, getString(R.string.dial_screen_whereami), getString(R.string.dial_screen_whereami));
    getTalkingButton(R.id.number).setText("");
    getTalkingButton(R.id.number).setReadText("");
  }
  
  public void telephone() {
    TelephonyManager telephonyManager;
    final EndCallListener callListener = new EndCallListener(getApplicationContext());
    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    telephonyManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
  }
}