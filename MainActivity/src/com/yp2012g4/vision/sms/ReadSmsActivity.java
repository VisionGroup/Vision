package com.yp2012g4.vision.sms;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
  
  private void deleteSMS(String msgBody, String msgAddr) {
    try {
      Uri uriSms = Uri.parse("content://sms/inbox");
      Cursor c = getContentResolver().query(uriSms, new String[] { "_id", "thread_id", "address", "person", "date", "body" }, null,
          null, null);
      if (c != null && c.moveToFirst())
        do {
          String address = c.getString(2);
          String body = c.getString(5);
          if (msgBody.equals(body) && address.equals(msgAddr))
            getContentResolver().delete(Uri.parse("content://sms/" + c.getLong(0)), null, null);
        } while (c.moveToNext());
    } catch (Exception e) {
      Log.e("ReadSmsActivity", "Could not delete SMS from inbox: " + e.getMessage());
    }
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    final Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    if (super.onSingleTapUp(e))
      return true;
    View button = getButtonByMode();
    switch (button.getId()) {
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
      case R.id.sms_remove:
        SmsType msg = messages.get(currentMessage);
        deleteSMS(msg.getBody(), msg.getAddress());
        messages.remove(currentMessage);
        if (currentMessage == messages.size())
          currentMessage--;
        setMessage();
        speakOut(getString(R.string.delete_message) + "; " + getString(R.string.message_number) + " " + (currentMessage + 1));
        vb.vibrate(150);
        break;
    }
    return false;
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_read_sms);
//    mHandler = new Handler();
    init(0, getString(R.string.read_sms_screen), getString(R.string.read_sms_help));
    // final SmsManager smsReader = new SmsManager(getApplicationContext());
    messages = SmsManager.getIncomingMessages(this);
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
      SmsType currMsg = messages.get(currentMessage);
      fromButton.setText(currMsg.getPerson());
      bodyButton.setText(currMsg.getBody());
      dateButton.setText(currMsg.getDate());
      fromButton.setReadText(currMsg.getPerson());
      bodyButton.setReadText(currMsg.getBody());
      dateButton.setReadText(currMsg.getDate());
      SmsManager.markMessageRead(this, currMsg.getAddress(), currMsg.getBody());
    } else
      speakOut(getString(R.string.no_messages));
  }
}
