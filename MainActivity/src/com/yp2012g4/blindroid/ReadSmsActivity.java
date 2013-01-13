package com.yp2012g4.blindroid;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.tools.BlindroidActivity;

/**
 * This class is an activity that enables to send a quick SMS message
 * 
 * @author Roman
 * @version 1.1
 */
public class ReadSmsActivity extends BlindroidActivity {
  TalkingButton b;
  private ArrayList<SmsType> messages;
  private int currentMessage = 0;
  
  @Override public int getViewId() {
    return R.id.ReadSmsActivity;
  }
  
  @Override public void onClick(View v) {
    Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    switch (v.getId()) {
      case R.id.sms_next:
        if (currentMessage < messages.size()) {
          currentMessage++;
          setMessage();
          speakOut("message number " + (currentMessage + 1));
        }else{
          speakOut("no more messages");
        }
        vb.vibrate(150);
        break;
      case R.id.sms_prev:
        if (currentMessage > 0) {
          currentMessage--;
          setMessage();
          speakOut("message number " + (currentMessage + 1));
        }else{       
          speakOut("no more messages");
        }
        vb.vibrate(150);
        break;
    }
    if (v instanceof TalkingImageButton)
      switch (v.getId()) {
        case R.id.settings_button:
          speakOut("Settings");
          Intent intent = new Intent(this, DisplaySettingsActivity.class);
          startActivity(intent);
          break;
        case R.id.back_button:
          speakOut("Previous screen");
          mHandler.postDelayed(mLaunchTask, 1000);
          break;
        case R.id.home_button:
          speakOut("Home");
          mHandler.postDelayed(mLaunchTask, 1000);
          break;
        case R.id.current_menu_button:
          speakOut("This is " + getString(R.string.title_activity_read_sms));
          break;
        default:
          break;
      }
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_read_sms);
    mHandler = new Handler();
    SmsReader smsReader = new SmsReader(getApplicationContext());
    messages = smsReader.getIncomingMessages();
    setMessage();
  }
  
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      speakOut("Read sms screen");
    }
  }
  
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_read_sms, menu);
    return true;
  }
  
  private void setMessage() {
    TalkingButton fromButton = (TalkingButton) findViewById(R.id.sms_from);
    TalkingButton bodyButton = (TalkingButton) findViewById(R.id.sms_body);
    TalkingButton dateButton = (TalkingButton) findViewById(R.id.sms_date);
    if (messages.size() != 0) {
      fromButton.setText(messages.get(currentMessage).getPerson());
      bodyButton.setText(messages.get(currentMessage).getBody());
      dateButton.setText(messages.get(currentMessage).getDate());
    } else {
      speakOut("No messages");
    }
  }
  
  @Override public void onBackPressed() {
    //do nothing
  }
}
