package com.yp2012g4.blindroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

/**
 * This class is an activity that quickly dials to a pre-defined limited number
 * of contacts
 * 
 * @author Amir
 * @version 1.0
 */
public class QuickDialActivity extends BlindroidActivity implements OnClickListener {
  protected List<String> list_of_phone_numbers = new ArrayList<String>();
  
  // private static final int NUM_OF_QUICK_DIALS = 9;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    telephone();
    setContentView(R.layout.activity_quick_dial);
    mHandler = new Handler();
    TalkingButton b = (TalkingButton) findViewById(R.id.Contact_number_1);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.Contact_number_2);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.Contact_number_3);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.Contact_number_4);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.Contact_number_5);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.Contact_number_6);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.Contact_number_7);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.Contact_number_8);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.Contact_number_9);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    back = (TalkingImageButton) findViewById(R.id.back_button);
    back.setOnClickListener(this);
    back.setOnTouchListener(this);
    next = (TalkingImageButton) findViewById(R.id.settings_button);
    next.setOnClickListener(this);
    next.setOnTouchListener(this);
    settings = (TalkingImageButton) findViewById(R.id.home_button);
    settings.setOnClickListener(this);
    settings.setOnTouchListener(this);
  }
  
  /**
   * Listening to a change in phone status
   */
  public void telephone() {
    TelephonyManager telephonyManager;
    EndCallListener callListener = new EndCallListener(this.getApplicationContext());
    telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
    telephonyManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
    
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_quick_dial, menu);
    return true;
  }
  
  @Override
  public void onClick(View v) {
    if (v instanceof TalkingButton) {
      speakOut("Dialing to" + ((TalkingButton) v).getText().toString());
      while (_t.isSpeaking() == Boolean.TRUE) {      
        //Wait...
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
    if (v instanceof TalkingImageButton) {
      switch (v.getId()) {
        case R.id.home_button:
          speakOut("Next screen");
          break;
        case R.id.settings_button:
          speakOut("Settings");
          break;
        case R.id.back_button:
          speakOut("Previous screen");
          mHandler.postDelayed(mLaunchTask, 1000);
          break;
      }
    }
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
  
  @Override
  public int getViewId() {
    return R.id.QuickDialActivity;
  }
}
