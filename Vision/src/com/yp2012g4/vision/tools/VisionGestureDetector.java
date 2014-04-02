package com.yp2012g4.vision.tools;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yp2012g4.vision.VisionApplication;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingEditText;
import com.yp2012g4.vision.customUI.TalkingImageButton;

/**
 * This super class is handling on touch events with text-to-speech feedback for
 * the (blind) user
 * 
 * @author Amir Mizrachi (made some changes in version 2.0)
 * @version 1.1
 * 
 */
public abstract class VisionGestureDetector extends Activity implements OnClickListener, OnGestureListener, OnTouchListener {
  private static final int VIBRATE_SHORT_DURATION = 20;
  private static final String TAG = "vision:VisionGestureDetector";
  public static final long VIBRATE_DURATION = 150;
  public static final int SWIPE_THRESHOLD = 100;
  public static final int SWIPE_VELOCITY_THRESHOLD = 100;
  /**
   * for multitouch gesture detection
   */
  private static final int TIMEOUT = ViewConfiguration.getDoubleTapTimeout() + 100;
  private long _mFirstDownTime = 0;
  private boolean _mSeparateTouches = false;
  private byte _mTwoFingerTapCount = 0;
  private Vibrator vibrator = null;
  
  public enum Dir {
    RIGHT, LEFT, UP, DOWN, NONE
  }
  
  /**
   * Stores the dimensions of a button
   */
  protected Rect _rect;
  /**
   * For supporting Text-To-Speech
   */
  public TTS _tts;
  /**
   * Stores the last view
   */
  protected View last_button_view = null;
  protected View curr_view;
  /**
   * For inserting delays...
   */
  public Handler _mHandler;
  /**
   * Stores the gestures
   */
  protected GestureDetector _gestureDetector;
  /**
   * Mapping from views to their locations on screen
   */
  private final Map<View, Rect> view_to_rect = new HashMap<View, Rect>();
  /**
   * Stores the current locale
   */
  protected Locale _myLocale;
  /**
   * Stores configuration
   */
  protected Configuration _config;
  /**
   * Holds the current spoken string
   */
  public static String _spokenString;
  
  // ==================================================================
  // ===========================METHODS================================
  // ==================================================================
  @Override public boolean onDown(final MotionEvent e) {
    Log.i(TAG, "onDown");
    final SharedPreferences _sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    final String buttonMode = _sp.getString("BUTTON MODE", "regular");
    if (buttonMode.equals("regular"))
      last_button_view = getView(e.getRawX(), e.getRawY()); // updating
    else
      getView(e.getRawX(), e.getRawY());
    return true;
  }
  
  public static Dir flingDir(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
    final float diffX = e2.getX() - e1.getX();
    final float diffY = e2.getY() - e1.getY();
    if (Math.abs(diffX) > Math.abs(diffY))
      if (Math.abs(diffX) > VisionGestureDetector.SWIPE_THRESHOLD
          && Math.abs(velocityX) > VisionGestureDetector.SWIPE_VELOCITY_THRESHOLD) {
        if (diffX > 0)
          return Dir.RIGHT;
        return Dir.LEFT;
      }
    if (Math.abs(diffY) > VisionGestureDetector.SWIPE_THRESHOLD
        && Math.abs(velocityY) > VisionGestureDetector.SWIPE_VELOCITY_THRESHOLD) {
      if (diffY > 0)
        return Dir.DOWN;
      return Dir.UP;
    }
    return Dir.NONE;
  }
  
  @Override public void onLongPress(final MotionEvent e) {
    Log.i(TAG, "onLongPress");
  }
  
  @Override public void onShowPress(final MotionEvent e) {
    Log.i(TAG, "onShowPress");
    if (isButtonType(last_button_view)) {
      VisionApplication.restoreColors(last_button_view, this);
      hapticFeedback(last_button_view);
      speakOutAsync(textToRead(last_button_view));
    }
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    Log.i(TAG, "onSingleTapUp");
    return false;
  }
  
  @Override public boolean onTouch(final View v, final MotionEvent event) {
    final int action = event.getAction() & MotionEvent.ACTION_MASK;
    final SharedPreferences _sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    final String buttonMode = _sp.getString("BUTTON MODE", "regular");
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        actionDown(event, buttonMode);
        break;
      case MotionEvent.ACTION_MOVE:
        actionMove(event);
        break;
      case MotionEvent.ACTION_UP:
        actionUp(buttonMode);
        break;
      default:
        break;
    }
    return _gestureDetector.onTouchEvent(event);
  }
  
  /**
   * Handling the movement event
   * 
   * @param buttonMode
   *          Whether the button is regular or sticky
   */
  private void actionUp(final String buttonMode) {
    Log.i(TAG, "onTouch: ACTION_UP");
    if (buttonMode.equals("regular"))
      VisionApplication.restoreColors(last_button_view, this);
    onActionUp(last_button_view);
  }
  
  /**
   * Handling the movement event
   * 
   * @param event
   *          The movement event
   * @param buttonMode
   *          Whether the button is regular or sticky
   */
  private void actionDown(final MotionEvent event, final String buttonMode) {
    Log.i(TAG, "onTouch: ACTION_DOWN");
    VisionApplication.restoreColors(last_button_view, this);
    if (buttonMode.equals("regular"))
      last_button_view = getView(event.getRawX(), event.getRawY());
  }
  
  /**
   * Handling the movement event
   * 
   * @param event
   *          The movement event
   */
  private void actionMove(final MotionEvent event) {
    for (final Map.Entry<View, Rect> entry : view_to_rect.entrySet())
      if (isButtonType(entry.getKey()))
        if (entry.getValue().contains((int) event.getRawX(), (int) event.getRawY()))
          if (last_button_view != entry.getKey()) {
            VisionApplication.restoreColors(entry.getKey(), this);
            speakOutAsync(textToRead(entry.getKey()));
            entry.getKey().playSoundEffect(SoundEffectConstants.CLICK);
            changeButton(entry.getKey());
          } else
            last_button_view = entry.getKey();
  }
  
  /**
   * Restore button color and making vibration when switching to different
   * button
   * 
   * @param curr
   *          Current view
   */
  private void changeButton(final View curr) {
    VisionApplication.restoreColors(last_button_view, this);
    last_button_view = curr;
    hapticFeedback(last_button_view);
  }
  
  /**
   * Resets params for Two fingers double tap
   * 
   * @param time
   */
  private void reset(final long time) {
    _mFirstDownTime = time;
    _mSeparateTouches = false;
    _mTwoFingerTapCount = 0;
  }
  
  /**
   * A new handler for two fingers double tap
   * 
   * @param event
   * @return
   */
  public boolean onTwoFingerDoubleTap(final MotionEvent event) {
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        Log.i(TAG, "onTwoFingerDoubleTap:ACTION_DOWN");
        if (_mFirstDownTime == 0 || event.getEventTime() - _mFirstDownTime > TIMEOUT)
          reset(event.getDownTime());
        break;
      case MotionEvent.ACTION_POINTER_UP:
        Log.i(TAG, "onTwoFingerDoubleTap:ACTION_POINTER_UP");
        if (event.getPointerCount() == 2)
          _mTwoFingerTapCount++;
        else
          _mFirstDownTime = 0;
        break;
      case MotionEvent.ACTION_UP:
        Log.i(TAG, "onTwoFingerDoubleTap:ACTION_UP");
        if (!_mSeparateTouches) {
          _mSeparateTouches = true;
          break;
        }
        if (_mTwoFingerTapCount == 2 && event.getEventTime() - _mFirstDownTime < TIMEOUT) {
          // open back door to tools.
          final Intent intent = new Intent(Settings.ACTION_SETTINGS);
          intent.addCategory(Intent.CATEGORY_LAUNCHER);
          startActivity(intent);
          _mFirstDownTime = 0;
          return true;
        }
        break;
      default:
        break;
    }
    return false;
  }
  
  /**
   * Getter function
   * 
   * @return
   */
  public Set<Entry<View, Rect>> getView_to_rect() {
    return view_to_rect.entrySet();
  }
  
  /**
   * In this overridden function we gather the buttons positions of the current
   * activity and make them all listen to onTouch and onClick.
   * 
   * @param hasFocus
   *          indicates whether a window has the focus
   */
  @Override public void onWindowFocusChanged(final boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    final ViewGroup _mainView = (ViewGroup) findViewById(getViewId());
    getButtonsPosition(_mainView);
    for (final Map.Entry<View, Rect> entry : view_to_rect.entrySet()) {
      entry.getKey().setOnClickListener(this);
      entry.getKey().setOnTouchListener(this);
    }
    // reads layout description out loud
    if (hasFocus) {
      VisionApplication.applyButtonSettings(view_to_rect.keySet(), _mainView, this);
      if (_mainView.getContentDescription() != null)
        speakOutAsync(findViewById(getViewId()).getContentDescription().toString());
    }
    Log.d(TAG, "caller function : " + Thread.currentThread().getStackTrace()[3].getClassName() + ":"
        + Thread.currentThread().getStackTrace()[3].getMethodName());
  }
  
  /**
   * This is an abstract method which returns the Id of a view
   * 
   * @return Id of the current view
   */
  public abstract int getViewId();
  
  /**
   * Checking whether a given has a button type
   * 
   * @param v
   *          the view to to be checked against a button type
   * @return true if the given view has a button type
   */
  protected static boolean isButtonType(final View v) {
    return v instanceof TalkingButton || v instanceof TalkingImageButton || v instanceof TalkingEditText;
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
  public static String textToRead(final View v) {
    return v instanceof TalkingButton ? ((TalkingButton) v).getReadText()
        : v instanceof TalkingImageButton ? ((TalkingImageButton) v).getReadText() : ((TalkingEditText) v).getReadText();
  }
  
  /**
   * This method define the behavior when the user stops touching the screen
   * 
   * @param v
   *          - last view being touched
   * 
   */
  public void onActionUp(final View v) {
    // to be overridden
  }
  
  /**
   * This method speaks out a given string
   * 
   * @param s
   *          the string to speak out
   */
  public static void speakOutAsync(final String s) {
    _spokenString = s;
    TTS.speak(s);
  }
  
  public static void speakOutSync(final String s) {
    speakOutAsync(s);
    TTS.waitUntilFinishTalking();
  }
  
  public void speakOutAsync(final int id) {
//    _spokenString = getString(id);
    speakOutAsync(getString(id));
  }
  
  public void speakOutSync(final int id) {
    speakOutSync(getString(id));
  }
  
  @Override public void onDestroy() {
    // _tts.shutdown();
    super.onDestroy();
  }
  
  @Override protected void onCreate(final Bundle savedInstanceState) {
    Log.v(TAG, "VisionGestureDetector:onCreate");
    VisionApplication.setThemeToActivity(this);
    super.onCreate(savedInstanceState);
    _mHandler = new Handler();
    _gestureDetector = new GestureDetector(this);
    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      @Override public void uncaughtException(final Thread thread, final Throwable t) {
        Log.e(TAG, "fatal exception: " + ThrowableToString.toString(t));
      }
    });
  }
  
  /**
   * A method for keeping the exact positions of each button (or text view) in a
   * given view
   * 
   * @param v
   *          the view from which to get buttons positions
   */
  public void getButtonsPosition(final View v) {
    _rect = new Rect(getRelativeLeft(v), getRelativeTop(v), getRelativeLeft(v) + v.getWidth(), getRelativeTop(v) + v.getHeight());
    if (v instanceof TimePicker || v instanceof AnalogClock)
      return; // ignoring these view types
    if (isButtonType(v) || v instanceof TextView) {
      view_to_rect.put(v, _rect);
      return;
    }
    final ViewGroup vg = (ViewGroup) v;
    view_to_rect.put(v, _rect);
    for (int i = 0; i < vg.getChildCount(); i++)
      getButtonsPosition(vg.getChildAt(i));
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
  public static int getRelativeLeft(final View myView) {
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
  public static int getRelativeTop(final View myView) {
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
  protected View getView(final float x, final float y) {
    for (final Map.Entry<View, Rect> entry : view_to_rect.entrySet())
      if (entry.getValue().contains((int) x, (int) y)) {
        curr_view = entry.getKey();
        if (isButtonType(entry.getKey()))
          // get view of buttons only
          return entry.getKey();
      }
    return null;
  }
  
  /**
   * Finishes the activity after some delay
   */
  public Runnable mLaunchTask = new Runnable() {
    @Override public void run() {
      finish();
    }
  };
  
  @Override public void startActivity(final Intent intent) {
    vibrate();
    super.startActivity(intent);
  }
  
  /**
   * Make the phone vibrate.
   * 
   * @param milliseconds
   *          The time to vibrate.
   */
  protected void vibrate(final long milliseconds) {
    vibrator.vibrate(milliseconds);
  }
  
  /**
   * Make the phone vibrate for a default duration
   * 
   * @param milliseconds
   *          The time to vibrate.
   */
  protected void vibrate() {
    vibrator.vibrate(VIBRATE_DURATION);
  }
  
  protected void hapticFeedback(final View v) {
    VisionApplication.visualFeedback(v, this);
    vibrate(VIBRATE_SHORT_DURATION);
  }
}
