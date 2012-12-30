package com.yp2012g4.blindroid.utils;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
public abstract class onTouchEventClass extends Activity implements OnTouchListener, TextToSpeech.OnInitListener {
  /**
   * Stores the dimensions of a button
   */
  protected Rect rect;
  /**
   * For supporting Text-To-Speech
   */
  public TTS _t;
  /**
   * Stores the previous view
   */
  protected View prev_view;
  /**
   * Stores last view when ACTION_UP
   */
  protected View last_view;
  /**
   * Stores the view we have moved to
   */
  protected View movedTo;
  /**
   * For inserting delays...
   */
  protected Handler mHandler;
  /**
   * Back button in each activity
   */
  protected TalkingImageButton back;
  protected TalkingImageButton next;
  protected TalkingImageButton settings;
  protected TalkingImageButton wai;
  protected TalkingImageButton home;
  /**
   * Mapping from buttons to their locations on screen
   */
  private Map<TalkingImageButton, Rect> imageButton_to_rect = new HashMap<TalkingImageButton, Rect>();
  private Map<TalkingButton, Rect> button_to_rect = new HashMap<TalkingButton, Rect>();
  
  public Map<TalkingButton, Rect> getButton_to_rect() {
    return button_to_rect;
  }
  
  public Map<TalkingImageButton, Rect> getImageButton_to_rect() {
    return imageButton_to_rect;
  }
  
  // protected Map<Button, Intent> button_to_intent = new HashMap<Button,
  // Intent>();
  // protected GestureDetector gestureDetector = new GestureDetector(this);
  /*
   * new GestureDetector . SimpleOnGestureListener () { public boolean
   * onDoubleTap ( MotionEvent e) { Log .i( "MyLog" , "Open new activty here" );
   * startActivity (( button_to_intent .get( last_view ))); return false ; } });
   */
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    ViewGroup mainView = (ViewGroup) findViewById(getViewId());
    getButtonsPosition(mainView);
    DisplaySettings.applyButtonSettings(imageButton_to_rect.keySet(), mainView);
  }
  
  /**
   * This method returns the Id of a view
   * 
   * @return Id of the current view
   */
  public abstract int getViewId();
  
  @Override public boolean onTouch(View v, MotionEvent event) {
    float accurateX = getRelativeLeft(v) + event.getX();
    float accurateY = getRelativeTop(v) + event.getY();
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      prev_view = getView(accurateX, accurateY);
      if (last_view != null && prev_view != last_view)
        // activity and touching
        // in different view
        last_view.setPressed(false);
      if (v instanceof TalkingButton) {
        Log.i("MyLog", "DOWN in Button");
        speakOut(((TalkingButton) v).getText().toString());
      }
      if (v instanceof TalkingImageButton) {
        Log.i("MyLog", "DOWN in ImageButton");
        ((TalkingImageButton) v).setColorFilter(Color.argb(150, 255, 165, 0));
        speakOut(((TalkingImageButton) v).getContentDescription().toString());
      } else
        Log.i("MyLog", "NOT IMAGEBUTTON AMD NOT BUTTON");
    }
    if (event.getAction() == MotionEvent.ACTION_MOVE) {
      movedTo = getView(accurateX, accurateY);
      if (movedTo instanceof TalkingButton) {
        Log.i("MyLog", "ON ACTION MOVE BUTTON");
        for (Map.Entry<TalkingButton, Rect> entry : button_to_rect.entrySet())
          if (entry.getValue().contains((int) accurateX, (int) accurateY))
            if (prev_view != entry.getKey()) {
              if (prev_view instanceof TalkingImageButton)
                ((TalkingImageButton) prev_view).setColorFilter(Color.argb(0, 255, 165, 0));
              if (prev_view instanceof TalkingButton)
                ((TalkingButton) prev_view).setPressed(false);
              speakOut(entry.getKey().getText().toString());
              entry.getKey().getBackground().setAlpha((int) (0.8 * 255));
              entry.getKey().setPressed(true);
              prev_view = entry.getKey();
            }
      }
      if (movedTo instanceof TalkingImageButton) {
        for (Map.Entry<TalkingImageButton, Rect> entry : imageButton_to_rect.entrySet())
          if (entry.getValue().contains((int) accurateX, (int) accurateY))
            if (prev_view != entry.getKey()) {
              if (prev_view instanceof TalkingButton)
                ((TalkingButton) prev_view).setPressed(false);
              if (prev_view instanceof TalkingImageButton)
                ((TalkingImageButton) prev_view).setColorFilter(Color.argb(0, 255, 165, 0));
              speakOut(entry.getKey().getContentDescription().toString());
              entry.getKey().setColorFilter(Color.argb(150, 255, 165, 0));
              prev_view = entry.getKey();
            }
      } else
        prev_view = getView(accurateX, accurateY);
    }
    if (event.getAction() == MotionEvent.ACTION_UP) {
      last_view = getView(accurateX, accurateY);
      if (last_view instanceof TalkingImageButton)
        ((TalkingImageButton) last_view).setColorFilter(Color.argb(0, 255, 165, 0));
      if (last_view instanceof TalkingButton) {
//				((TalkingButton)last_view).getBackground().setAlpha((int)( 1.0 * 255));
//				((TalkingButton)last_view).setPressed(false);
      }
    }
    // gestureDetector.setOnDoubleTapListener(this);
    // return gestureDetector.onTouchEvent(event);
    return false;
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
  
  @Override public void onDestroy() {
    _t.shutdown();
    super.onDestroy();
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    _t = new TTS(this, this);
    if (_t.isRuning())
      speakOut("start");
    else
      Log.e("onTouchEventClass", "tts init error");
    DisplaySettings.setThemeToActivity(this);
    super.onCreate(savedInstanceState);
  }
  
  /**
   * A method for keeping the exact positions of each button in a given view
   * 
   * @param v
   *          the view from which to get buttons positions
   */
  public void getButtonsPosition(View v) {
    rect = new Rect(getRelativeLeft(v), getRelativeTop(v), getRelativeLeft(v) + v.getWidth(), getRelativeTop(v) + v.getHeight());
    if (v instanceof TalkingButton) {
      // Construct a rect of the view's bounds
      button_to_rect.put((TalkingButton) v, rect);
      return;
    }
    if (v instanceof TalkingImageButton) {
      imageButton_to_rect.put((TalkingImageButton) v, rect);
      return;
    } else if (v instanceof TimePicker || v instanceof AnalogClock || v instanceof TextView)
      // ignoring these view types
      return;
    ViewGroup vg = (ViewGroup) v;
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
    return (myView.getLeft() + getRelativeLeft((View) myView.getParent()));
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
    return (myView.getTop() + getRelativeTop((View) myView.getParent()));
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
    for (Map.Entry<TalkingButton, Rect> entry : button_to_rect.entrySet()) {
      Log.i("MyLog", "Button:     Left = " + entry.getValue().left + "  ;  Top = " + entry.getValue().top);
      if (entry.getValue().contains((int) x, (int) y))
        return (entry.getKey());
    }
    for (Map.Entry<TalkingImageButton, Rect> entry : imageButton_to_rect.entrySet()) {
      Log.i("MyLog", "ImageButton:     Left = " + entry.getValue().left + "  ;  Top = " + entry.getValue().top + "  ;  Right = "
          + entry.getValue().right);
      Log.i("MyLog", "x = " + (int) x + "y = " + (int) y);
      if (entry.getValue().contains((int) x, (int) y))
        return (entry.getKey());
    }
    return null;
  }
  
  @Override public void onInit(int status) {
    if (!_t.isRuning())
      _t = new TTS(this, this);
  }
  
  /**
   * Finishes the activity after some delay
   */
  public Runnable mLaunchTask = new Runnable() {
    @Override public void run() {
      finish();
    }
  };
}
