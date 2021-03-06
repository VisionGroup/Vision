package com.yp2012g4.vision.apps.smsReader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsSender.QuickSMSActivity;
import com.yp2012g4.vision.apps.smsSender.SendSMSActivity;
import com.yp2012g4.vision.apps.telephony.CallUtils;
import com.yp2012g4.vision.customUI.lists.SmsAdapter;
import com.yp2012g4.vision.customUI.lists.TalkingListView;
import com.yp2012g4.vision.managers.SmsManager;
import com.yp2012g4.vision.managers.SmsType;
import com.yp2012g4.vision.tools.DeleteConfirmation;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * 
 * @author Amir Blumental
 * @version 2
 */
public class ReadSmsActivity extends VisionActivity {
  TalkingListView listView;
  SmsAdapter adapter;
  
  @Override protected void onCreate(final Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_read_sms);
    init(0, getString(R.string.read_sms_screen), getString(R.string.read_sms_help));
    listView = (TalkingListView) findViewById(R.id.TalkingSmsListView2);
    adapter = new SmsAdapter(this);
    if (adapter.getCount() == 0) {
      speakOutSync(R.string.no_messages);
      finish();
    }
    listView.setAdapter(adapter);
  }
  
  @Override public int getViewId() {
    return R.id.ReadSmsScreen;
  }
  
  @Override public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float f1, final float f2) {
    final float diffX = e2.getX() - e1.getX();
    if (Math.abs(diffX) > Math.abs(e2.getY() - e1.getY()) && Math.abs(diffX) > SWIPE_THRESHOLD
        && Math.abs(f1) > SWIPE_VELOCITY_THRESHOLD) {
      final boolean res = diffX > 0 ? listView.prevPage() : listView.nextPage();
      vibrate();
      if (!res)
        speakOutAsync(R.string.no_more_contacts);
      else
        speakOutAsync(getCurrentSms().person);
    }
    return super.onFling(e1, e2, f1, f2);
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    Intent intent;
    final SmsType currMsg = getCurrentSms();
    switch (getButtonByMode().getId()) {
      case R.id.sms_send_sms:
        intent = newFlaggedIntent(getApplicationContext(), SendSMSActivity.class);
        intent.putExtra(CallUtils.NUMBER_KEY, currMsg.address);
        startActivity(intent);
        break;
      case R.id.sms_send_quick_sms:
        intent = newFlaggedIntent(getApplicationContext(), QuickSMSActivity.class);
        intent.putExtra(CallUtils.NUMBER_KEY, currMsg.address);
        startActivity(intent);
        break;
      case R.id.sms_call_sender:
        intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(CallUtils.CALL_TEL_STRING + currMsg.address));
        startActivity(setIntentFlags(intent));
        break;
      case R.id.sms_remove:
        intent = newFlaggedIntent(this, DeleteConfirmation.class);
        intent.putExtra(DeleteConfirmation.ACTIVITY_EXTRA, this.getClass().getName());
        startActivity(intent);
        break;
      default:
        break;
    }
    SmsManager.markMessageRead(this, currMsg.address, currMsg.body);
    return false;
  }
  
  @Override protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);
    final SmsType currMsg = getCurrentSms();
    final Bundle extras = getIntent().getExtras();
    if (extras != null)
      if (extras.getString(ACTION_EXTRA).equals(DeleteConfirmation.DELETE_FLAG)) {
        // first we remove the SMS from the phone DB
        SmsManager.deleteSMS(this, currMsg.body, currMsg.address);
        // then we remove the SMS from the displayed list
        final int pageNumber = listView.getPage();
        adapter.removeItemFromList((int) listView.getDisplayedItemIds()[0]);
        listView.setAdapter(adapter);
        listView.setPage(pageNumber);
        speakOutAsync(R.string.delete_message);
        vibrate();
      }
  }
  
  public SmsType getCurrentSms() {
    return (SmsType) adapter.getItem(listView.getPage());
  }
}
