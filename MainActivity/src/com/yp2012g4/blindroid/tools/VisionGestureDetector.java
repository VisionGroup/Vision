package com.yp2012g4.blindroid.tools;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yp2012g4.blindroid.DisplaySettings;
import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

/**
 * This super class is handling on touch events with text-to-speech feedback for
 * the (blind) user
 * 
 * @author Amir
 * @version 1.0
 */
public abstract class VisionGestureDetector extends Activity implements OnClickListener, TextToSpeech.OnInitListener,
    OnGestureListener, OnTouchListener {
  /**
   * Stores the dimensions of a button
   */
  protected Rect rect;
  /**
   * For supporting Text-To-Speech
   */
  public TTS _t;
  /**
   * Stores the last view
   */
  protected View last_button_view;
  protected View curr_view;
  /**
   * For inserting delays...
   */
  public Handler mHandler;
  /**
   * Back button in each activity
   */
  protected TalkingImageButton back;
  protected TalkingImageButton next;
  protected TalkingImageButton settings;
  protected TalkingImageButton wai;
  protected TalkingImageButton home;
  public GestureDetector gestureDetector;
  /**
   * Mapping from buttons to their locations on screen
   */
  private final Map<View, Rect> view_to_rect = new HashMap<View, Rect>();
  private final Map<TalkingImageButton, Rect> imageButton_to_rect = new HashMap<TalkingImageButton, Rect>();
  
  // ==================================================================
  // ===========================METHODS================================
  // ==================================================================
  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
  }
  
  @Override
  public boolean onDown(MotionEvent e) {
    Log.i("MyLog", "onDown");
    last_button_view = getView(e.getRawX(), e.getRawY());
    if (last_button_view instanceof TalkingButton)
      speakOut(((TalkingButton) last_button_view).getReadText());
    if (last_button_view instanceof TalkingImageButton)
      speakOut(((TalkingImageButton) last_button_view).getReadText());
    return true;
  }
  
  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    Log.i("MyLog", "onFling");
    return false;
  }
  
  @Override
  public void onLongPress(MotionEvent e) {
    Log.i("MyLog", "onLongPress");
  }
  
  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    Log.i("MyLog", "onScroll");
    if (e2.getAction() == MotionEvent.ACTION_MOVE)
      for (Map.Entry<View, Rect> entry : view_to_rect.entrySet())
        if (entry.getKey() instanceof TalkingButton || entry.getKey() instanceof TalkingImageButton)
          if (entry.getValue().contains((int) e2.getRawX(), (int) e2.getRawY()))
            if (last_button_view != entry.getKey()) {
              speakOut(textToRead(entry.getKey()));
              last_button_view = entry.getKey();
            } else
              last_button_view = getView(e2.getRawX(), e2.getRawY());
    return true;
  }
  
  @Override
  public void onShowPress(MotionEvent e) {
    Log.i("MyLog", "onShowPress");
  }
  
  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    Log.i("MyLog", "onSingleTapUp");
    return false;
  }
  
  public Map<TalkingImageButton, Rect> getImageButton_to_rect() {
    return imageButton_to_rect;
  }
  
  /**
   * In this overridden function we gather the buttons positions of the current
   * activity and make them all listen to onTouch and onClick.
   * 
   * @param hasFocus
   *          indicates whether a window has the focus
   */
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    ViewGroup mainView = (ViewGroup) findViewById(getViewId());
    getButtonsPosition(mainView);
    for (Map.Entry<View, Rect> entry : view_to_rect.entrySet()) {
      entry.getKey().setOnClickListener(this);
      entry.getKey().setOnTouchListener(this);
    }
    // reads layout description out loud
    if (hasFocus && findViewById(getViewId()).getContentDescription() != null) {
      DisplaySettings.applyButtonSettings(view_to_rect.keySet());
      speakOut(findViewById(getViewId()).getContentDescription().toString());
    }
  }
  
  /**
   * This is an abstract method which returns the Id of a view
   * 
   * @return Id of the current view
   */
  public abstract int getViewId();
  
  @Override
  public boolean onTouch(View v, MotionEvent event) {
    // remember the last view when finger is up
    if (event.getAction() == MotionEvent.ACTION_UP) {
      Log.i("MyLog", "ACTION UP");
      onActionUp(last_button_view);
    }
    gestureDetector.setIsLongpressEnabled(false);
    return gestureDetector.onTouchEvent(event);
  }
  
  private static boolean isButtonType(View v) {
    return v instanceof TalkingButton || v instanceof TalkingImageButton;
  }
  
  /**
   * return a button text (after upcasting the given view to the appropriate
   * button type)
   * 
   * @param v
   *          the view which is upcasted to one of the buttons types (if it's a
   *          button)
   * @return the button text to be read
   */
  public static String textToRead(View v) {
    return v instanceof TalkingButton ? ((TalkingButton) v).getReadText() : ((TalkingImageButton) v).getReadText();
  }
  
  /**
   * This method defined the behavior when the user stops touching the screen
   * 
   * @param v
   *          - last view being touched
   * 
   */
  public void onActionUp(View v) {/* to be overridden */
  }
  
  /**
   * This method speaks out a given string
   * 
   * @param s
   *          the string to speak out
   */
  public void speakOut(String s) {
    if (_t == null) {
      Log.e("onTouchEventClass", "TTS is null");
      return;
    }
    _t.speak(s);
  }
  
  @Override
  public void onDestroy() {
    _t.shutdown();
    super.onDestroy();
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    _t = new TTS(this, this);
    if (_t.isRuning())
      speakOut("start");
    else
      Log.e("onTouchEventClass", "tts init error");
    DisplaySettings.setThemeToActivity(this);
    super.onCreate(savedInstanceState);
    mHandler = new Handler();
    gestureDetector = new GestureDetector(this);
  }
  
  /**
   * A method for keeping the exact positions of each button in a given view
   * 
   * @param v
   *          the view from which to get buttons positions
   */
  public void getButtonsPosition(View v) {
    rect = new Rect(getRelativeLeft(v), getRelativeTop(v), getRelativeLeft(v) + v.getWidth(), getRelativeTop(v) + v.getHeight());
    if (v instanceof TalkingButton || v instanceof TextView) {
      view_to_rect.put(v, rect);
      return;
    }
    if (v instanceof TalkingImageButton) {
      imageButton_to_rect.put((TalkingImageButton) v, rect);
      view_to_rect.put(v, rect);
      return;
    }
    if (v instanceof TimePicker || v instanceof AnalogClock/*
                                                            * || v instanceof
                                                            * TextView
                                                            */)
      return; // ignoring these view types
    else if (((ViewGroup) v).getChildCount() == 0) {
      view_to_rect.put(v, rect);
      return;
    }
    ViewGroup vg = (ViewGroup) v;
    view_to_rect.put(v, rect);
    for (int i = 0; i < vg.getChildCount(); i++)
      getButtonsPosition(vg.getChildAt(i));
    return;
  }
  
  /**
   * A method to calculate the exact left position of a view on screen (relative
   * to the view's root)
   * 
   * @param myView
   *          the given view for which we want to calculate the exact left
   *          position on screen
   * @return exact left position on screen
   */
  private int getRelativeLeft(View myView) {
    if (myView.getParent() == myView.getRootView())
      return myView.getLeft();
    return myView.getLeft() + getRelativeLeft((View) myView.getParent());
  }
  
  /**
   * A method to calculate the exact top position of a view on screen (relative
   * to the view's root)
   * 
   * @param myView
   *          the given view for which we want to calculate the exact top
   *          position on screen
   * @return exact top position on screen
   */
  private int getRelativeTop(View myView) {
    if (myView.getParent() == myView.getRootView())
      return myView.getTop();
    return myView.getTop() + getRelativeTop((View) myView.getParent());
  }
  
  /**
   * This method returns a specific button view by a given coordinates on screen
   * 
   * @param x
   *          X-coordinate on screen
   * @param y
   *          Y-coordinate on screen
   * @return the view (button) for which the given coordinates belongs to. Null
   *         - if the coordinates are out of any button
   */
  private View getView(float x, float y) {
    for (Map.Entry<View, Rect> entry : view_to_rect.entrySet())
      if (entry.getValue().contains((int) x, (int) y)) {
        curr_view = entry.getKey();
        if (isButtonType(entry.getKey()))
          // get view of buttons only
          return entry.getKey();
      }
    return null;
  }
  
  @Override
  public void onInit(int status) {
    if (!_t.isRuning())
      _t = new TTS(this, this);
  }
  
  /**
   * Finishes the activity after some delay
   */
  public Runnable mLaunchTask = new Runnable() {
    @Override
    public void run() {
      finish();
    }
  };
}
