package com.yp2012g4.vision;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.customUI.lists.CallAdapter;
import com.yp2012g4.vision.customUI.lists.TalkingListView;
import com.yp2012g4.vision.managers.CallManager;
import com.yp2012g4.vision.managers.CallType;
import com.yp2012g4.vision.sms.QuickSMSActivity;
import com.yp2012g4.vision.tools.VisionActivity;

public class CallListActivity extends VisionActivity {
  TalkingListView listView;
  private static String TAG = "CallListActivity";
  CallAdapter adapter;
  Vibrator vb;
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_call_list);
    init(0, getString(R.string.call_list_screen), getString(R.string.call_list_screen));
    listView = (TalkingListView) findViewById(R.id.TalkingCallListView);
    vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    final ArrayList<CallType> data = CallManager.getAllCallsList(this);
    Log.d(TAG, "Received " + data.size() + " Calls from CallManager.");
    adapter = new CallAdapter(data, this);
    listView.setAdapter(adapter);
    if (data.size() == 0)
      _tts.speak(getString(R.string.noCalls));
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    Intent i = null;
    final CallType currCall = getCurrentCall();
    final View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.calllist_send_sms:
        i = new Intent(getApplicationContext(), QuickSMSActivity.class);
        i.putExtra(QuickSMSActivity.NUMBER_KEY, currCall.getNumber());
        startActivity(i);
        break;
      case R.id.calllist_call_sender:
        i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + currCall.getNumber()));
        startActivity(i);
        break;
      case R.id.calllist_remove:
        // first we remove the SMS from the phone DB
        CallManager.DeleteCallLogByNumber(this, currCall.getNumber());
        // then we remove the SMS from the displayed list
        final int callId = (int) listView.getDisplayedItemIds()[0];
        adapter.removeItemFromList(callId);
        listView.setAdapter(adapter);
        listView.prevPage();
        speakOutAsync(getString(R.string.delete_call));
        vb.vibrate(150);
        break;
      default:
        break;
    }
    // SmsManager.markMessageRead(this,
    // currCall.getAddress(),currCall.getBody());
    // TODO: Add mark call as seen.
    return false;
  }
  
  public CallType getCurrentCall() {
    final long callID = listView.getDisplayedItemIds()[0];
    final CallType call = (CallType) adapter.getItem((int) callID);
    return call;
  }
  
  @Override public int getViewId() {
    return R.id.CallListActivity;
  }
  
  @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    listView.nextPage();
    return super.onFling(e1, e2, velocityX, velocityY);
  }
}
