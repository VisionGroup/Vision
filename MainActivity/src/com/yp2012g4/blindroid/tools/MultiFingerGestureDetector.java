package com.yp2012g4.blindroid.tools;

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
  private long mFirstDownTime = 0;
  private boolean mSeparateTouches = false;
  private byte mTwoFingerTapCount = 0;
  MultiTouchRun multiTouchRun = null;
  
  /**
   * set multi touch run class
   * 
   * @param multiTouchRun
   */
  public synchronized void setMultiTouchRun(MultiTouchRun multiTouchRun) {
    this.multiTouchRun = multiTouchRun;
  }
  
  public MultiFingerGestureDetector(Context context, OnGestureListener listener, Handler handler, boolean unused) {
    super(context, listener, handler, unused);
    // TODO Auto-generated constructor stub
  }
  
  public MultiFingerGestureDetector(Context context, OnGestureListener listener, Handler handler) {
    super(context, listener, handler);
    // TODO Auto-generated constructor stub
  }
  
  public MultiFingerGestureDetector(OnGestureListener listener, Handler handler) {
    super(listener, handler);
    // TODO Auto-generated constructor stub
  }
  
  public MultiFingerGestureDetector(OnGestureListener listener) {
    super(listener);
    // TODO Auto-generated constructor stub
  }
  
  public MultiFingerGestureDetector(Context context, OnGestureListener listener) {
    super(context, listener);
    // TODO Auto-generated constructor stub
  }
  
  @Override public boolean onTouchEvent(MotionEvent ev) {
    onTwoFingerDoubleTap(ev);
    return super.onTouchEvent(ev);
  }
  
  private void reset(long time) {
    mFirstDownTime = time;
    mSeparateTouches = false;
    mTwoFingerTapCount = 0;
  }
  
  public boolean onTwoFingerDoubleTap(MotionEvent event) {
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        if (mFirstDownTime == 0 || event.getEventTime() - mFirstDownTime > TIMEOUT)
          reset(event.getDownTime());
        break;
      case MotionEvent.ACTION_POINTER_UP:
        if (event.getPointerCount() == 2)
          mTwoFingerTapCount++;
        else
          mFirstDownTime = 0;
        break;
      case MotionEvent.ACTION_UP:
        if (!mSeparateTouches)
          mSeparateTouches = true;
        else if (mTwoFingerTapCount == 2 && event.getEventTime() - mFirstDownTime < TIMEOUT) {
          multiTouchRun.twoFingerDoubleTapRun();
          mFirstDownTime = 0;
          return true;
        }
    }
    return false;
  }
}
