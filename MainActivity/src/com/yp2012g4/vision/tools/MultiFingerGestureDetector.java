package com.yp2012g4.vision.tools;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Multiple fingers gesture detector To detect gesture add functions to
 * onTouchEvent.
 * 
 * @author Roman
 * 
 */
public class MultiFingerGestureDetector extends GestureDetector {
  private static final int TIMEOUT = ViewConfiguration.getDoubleTapTimeout() + 100;
  private long _mFirstDownTime = 0;
  private boolean _mSeparateTouches = false;
  private byte _mTwoFingerTapCount = 0;
  MultiTouchRun _multiTouchRun = null;
  
  /**
   * set multi touch run class
   * 
   * @param multiTouchRun1
   */
  public synchronized void setMultiTouchRun(MultiTouchRun multiTouchRun1) {
    _multiTouchRun = multiTouchRun1;
  }
  
  public MultiFingerGestureDetector(Context context, OnGestureListener listener, Handler handler, boolean unused) {
    super(context, listener, handler, unused);
  }
  
  public MultiFingerGestureDetector(Context context, OnGestureListener listener, Handler handler) {
    super(context, listener, handler);
  }
  
  public MultiFingerGestureDetector(OnGestureListener listener, Handler handler) {
    super(listener, handler);
  }
  
  public MultiFingerGestureDetector(OnGestureListener listener) {
    super(listener);
  }
  
  public MultiFingerGestureDetector(Context context, OnGestureListener listener) {
    super(context, listener);
  }
  
  @Override public boolean onTouchEvent(MotionEvent ev) {
    onTwoFingerDoubleTap(ev);
    return super.onTouchEvent(ev);
  }
  
  private void reset(long time) {
    _mFirstDownTime = time;
    _mSeparateTouches = false;
    _mTwoFingerTapCount = 0;
  }
  
  public boolean onTwoFingerDoubleTap(MotionEvent event) {
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        if (_mFirstDownTime == 0 || event.getEventTime() - _mFirstDownTime > TIMEOUT)
          reset(event.getDownTime());
        break;
      case MotionEvent.ACTION_POINTER_UP:
        if (event.getPointerCount() == 2)
          _mTwoFingerTapCount++;
        else
          _mFirstDownTime = 0;
        break;
      case MotionEvent.ACTION_UP:
        if (!_mSeparateTouches)
          _mSeparateTouches = true;
        else if (_mTwoFingerTapCount == 2 && event.getEventTime() - _mFirstDownTime < TIMEOUT) {
          _multiTouchRun.twoFingerDoubleTapRun();
          _mFirstDownTime = 0;
          return true;
        }
        break;
      default:
        break;
    }
    return false;
  }
}
