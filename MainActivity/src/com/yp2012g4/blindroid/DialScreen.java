package com.yp2012g4.blindroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.tools.BlindroidActivity;

public class DialScreen extends BlindroidActivity {
  final static int MAX_LENGTH = 25;
  private String dialed_number = "";
  private String read_number = "";
  
  @Override public int getViewId() {
    return R.id.DialScreen;
  }
  
  @Override public void onActionUp(View v) {
    // Get instance of Vibrator from current Context
    final Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    // Vibrate for 300 milliseconds
    vb.vibrate(100);
    if (v.getId() == R.id.dialer_dial_button) {
      //final Intent returnIntent = new Intent();
      //returnIntent.putExtra("result", dialed_number);
      //setResult(RESULT_OK, returnIntent);
      Intent call = new Intent(Intent.ACTION_CALL);
      call.setData(Uri.parse("tel:" + dialed_number));
      startActivity(call);
      finish();
    }
    if (v.getId() == R.id.dialer_sms_button) {
      Intent i = new Intent(getApplicationContext(), QuickSMSActivity.class);
      i.putExtra("number", dialed_number);
      startActivity(i);
      finish();
    }
    if (v.getId() == R.id.button_reset) {
      dialed_number = "";
      read_number = "";
    }
    if (v.getId() == R.id.number)
      return;
    if (dialed_number.length() == MAX_LENGTH || v.getId() == R.id.button_delete) {
      dialed_number = dialed_number.substring(0, Math.max(0, dialed_number.length() - 1));
      read_number = read_number.substring(0, Math.max(0, read_number.length() - 2));
    }
    if (((View) (v.getParent().getParent())).getId() == R.id.DialScreenNumbers) {
      dialed_number += ((TalkingButton) v).getText();
      read_number = read_number + ((TalkingButton) v).getText() + " ";
    }
    ((TalkingButton) findViewById(R.id.number)).setText(dialed_number.toCharArray(), 0, dialed_number.length());
    ((TalkingButton) findViewById(R.id.number)).setReadText(read_number);
  }
  
  /** Called when the activity is first created. */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dial_screen);
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    return false;
  }
}
