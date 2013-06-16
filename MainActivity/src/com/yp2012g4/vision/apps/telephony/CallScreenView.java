package com.yp2012g4.vision.apps.telephony;

import java.util.Locale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.yp2012g4.vision.VisionApplication;
import com.yp2012g4.vision.managers.ContactManager;
import com.yp2012g4.vision.tools.TTS;
import com.yp2012g4.vision.tools.VisionGestureDetector;
import com.yp2012g4.vision.tools.VisionGestureDetector.Dir;

/**
 * This is the view used during calls.
 * 
 * @author Amit
 * 
 */
public class CallScreenView extends ViewGroup implements OnGestureListener {
  private final Paint mLoadPaint;
  private String _number = "";
  private static final String TAG = "vision:CallScreenView";
  private final GestureDetector gd = new GestureDetector(this);
  private final CallUtils _cu;
  private final Context _c;
  ContactManager _cm;
  String _name;
  
  /********/
  @Override protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
  }
  
  /********/
  public CallScreenView(final Context c) {
    super(c);
    mLoadPaint = new Paint();
    mLoadPaint.setAntiAlias(true);
    mLoadPaint.setTextSize(VisionApplication.getTextSize());
    mLoadPaint.setColor(VisionApplication.getTextColor());
    _cu = new CallUtils(c);
    _c = c;
    _cm = new ContactManager(_c);
  }
  
  @Override protected void onDraw(final Canvas canvas) {
    super.onDraw(canvas);
    final int lX = 40, ly = 100;
    canvas.drawColor(VisionApplication.getBackgroundColor());
    // TODO:: Find screen center
    canvas.drawText(_number, lX, ly - VisionApplication.getTextSize(), mLoadPaint);
    canvas.drawText(_name, lX, ly + VisionApplication.getTextSize(), mLoadPaint);
    if (TTS.getLanguage() == Locale.US && !TTS.isPureEnglish(_name))
      TTS.speak(_number);
    else
      TTS.speak(_name);
  }
  
  public void setNumber(final String number) {
    _number = number;
    _name = _cm.getNameFromPhone(_number);
  }
  
  @Override protected void onLayout(final boolean arg0, final int arg1, final int arg2, final int arg3, final int arg4) {
    // Unused
  }
  
  @Override public boolean onTouchEvent(final MotionEvent event) {
    _cu.silenceRinger();
    Log.d(TAG, "onTOuch");
    return gd.onTouchEvent(event);
  }
  
  @Override public boolean onDown(final MotionEvent e) {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float f1, final float f2) {
    Log.d(TAG, "OnFling");
    final Dir d = VisionGestureDetector.flingDir(e1, e2, f1, f2);
    Log.d(TAG, "Direction = " + d);
    switch (d) {
      case RIGHT:
        CallUtils.answerCall(_c);
        break;
      case LEFT:
        _cu.endCall();
        break;
      case UP:
        CallUtils.toggleSpeakerPhone(_c);
        break;
      case DOWN:
        break;
      default:
        break;
    }
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
    TTS.speak(_name + " " + _number);
    return false;
  }
}