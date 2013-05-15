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
  private Runnable _run; // the functionality of the button
  private String _readText = ""; // the text for the TTS
  private String _readToolTip = ""; // tool tip text
  private String _prefsValue = ""; // value for preference buttons
  
  /**
   * c'tor
   */
  public TalkingButton(final Context c, final AttributeSet as) {
    super(c, as);
    final TypedArray a = c.obtainStyledAttributes(as, R.styleable.TalkingButton, 0, 0);
    _readText = a.getString(R.styleable.TalkingButton_readText);
    _prefsValue = a.getString(R.styleable.TalkingButton_prefsValue);
  }
  
  /**
   * Returns the text that should be used for TTS when touching the button.
   * 
   * @return String ReadText
   */
  public String getReadText() {
    return _readText;
  }
  
  /**
   * Set the text that should be used for TTS when touching the button.
   * 
   * @param readText
   */
  public void setReadText(final String readText) {
    _readText = readText;
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
