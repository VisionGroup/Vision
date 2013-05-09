package com.yp2012g4.vision.customUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.yp2012g4.vision.R;

/**
 * This is a Talking button used in a TTS project to provide the additional
 * functionality and information required by tts.
 * 
 * @version 1.0
 * @author Amit Yaffe
 * 
 */
public class TalkingButton extends Button implements Runnable {
  private Runnable _run;
  private String _readText = "";
  private String _readToolTip = "";
  private String _prefsValue = "";
  
  public TalkingButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TalkingButton, 0, 0);
    _readText = a.getString(R.styleable.TalkingButton_readText);
    _prefsValue = a.getString(R.styleable.TalkingButton_prefsValue);
  }
  
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
}
