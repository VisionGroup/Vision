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
  public TalkingImageButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TalkingImageButton, 0, 0);
    ReadText = a.getString(R.styleable.TalkingImageButton_ReadText);
    PrefsValue = a.getString(R.styleable.TalkingButton_PrefsValue);
  }
  
  private Runnable _run;
  
  /**
   * Returns the short button text that should be used for TTS.
   * 
   * @return String ReadText
   */
  public String getReadText() {
    return ReadText;
  }
  
  /**
   * Set the short button text that should be used for TTS.
   * 
   * @param readText
   */
  public void setReadText(String readText) {
    ReadText = readText;
  }
  
  /**
   * Returns the tool tip text that should be used for TTS.
   * 
   * @return String ReadToolTip
   */
  public String getReadToolTip() {
    return ReadToolTip;
  }
  
  /**
   * Set the tool tip text that should be used for TTS.
   * 
   * @param readToolTip
   */
  public void setReadToolTip(String readToolTip) {
    ReadToolTip = readToolTip;
  }
  
  private String ReadText = "";
  private String ReadToolTip = "";
  private String PrefsValue = "";
  
  // TODO: Check How to connect to foreground and background color settings
  /**
   * Returns the value linked with the preference represented by the button.
   * 
   * @return String PrefsValue
   */
  public String getPrefsValue() {
    return PrefsValue;
  }
  
  /**
   * Set the value linked with the preference represented by the button.
   * 
   * @param prefsValue
   */
  public void setPrefsValue(String prefsValue) {
    PrefsValue = prefsValue;
  }
  
  /**
   * set runnable class to button.
   * 
   * @param run
   */
  public void setRun(Runnable run) {
    _run = run;
  }
  
  @Override public void run() {
    if (_run != null) {
      _run.run();
    }
  }
}
