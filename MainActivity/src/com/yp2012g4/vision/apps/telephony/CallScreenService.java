package com.yp2012g4.vision.apps.telephony;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.yp2012g4.vision.tools.AbstractService;
import com.yp2012g4.vision.tools.CallUtils;
import com.yp2012g4.vision.tools.CallUtils.CALL_TYPE;

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
    Toast.makeText(getBaseContext(), "onDestroy", Toast.LENGTH_LONG).show();
    if (mView != null) {
      ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
      mView = null;
    }
  }
  
//  private static void processCall(final Context c, final String phoneNumber) {
//    try {
//      Log.d(TAG, "Creating CAllActivity intent");
//      final Intent i = new Intent(c, IncomingCallActivity.class);
//      i.putExtra(CallUtils.RANG_KEY, true);
//      i.putExtra(CallUtils.NUMBER_KEY, phoneNumber);
//      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      Log.d(TAG, "Starting CAllActivity");
//      Thread.sleep(VisionApplication.DEFUALT_DELAY_TIME);
//      c.startActivity(i);
//    } catch (final Exception e) {
//      Log.e(TAG, "error starting CAllActivity", e);
//    }
//  }
  @Override public void onStartService() {
    Log.d(TAG, "Service Started.");
  }
  
  @Override public void onStopService() {
    Log.d(TAG, "Service Stopped.");
  }
  
  HUDView mView;
  
  @Override public IBinder onBind(final Intent intent) {
    return null;
  }
  
  private void processCall(final Context c, final String phoneNumber) {
    Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_LONG).show();
    mView = new HUDView(this);
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 0,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        PixelFormat.TRANSLUCENT);
    params.gravity = Gravity.RIGHT | Gravity.TOP;
    params.setTitle("Load Average");
    final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    wm.addView(mView, params);
  }
}

class HUDView extends ViewGroup {
  private final Paint mLoadPaint;
  
  public HUDView(final Context context) {
    super(context);
    Toast.makeText(getContext(), "HUDView", Toast.LENGTH_LONG).show();
    mLoadPaint = new Paint();
    mLoadPaint.setAntiAlias(true);
    mLoadPaint.setTextSize(10);
    mLoadPaint.setARGB(255, 255, 0, 0);
  }
  
  @Override protected void onDraw(final Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawText("Hello World", 5, 15, mLoadPaint);
  }
  
  @Override protected void onLayout(final boolean arg0, final int arg1, final int arg2, final int arg3, final int arg4) {
  }
  
  @Override public boolean onTouchEvent(final MotionEvent event) {
    // return super.onTouchEvent(event);
    Toast.makeText(getContext(), "onTouchEvent", Toast.LENGTH_LONG).show();
    return true;
  }
}