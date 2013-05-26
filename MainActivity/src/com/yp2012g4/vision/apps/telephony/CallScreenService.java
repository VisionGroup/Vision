package com.yp2012g4.vision.apps.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.yp2012g4.vision.apps.settings.VisionApplication;
import com.yp2012g4.vision.tools.AbstractService;
import com.yp2012g4.vision.tools.CallUtils;
import com.yp2012g4.vision.tools.CallUtils.CALL_TYPE;

public class CallScreenService extends AbstractService {
  private static final String TAG = "vision:CallScreenService";
  
  @Override public void onCreate() {
    super.onCreate();
//    final WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//    final LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    /* View */final View overlay = vi.inflate(R.layout.activity_incoming_call, null);// new
//    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
//        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//        /* | WindowManager.LayoutParams.FLAG_FULLSCREEN */, PixelFormat.TRANSLUCENT);
//    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT);
//    params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND
//        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN;
//    windowManager.addView(overlay, params);
    // processOutgoingCall(this, "");
    Log.d(TAG, "Finished onCreate");
  }
  
  @SuppressWarnings({ "static-method" }) private void processOutgoingCall(final Context c, final String phoneNumber) {
    try {
      Log.d(TAG, "Creating OutgoingCAllActivity intent");
      final Intent i = new Intent(c, IncomingCallActivity.class);
      i.putExtra(CallUtils.RANG_KEY, true);
      i.putExtra(CallUtils.NUMBER_KEY, phoneNumber);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Log.d(TAG, "Starting OutgoingCAllActivity");
      Thread.sleep(VisionApplication.DEFUALT_DELAY_TIME);
      c.startActivity(i);
    } catch (final Exception e) {
      Log.e(TAG, "error starting OutgoingCAllActivity", e);
    }
  }
  
  @SuppressWarnings({ "static-method" }) private void processIncomingCall(final Context c, final String phoneNumber) {
    try {
      Log.d(TAG, "Creating IncomingCAllActivity intent");
      final Intent i = new Intent(c, IncomingCallActivity.class);
      i.putExtra(CallUtils.RANG_KEY, true);
      i.putExtra(CallUtils.NUMBER_KEY, phoneNumber);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Log.d(TAG, "Starting IncomingCAllActivity, number: " + phoneNumber);
      Thread.sleep(VisionApplication.DEFUALT_DELAY_TIME);
      c.startActivity(i);
    } catch (final Exception e) {
      Log.e(TAG, "error starting IncomingCAllActivity", e);
    }
  }
  
  @Override public void onStartService() {
    // TODO Auto-generated method stub
  }
  
  @Override public void onStopService() {
    // TODO Auto-generated method stub
  }
  
  @Override public void onReceiveMessage(final Message msg) {
    Log.d(TAG, "On receive message");
    if (msg == null)
      return;
    try {
      final Bundle b = msg.getData();
      final int ct = b.getInt(CallUtils.CALL_TYPE_KEY);
      final String phoneNumber = b.getString(CallUtils.NUMBER_KEY);
      Log.d(TAG, phoneNumber);
      if (CALL_TYPE.INCOMING_CALL.ordinal() == ct)
        processIncomingCall(this, phoneNumber);
      else
        processOutgoingCall(this, phoneNumber);
    } catch (final Exception e) {
      Log.e(TAG, "Error parsing message.", e);
    }
  }
}
//class HUDView extends ViewGroup {
//  private final Paint mLoadPaint;
//  
//  public HUDView(final Context context) {
//    super(context);
//    Toast.makeText(getContext(), "HUDView", Toast.LENGTH_LONG).show();
//    mLoadPaint = new Paint();
//    mLoadPaint.setAntiAlias(true);
//    mLoadPaint.setTextSize(10);
//    mLoadPaint.setARGB(255, 255, 0, 0);
//  }
//  
//  @Override protected void onDraw(final Canvas canvas) {
//    super.onDraw(canvas);
//    canvas.drawText("Hello World", 5, 15, mLoadPaint);
//  }
//  
//  @Override protected void onLayout(final boolean arg0, final int arg1, final int arg2, final int arg3, final int arg4) {
//  }
//  
//  @Override public boolean onTouchEvent(final MotionEvent event) {
//    // return super.onTouchEvent(event);
//    Toast.makeText(getContext(), "onTouchEvent", Toast.LENGTH_LONG).show();
//    return true;
//  }
//}