package com.yp2012g4.blindroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

/**
 * This class is an activity which sends a pre-defined SOS message to a
 * pre-defined contact
 * 
 * @author Amir
 * @version 1.0
 */
public class SOSActivity extends BlindroidActivity implements OnClickListener {
  @Override
  public int getViewId() {
    return R.id.SOS_textview;
  }
  
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.Send_SOS_Message:
        String messageToSend = "I need your help!";
        String number = "0529240424";
        SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
        speakOut("SOS message has been sent");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.back_button:
        speakOut("Previous screen");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.settings_button:
        speakOut("Settings");
        Intent intent = new Intent(this, ColorSettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.home_button:
        speakOut("Home");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.current_menu_button:
        speakOut("This is " + getString(R.string.title_activity_sos));
        break;
      default:
        break;
    }
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sos);
    mHandler = new Handler();
    back = (TalkingImageButton) findViewById(R.id.back_button);
    back.setOnClickListener(this);
    back.setOnTouchListener(this);
    settings = (TalkingImageButton) findViewById(R.id.settings_button);
    settings.setOnClickListener(this);
    settings.setOnTouchListener(this);
    wai = (TalkingImageButton) findViewById(R.id.current_menu_button);
    wai.setOnClickListener(this);
    wai.setOnTouchListener(this);
    home = (TalkingImageButton) findViewById(R.id.home_button);
    home.setOnClickListener(this);
    home.setOnTouchListener(this);
    TalkingImageButton tb = (TalkingImageButton) findViewById(R.id.Send_SOS_Message);
    tb.setOnClickListener(this);
    tb.setOnTouchListener(this);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_sos, menu);
    return true;
  }
}
