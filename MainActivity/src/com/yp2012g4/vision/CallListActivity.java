package com.yp2012g4.vision;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.yp2012g4.vision.apps.smsReader.DeleteConfirmation;
import com.yp2012g4.vision.apps.smsSender.QuickSMSActivity;
import com.yp2012g4.vision.customUI.lists.CallAdapter;
import com.yp2012g4.vision.customUI.lists.TalkingListView;
import com.yp2012g4.vision.managers.CallManager;
import com.yp2012g4.vision.managers.CallType;
import com.yp2012g4.vision.tools.CallUtils;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * 
 * @author Amit Yaffe
 * @version 2
 */
public class CallListActivity extends VisionActivity {
  private TalkingListView _tlv;
  private static String TAG = "vision:CallListActivity";
  private CallAdapter _ca;
  
//  private static final int SWIPE_THRESHOLD = 100;
//  private static final int SWIPE_VELOCITY_THRESHOLD = 100;
  @Override protected void onCreate(final Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_call_list);
    init(0, getString(R.string.call_list_screen), getString(R.string.call_list_help));
    _tlv = (TalkingListView) findViewById(R.id.TalkingCallListView);
    _ca = new CallAdapter(this);
    _tlv.setAdapter(_ca);
    // if (_tlv.isEmpty())
//      TTS.speak(getString(R.string.noCalls));
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final CallType currCall = getCurrentCall();
    Log.d(TAG, currCall.getName().toString() + " " + currCall.getNumber() + " " + currCall.getDate().toString());
    switch (getButtonByMode().getId()) {
      case R.id.calllist_send_sms:
        startActivity(new Intent(getApplicationContext(), QuickSMSActivity.class).putExtra(CallUtils.NUMBER_KEY,
            currCall.getNumber()));
        break;
      case R.id.calllist_call_sender:
        startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + currCall.getNumber())));
        break;
      case R.id.calllist_remove:
        final Intent intent = new Intent(this, DeleteConfirmation.class);
        intent.putExtra("activity", "com.yp2012g4.vision.CallListActivity");
        startActivity(intent);
        // removeFromCallList(currCall);
        break;
      default:
        break;
    }
    // CallManager.markMessageRead(this, currMsg.getAddress(),
    // currMsg.getBody());
    // TODO: Mark Call as read.
    return false;
  }
  
  @Override protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);
    final CallType currCall = getCurrentCall();
    final Bundle extras = getIntent().getExtras();
    if (extras != null)
      if (extras.getString("ACTION").equals("DELETE")) {
        removeFromCallList(currCall);
        Log.d(TAG, "Removing from call list");
      }
  }
  
  /**
   * @param currCall
   */
  private void removeFromCallList(final CallType currCall) {
    // first we remove the Call from the phone DB
    CallManager.UnmarkCallLFromMissedCallList(this, currCall.getNumber());
    // then we remove the Call from the displayed list
    _ca.removeItemFromList(_tlv.getPage());
    _tlv.setAdapter(_ca);
    _tlv.prevPage();
    speakOutAsync(getString(R.string.delete_call));
    vibrate(VIBRATE_DURATION);
  }
  
  public CallType getCurrentCall() {
    final int displayItemId = _tlv.getPage();
    return (CallType) _ca.getItem(displayItemId);
  }
  
  @Override public int getViewId() {
    return R.id.CallListActivity;
  }
  
  @Override public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float f1, final float f2) {
    final float diffX = e2.getX() - e1.getX();
    if (Math.abs(diffX) > Math.abs(e2.getY() - e1.getY()))
      if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(f1) > SWIPE_VELOCITY_THRESHOLD) {
        if (diffX > 0)
          _tlv.prevPage();
        else
          _tlv.nextPage();
        CallManager.UnmarkCallLFromMissedCallList(this, ((CallType) _ca.getItem(_tlv.getPage())).getNumber());
        vibrate(VIBRATE_DURATION);
      }
    return super.onFling(e1, e2, f1, f2);
  }
}
