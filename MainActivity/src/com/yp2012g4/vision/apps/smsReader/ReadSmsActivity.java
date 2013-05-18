package com.yp2012g4.vision.apps.smsReader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsSender.QuickSMSActivity;
import com.yp2012g4.vision.customUI.lists.SmsAdapter;
import com.yp2012g4.vision.customUI.lists.TalkingListView;
import com.yp2012g4.vision.managers.SmsManager;
import com.yp2012g4.vision.managers.SmsType;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * 
 * @author Amir Blumental
 * @version 2
 */
public class ReadSmsActivity extends VisionActivity {
  TalkingListView listView;
  SmsAdapter adapter;
  private static final int SWIPE_THRESHOLD = 100;
  private static final int SWIPE_VELOCITY_THRESHOLD = 100;
  
  @Override protected void onCreate(final Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_read_sms);
    init(0, getString(R.string.read_sms_screen), getString(R.string.read_sms_help));
    listView = (TalkingListView) findViewById(R.id.TalkingSmsListView2);
    adapter = new SmsAdapter(SmsManager.getIncomingMessages(this), this);
    listView.setAdapter(adapter);
  }
  
  @Override public int getViewId() {
    return R.id.ReadSmsScreen;
  }
  
  @Override public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float f1, final float f2) {
    final float diffX = e2.getX() - e1.getX();
    if (Math.abs(diffX) > Math.abs(e2.getY() - e1.getY()))
      if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(f1) > SWIPE_VELOCITY_THRESHOLD) {
        if (diffX > 0)
          listView.prevPage();
        else
          listView.nextPage();
        vibrate(VIBRATE_DURATION);
      }
    return super.onFling(e1, e2, f1, f2);
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    Intent i = null;
    final SmsType currMsg = getCurrentSms();
    switch (getButtonByMode().getId()) {
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
        adapter.removeItemFromList((int) listView.getDisplayedItemIds()[0]);
        listView.setAdapter(adapter);
        listView.prevPage();
        speakOutAsync(getString(R.string.delete_message));
        vibrate(VIBRATE_DURATION);
        break;
      default:
        break;
    }
    SmsManager.markMessageRead(this, currMsg.getAddress(), currMsg.getBody());
    return false;
  }
  
  public SmsType getCurrentSms() {
    return (SmsType) adapter.getItem((int) listView.getDisplayedItemIds()[0]);
  }
}
