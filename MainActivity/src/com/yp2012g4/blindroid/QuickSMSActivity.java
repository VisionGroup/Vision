package com.yp2012g4.blindroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

/**
 * This class is an activity that enables to send a quick SMS message
 * 
 * @author Amir
 * @version 1.0
 */
public class QuickSMSActivity extends BlindroidActivity implements OnClickListener {
  TalkingButton b;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quick_sms);
    mHandler = new Handler();
    b = (TalkingButton) findViewById(R.id.SMS_number_1);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.SMS_number_2);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.SMS_number_3);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.SMS_number_4);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.SMS_number_5);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.SMS_number_6);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.SMS_number_7);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.SMS_number_8);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.SMS_number_9);
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
  
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_quick_sms, menu);
    return true;
  }
  
  @SuppressWarnings("boxing")
  @Override public void onClick(View v) {
    final View view = v;
    if (v instanceof TalkingButton) {
      final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
      alertDialog.setTitle("Choose contact");
      alertDialog.setCancelable(false);
      alertDialog.setMessage("will send the SMS to the chosen contact");
      alertDialog.setButton("Send..", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          String messageToSend = ((TalkingButton) view).getText().toString();
          String number = "0524484993";
          SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
          speakOut("Message has been sent");
        }
      });
      alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          speakOut("Cancel");
          alertDialog.dismiss();
        }
      });
      speakOut("Sending" + ((TalkingButton) v).getText().toString());
      while (_t.isSpeaking()){
        //wait...
      }
      alertDialog.show();
      
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
  
  @Override public int getViewId() {
    return R.id.QuickSMSActivity;
  }
}
