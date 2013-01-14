package com.yp2012g4.blindroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.tools.BlindroidActivity;

/**
 * This class is an activity that quickly dials to a pre-defined limited number
 * of contacts
 * 
 * @author Amir
 * @version 1.0
 */
public class QuickDialActivity extends BlindroidActivity {
  protected List<String> list_of_phone_numbers = new ArrayList<String>();
  
  @Override public int getViewId() {
    return R.id.QuickDialActivity;
  }
  
  @SuppressWarnings("boxing") @Override public void onClick(View v) {
    if (v instanceof TalkingButton) {
      speakOut("Dialing to" + ((TalkingButton) v).getReadText());
      while (_t.isSpeaking() == Boolean.TRUE) {
        // Wait...
      }
      switch (v.getId()) {
        case R.id.Contact_number_1:
          phoneCall("0529240424");
          break;
        case R.id.Contact_number_2:
          phoneCall("0528726908");
          break;
        case R.id.Contact_number_3:
          phoneCall("0544457141");
          break;
        case R.id.Contact_number_4:
          phoneCall("0542197720");
          break;
        case R.id.Contact_number_5:
          phoneCall("0524484993");
          break;
        case R.id.Contact_number_6:
          // phoneCall("0524484993");
          break;
        case R.id.Contact_number_7:
          // phoneCall("0524484993");
          break;
        case R.id.Contact_number_8:
          // phoneCall("0524484993");
          break;
        case R.id.Contact_number_9:
          break;
      }
    }
    if (v instanceof TalkingImageButton)
      super.onClick(v);
  }
  
  // private static final int NUM_OF_QUICK_DIALS = 9;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    telephone();
    setContentView(R.layout.activity_quick_dial);
  }
  
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_quick_dial, menu);
    return true;
  }
  
  /**
   * Activating the Android phone app
   * 
   * @param s
   *          the phone number to dial to (as a string)
   */
  public void phoneCall(String s) {
    Intent call = new Intent(Intent.ACTION_CALL);
    call.setData(Uri.parse("tel:" + s));
    startActivity(call);
  }
  
  /**
   * Listening to a change in phone status
   */
  public void telephone() {
    TelephonyManager telephonyManager;
    EndCallListener callListener = new EndCallListener(getApplicationContext());
    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    telephonyManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
  }
}
