package com.yp2012g4.vision.sms;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.managers.SmsManager;
import com.yp2012g4.vision.managers.SmsType;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * This class is an activity that enables to send a quick SMS message
 * 
 * @author Roman
 * @version 1.1
 */
public class ReadSmsActivity extends VisionActivity {
  TalkingButton b;
  private ArrayList<SmsType> messages;
  private int currentMessage = 0;
  
  @Override public int getViewId() {
    return R.id.ReadSmsActivity;
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    final Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    super.onSingleTapUp(e);
    switch (curr_view.getId()) {
      case R.id.sms_next:
        if (currentMessage < messages.size() - 1) {
          currentMessage++;
          setMessage();
          speakOut(getString(R.string.message_number) + " " + (currentMessage + 1));
        } else
          speakOut(getString(R.string.no_more_messages));
        vb.vibrate(150);
        break;
      case R.id.sms_prev:
        if (currentMessage > 0) {
          currentMessage--;
          setMessage();
          speakOut(getString(R.string.message_number) + " " + (currentMessage + 1));
        } else
          speakOut(getString(R.string.no_more_messages));
        vb.vibrate(150);
        break;
      default:
        break;
    }
    return false;
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_read_sms);
//    mHandler = new Handler();
    init(0, getString(R.string.read_sms_screen), getString(R.string.read_sms_help));
    final SmsManager smsReader = new SmsManager(getApplicationContext());
    messages = smsReader.getIncomingMessages();
    setMessage();
  }
  
//  @Override public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.activity_read_sms, menu);
//    return true;
//  }
  private void setMessage() {
    final TalkingButton fromButton = (TalkingButton) findViewById(R.id.sms_from);
    final TalkingButton bodyButton = (TalkingButton) findViewById(R.id.sms_body);
    final TalkingButton dateButton = (TalkingButton) findViewById(R.id.sms_date);
    if (messages.size() != 0) {
      fromButton.setText(messages.get(currentMessage).getPerson());
      bodyButton.setText(messages.get(currentMessage).getBody());
      dateButton.setText(messages.get(currentMessage).getDate());
      fromButton.setReadText(messages.get(currentMessage).getPerson());
      bodyButton.setReadText(messages.get(currentMessage).getBody());
      dateButton.setReadText(messages.get(currentMessage).getDate());
    } else
      speakOut(getString(R.string.no_messages));
  }
}
