package com.yp2012g4.vision.customUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
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
public class TalkingImageButton extends ImageButton implements Runnable {
  public TalkingImageButton(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TalkingImageButton, 0, 0);
    _readText = a.getString(R.styleable.TalkingImageButton_readText);
    _prefsValue = a.getString(R.styleable.TalkingButton_prefsValue);
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
  public void setReadText(final String s) {
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
  public void setReadToolTip(final String s) {
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
  public void setPrefsValue(final String s) {
    _prefsValue = s;
  }
  
  /**
   * set runnable class to button.
   * 
   * @param r
   */
  public void setRun(final Runnable r) {
    _run = r;
  }
  
  @Override public void run() {
    if (_run != null)
      _run.run();
  }
}
