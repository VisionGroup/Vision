package com.yp2012g4.vision.customUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

import com.yp2012g4.vision.R;

/**
 * This is a Talking ImageButton used in a TTS project to provide the additional
 * functionality and information required by tts.
 * 
 * @version 1.0
 * @author Amit Yaffe
 * 
 */
public class TalkingImageButton extends ImageButton implements Runnable, OnTouchListener, OnGestureListener {
  private final GestureDetector gestureDetector;
  
  public TalkingImageButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TalkingImageButton, 0, 0);
    _readText = a.getString(R.styleable.TalkingImageButton_readText);
    _prefsValue = a.getString(R.styleable.TalkingButton_prefsValue);
    gestureDetector = new GestureDetector(this);
  }
  
  private Runnable _run;
  
  /**
   * Returns the short button text that should be used for TTS.
   * 
   * @return String ReadText
   */
  public String getReadText() {
    return _readText;
  }
  
  /**
   * Set the short button text that should be used for TTS.
   * 
   * @param s
   */
  public void setReadText(String s) {
    _readText = s;
  }
  
  /**
   * Returns the tool tip text that should be used for TTS.
   * 
   * @return String ReadToolTip
   */
  public String getReadToolTip() {
    return _readToolTip;
  }
  
  /**
   * Set the tool tip text that should be used for TTS.
   * 
   * @param s
   */
  public void setReadToolTip(String s) {
    _readToolTip = s;
  }
  
  private String _readText = "";
  private String _readToolTip = "";
  private String _prefsValue = "";
  
  // TODO: Check How to connect to foreground and background color settings
  /**
   * Returns the value linked with the preference represented by the button.
   * 
   * @return String PrefsValue
   */
  public String getPrefsValue() {
    return _prefsValue;
  }
  
  /**
   * Set the value linked with the preference represented by the button.
   * 
   * @param s
   */
  public void setPrefsValue(String s) {
    _prefsValue = s;
  }
  
  /**
   * set runnable class to button.
   * 
   * @param r
   */
  public void setRun(Runnable r) {
    _run = r;
  }
  
  @Override public void run() {
    if (_run != null)
      _run.run();
  }
  
  @Override public boolean onTouch(View v, MotionEvent event) {
    final int action = event.getAction() & MotionEvent.ACTION_MASK;
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        Log.i("MyLog", "onTouchTalkingImageButton: ACTION_DOWN");
        break;
      case MotionEvent.ACTION_MOVE:
        Log.i("MyLog", "onTouchTalkingImageButton: ACTION_MOVE");
        break;
      case MotionEvent.ACTION_UP:
        Log.i("MyLog", "onTouchTalkingImageButton: ACTION_UP");
        break;
      default:
        break;
    }
    return gestureDetector.onTouchEvent(event);
  }
  
  @Override public boolean onDown(MotionEvent arg0) {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override public void onLongPress(MotionEvent e) {
    // TODO Auto-generated method stub
  }
  
  @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override public void onShowPress(MotionEvent e) {
    // TODO Auto-generated method stub
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    // TODO Auto-generated method stub
    return false;
  }
}
