package com.yp2012g4.vision.sms;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.lists.SmsAdapter;
import com.yp2012g4.vision.customUI.lists.TalkingListView;
import com.yp2012g4.vision.managers.SmsManager;
import com.yp2012g4.vision.managers.SmsType;
import com.yp2012g4.vision.tools.VisionActivity;

public class ReadSmsActivity extends VisionActivity {
  TalkingListView listView;
  SmsAdapter adapter;
  Vibrator vb;
  private static final int SWIPE_THRESHOLD = 100;
  private static final int SWIPE_VELOCITY_THRESHOLD = 100;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_read_sms);
    init(0, getString(R.string.read_sms_screen), getString(R.string.read_sms_help));
    vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    listView = (TalkingListView) findViewById(R.id.TalkingSmsListView2);
    ArrayList<SmsType> data = SmsManager.getIncomingMessages(this);
    adapter = new SmsAdapter(data, this);
    listView.setAdapter(adapter);
  }
  
  @Override
  public int getViewId() {
    return R.id.ReadSmsScreen;
  }
  
  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    float diffY = e2.getY() - e1.getY();
    float diffX = e2.getX() - e1.getX();
    if (Math.abs(diffX) > Math.abs(diffY))
      if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
        if (diffX > 0)
          listView.prevPage();
        else
          listView.nextPage();
        vb.vibrate(150);
      }
    return super.onFling(e1, e2, velocityX, velocityY);
  }
  
  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    Intent i = null;
    SmsType currMsg = getCurrentSms();
    View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.sms_send_sms:
        i = new Intent(getApplicationContext(), QuickSMSActivity.class);
        i.putExtra("number", currMsg.getAddress());
        startActivity(i);
        break;
      case R.id.sms_call_sender:
        i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + currMsg.getAddress()));
        startActivity(i);
        break;
      case R.id.sms_remove:
        // first we remove the SMS from the phone DB
        SmsManager.deleteSMS(this, currMsg.getBody(), currMsg.getAddress());
        // then we remove the SMS from the displayed list
        int smsId = (int) listView.getDisplayedItemIds()[0];
        adapter.removeItemFromList(smsId);
        listView.setAdapter(adapter);
        listView.prevPage();
        speakOut(getString(R.string.delete_message));
        vb.vibrate(150);
        break;
      default:
        break;
    }
    SmsManager.markMessageRead(this, currMsg.getAddress(), currMsg.getBody());
    return false;
  }
  
  public SmsType getCurrentSms() {
    long smsId = listView.getDisplayedItemIds()[0];
    SmsType msg = (SmsType) adapter.getItem((int) smsId);
    return msg;
  }
}
