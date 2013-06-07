package com.yp2012g4.vision.apps.telephony;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Toast;

import com.yp2012g4.vision.tools.AbstractService;
import com.yp2012g4.vision.tools.CallUtils;
import com.yp2012g4.vision.tools.CallUtils.CALL_TYPE;
import com.yp2012g4.vision.tools.TTS;

public class CallScreenService extends AbstractService {
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
    // Toast.makeText(getBaseContext(), "onDestroy", Toast.LENGTH_LONG).show();
    if (csView != null) {
      ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(csView);
      csView = null;
//    if (v != null) {
//      ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(v);
//      v = null;
    }
    new CallUtils(this).restoreRinger();
  }
  
  @Override public void onStartService() {
    Log.d(TAG, "Service Started.");
  }
  
  @Override public void onStopService() {
    Log.d(TAG, "Service Stopped.");
  }
  
  CallScreenView csView;
  GestureOverlayView gV;
  
  @Override public IBinder onBind(final Intent intent) {
    return super.onBind(intent);
  }
  
  private void processCall(final Context c, final String phoneNumber) {
    Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_LONG).show();
    csView = new CallScreenView(this);
    gV = new GestureOverlayView(c);
    // gV.seton
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT);
//        
//        WindowManager.LayoutParams.WRAP_CONTENT,
//        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY/* ALERT */, 0,
////            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
////                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//        PixelFormat.TRANSLUCENT);
    params.gravity = Gravity.RIGHT | Gravity.TOP;
    params.setTitle("Load Average");
    final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    csView.setNumber(phoneNumber);
    wm.addView(csView, params);
    // wm.addView(gV, params);
  }
}

class CallScreenView extends ViewGroup implements OnGestureListener {
  private final Paint mLoadPaint;
  private String _number = "";
  private static final String TAG = "vision:CallScreenView";
  private final GestureDetector gd = new GestureDetector(this);
  private final CallUtils _cu;
  private final Context _c;
  
  public CallScreenView(final Context c) {
    super(c);
    // Toast.makeText(getContext(), "HUDView", Toast.LENGTH_LONG).show();
    mLoadPaint = new Paint();
    mLoadPaint.setAntiAlias(true);
    mLoadPaint.setTextSize(25);
    mLoadPaint.setARGB(255, 255, 0, 0);
    _cu = new CallUtils(c);
    _c = c;
//    final LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    final View v = vi.inflate(R.layout.activity_incoming_call, null);
  }
  
  @Override protected void onDraw(final Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawColor(Color.BLACK);
    canvas.drawText(_number, 20, 20, mLoadPaint);
    TTS.speak(_number);
  }
  
  public void setNumber(final String number) {
    _number = number;
  }
  
  @Override protected void onLayout(final boolean arg0, final int arg1, final int arg2, final int arg3, final int arg4) {
  }
  
  @Override public boolean onTouchEvent(final MotionEvent event) {
    // Toast.makeText(getContext(), "onTouchEvent", Toast.LENGTH_LONG).show();
    _cu.silenceRinger();
    Log.d(TAG, "onTOuch");
    return gd.onTouchEvent(event);// true;
  }
  
  @Override public boolean onDown(final MotionEvent e) {
    // TODO Auto-generated method stub
    return false;
  }
  
  public static final long VIBRATE_DURATION = 150;
  public static final int SWIPE_THRESHOLD = 100;
  public static final int SWIPE_VELOCITY_THRESHOLD = 100;
  
  @Override public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float f1, final float f2) {
    Log.d(TAG, "OnFling");
    final float diffX = e2.getX() - e1.getX();
    if (Math.abs(diffX) > Math.abs(e2.getY() - e1.getY()))
      if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(f1) > SWIPE_VELOCITY_THRESHOLD)
        if (diffX > 0)
          CallUtils.answerCall(_c);
        else
          _cu.endCall();
    // vibrate(VIBRATE_DURATION);
    return true;
  }
  
  @Override public void onLongPress(final MotionEvent e) {
    // TODO Auto-generated method stub
  }
  
  @Override public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override public void onShowPress(final MotionEvent e) {
    // TODO Auto-generated method stub
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    TTS.speak(_number);
    return false;
  }
}