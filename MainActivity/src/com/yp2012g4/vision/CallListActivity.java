package com.yp2012g4.vision;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;

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
  private static final int SWIPE_THRESHOLD = 100;
  private static final int SWIPE_VELOCITY_THRESHOLD = 100;
  
  @Override protected void onCreate(final Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_call_list);
    init(0, getString(R.string.call_list_screen), getString(R.string.call_list_help));
    _tlv = (TalkingListView) findViewById(R.id.TalkingCallListView);
    // final ArrayList<CallType> data = CallManager.getAllCallsList(this);
    // Log.d(TAG, "Received " + data.size() + " Calls from CallManager.");
    _ca = new CallAdapter(/* data, */this);
    _tlv.setAdapter(_ca);
//    if (data.isEmpty())
//      TTS.speak(getString(R.string.noCalls));
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final CallType currCall = getCurrentCall();
    switch (getButtonByMode().getId()) {
      case R.id.calllist_send_sms:
        startActivity(new Intent(getApplicationContext(), QuickSMSActivity.class).putExtra(CallUtils.NUMBER_KEY,
            currCall.getNumber()));
        break;
      case R.id.calllist_call_sender:
        startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + currCall.getNumber())));
        break;
      case R.id.calllist_remove:
        removeFromCallList(currCall);
        break;
      default:
        break;
    }
    // TODO: Add mark call as seen.
    return false;
  }
  
  /**
   * @param currCall
   */
  private void removeFromCallList(final CallType currCall) {
    // first we remove the Call from the phone DB
    CallManager.DeleteCallLogByNumber(this, currCall.getNumber());
    // then we remove the SMS from the displayed list
    _ca.removeItemFromList((int) _tlv.getDisplayedItemIds()[0]);
    _tlv.setAdapter(_ca);
    _tlv.prevPage();
    speakOutAsync(getString(R.string.delete_call));
    vibrate(VIBRATE_DURATION);
  }
  
  public CallType getCurrentCall() {
    // final long callID = _tlv.getDisplayedItemIds()[0];
    return (CallType) _ca.getItem((int) _tlv.getDisplayedItemIds()[0]);
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
        vibrate(VIBRATE_DURATION);
      }
    return super.onFling(e1, e2, f1, f2);
  }
}
