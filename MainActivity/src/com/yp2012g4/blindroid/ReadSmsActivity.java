package com.yp2012g4.blindroid;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MotionEvent;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.managers.SmsManager;
import com.yp2012g4.blindroid.managers.SmsType;
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
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    super.onSingleTapUp(e);
    switch (curr_view.getId()) {
      case R.id.sms_next:
        if (currentMessage < messages.size()-1) {
          currentMessage++;
          setMessage();
          speakOut("message number " + (currentMessage + 1));
        } else {
          speakOut("no more messages");
        }
        vb.vibrate(150);
        break;
      case R.id.sms_prev:
        if (currentMessage > 0) {
          currentMessage--;
          setMessage();
          speakOut("message number " + (currentMessage + 1));
        } else {
          speakOut("no more messages");
        }
        vb.vibrate(150);
        break;
    }
    return false;
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_read_sms);
//    mHandler = new Handler();
    init(0,"Read SMS screen","Touch on screen to read messages, press next or previous message");
    SmsManager smsReader = new SmsManager(getApplicationContext());
    messages = smsReader.getIncomingMessages();
    setMessage();
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
      fromButton.setReadText(messages.get(currentMessage).getPerson());
      bodyButton.setReadText(messages.get(currentMessage).getBody());
      dateButton.setReadText(messages.get(currentMessage).getDate());
    } else {
      speakOut("No messages");
    }
  }
}
