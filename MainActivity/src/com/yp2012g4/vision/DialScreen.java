package com.yp2012g4.vision;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.tools.VisionActivity;

public class DialScreen extends VisionActivity {
  final static int MAX_LENGTH = 25;
  private String dialed_number = "";
  private String read_number = "";
  
  @Override public int getViewId() {
    return R.id.DialScreen;
  }
  
  @Override public void onActionUp(View v) {
    // Get instance of Vibrator from current Context
    final Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    
    if (v.getId() == R.id.dialer_dial_button) {
      speakOut(((TalkingImageButton)v).getReadText());
      Intent call = new Intent(Intent.ACTION_CALL);
      call.setData(Uri.parse("tel:" + dialed_number));
      startActivity(call);
    }
    if (v.getId() == R.id.dialer_sms_button) {
      Intent i = new Intent(getApplicationContext(), QuickSMSActivity.class);
      i.putExtra("number", dialed_number);
      startActivity(i);
    }
    if (v.getId() == R.id.number)
      return;
    //reset
    if (v.getId() == R.id.button_reset) {
      dialed_number = "";
      read_number = "";
    }
    //undo
    else if (dialed_number.length() == MAX_LENGTH || v.getId() == R.id.button_delete) {
      dialed_number = dialed_number.substring(0, Math.max(0, dialed_number.length() - 1));
      read_number = read_number.substring(0, Math.max(0, read_number.length() - 2));
    }
    //a number or sign has been chosen
    else if (((View) (v.getParent().getParent())).getId() == R.id.DialScreenNumbers) {
      dialed_number += ((TalkingButton) v).getText();
      read_number = read_number + ((TalkingButton) v).getText() + " ";
    }
    else
      return;
    if (v instanceof TalkingButton)
      speakOut(((TalkingButton)v).getReadText());
    else
      speakOut(((TalkingImageButton)v).getReadText());
    // Vibrate for 150 milliseconds
    vb.vibrate(150);
    ((TalkingButton) findViewById(R.id.number)).setText(dialed_number.toCharArray(), 0, dialed_number.length());
    ((TalkingButton) findViewById(R.id.number)).setReadText(read_number);
  }
  
  /** Called when the activity is first created. */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    telephone();
    setContentView(R.layout.dial_screen);
    init(0, getString(R.string.dial_screen), getString(R.string.dial_screen));
    //TODO: create help string
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    return false;
  }
  
  public void telephone() {
    TelephonyManager telephonyManager;
    EndCallListener callListener = new EndCallListener(getApplicationContext());
    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    telephonyManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
  }
}
