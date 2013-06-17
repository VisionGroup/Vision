package com.yp2012g4.vision.apps.telephony;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.yp2012g4.vision.apps.telephony.CallUtils.CALL_TYPE;
import com.yp2012g4.vision.tools.AbstractService;

public class CallScreenService extends AbstractService {
  ArrayList<CallScreenView> csViews = new ArrayList<CallScreenView>();
  // CallScreenView csView;
  GestureOverlayView gV;
  private static final String TAG = "vision:CallScreenService";
  
  /**
   * Receives a message containing bundle with a phone number (using the
   * CallUtils.NUMBER_KEY) and a call type (using CallUtils.CALL_TYPE_KEY)
   * Starts/ends the call activity.
   */
  @Override public void onReceiveMessage(final Message m) {
    Log.d(TAG, "On receive message");
    if (m == null)
      return;
    try {
      final Bundle b = m.getData();
      final String phoneNumber = b.getString(CallUtils.NUMBER_KEY);
      final CALL_TYPE ct = CALL_TYPE.values()[b.getInt(CallUtils.CALL_TYPE_KEY)];
      switch (ct) {
        case INCOMING_CALL:
        case OUTGOING_CALL:
          Log.d(TAG, phoneNumber);
          processCall(this, phoneNumber);
          break;
        case CALL_ENDED:
          endCall();
          break;
        default:
          break;
      }
    } catch (final Exception e) {
      Log.e(TAG, "Error parsing message.", e);
    }
  }
  
  private void endCall() {
    if (csViews != null && csViews.size() > 0)
      for (final CallScreenView csView : csViews)
        if (csView != null)
          ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(csView);
    csViews.clear();
    new CallUtils(this).restoreRinger();
  }
  
  @Override public void onStartService() {
    Log.d(TAG, "Service Started.");
  }
  
  @Override public void onStopService() {
    Log.d(TAG, "Service Stopped.");
  }
  
  @Override public IBinder onBind(final Intent intent) {
    return super.onBind(intent);
  }
  
  private void processCall(final Context c, final String phoneNumber) {
    final CallScreenView csv = new CallScreenView(this);
    gV = new GestureOverlayView(c);
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN, PixelFormat.TRANSLUCENT);
    params.gravity = Gravity.RIGHT | Gravity.TOP;
    params.setTitle("Load Average");
    csv.setNumber(phoneNumber);
    csViews.add(csv);
    ((WindowManager) getSystemService(WINDOW_SERVICE)).addView(csv, params);
  }
}
