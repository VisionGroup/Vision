package com.yp2012g4.vision.apps.phoneStatus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsSender.QuickSMSActivity;
import com.yp2012g4.vision.apps.smsSender.SendSMSActivity;
import com.yp2012g4.vision.apps.telephony.CallUtils;
import com.yp2012g4.vision.customUI.lists.CallAdapter;
import com.yp2012g4.vision.customUI.lists.TalkingListView;
import com.yp2012g4.vision.managers.CallType;
import com.yp2012g4.vision.tools.TTS;
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
  
  @Override protected void onCreate(final Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_call_list);
    init(0, getString(R.string.call_list_screen), getString(R.string.call_list_help));
    _tlv = (TalkingListView) findViewById(R.id.TalkingCallListView);
//    if (_tlv.isEmpty())
//      TTS.speak(getString(R.string.noCalls));
  }
  
  @Override public void onResume() {
    super.onRestart();
    _ca = new CallAdapter(this);
    if (_ca.getCount() == 0) {
      TTS.speakSync(getString(R.string.noCalls));
      finish();
    }
    _tlv.setAdapter(_ca);
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    Intent intent;
    final CallType currCall = getCurrentCall();
    Log.d(TAG, currCall.name + " " + currCall.number + " " + currCall.date.toString());
    switch (getButtonByMode().getId()) {
      case R.id.calllist_send_quick_sms:
        intent = newFlaggedIntent(getApplicationContext(), QuickSMSActivity.class).putExtra(CallUtils.NUMBER_KEY, currCall.number);
        startActivity(intent);
        break;
      case R.id.calllist_send_sms:
        intent = newFlaggedIntent(getApplicationContext(), SendSMSActivity.class).putExtra(CallUtils.NUMBER_KEY, currCall.number);
        startActivity(intent);
        break;
      case R.id.calllist_call_sender:
        startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse(CallUtils.CALL_TEL_STRING + currCall.number)));
        break;
      default:
        break;
    }
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
    _ca.removeItemFromList(_tlv.getPage());
    _tlv.setAdapter(_ca);
    _tlv.prevPage();
    speakOutAsync(R.string.delete_call);
    vibrate();
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
    if (Math.abs(diffX) > Math.abs(e2.getY() - e1.getY()) && Math.abs(diffX) > SWIPE_THRESHOLD
        && Math.abs(f1) > SWIPE_VELOCITY_THRESHOLD) {
      if (diffX > 0)
        _tlv.prevPage();
      else
        _tlv.nextPage();
      vibrate();
    }
    return super.onFling(e1, e2, f1, f2);
  }
}
