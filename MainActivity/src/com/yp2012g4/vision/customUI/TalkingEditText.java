package com.yp2012g4.vision.customUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.yp2012g4.vision.R;

public class TalkingEditText extends EditText {
  private String _readText = ""; // the text for the TTS
  private String _readToolTip = ""; // tool tip text
  private String _prefsValue = ""; // value for preference buttons
  
  /**
   * c'tor
   */
  public TalkingEditText(final Context c, final AttributeSet as) {
    super(c, as);
    final TypedArray a = c.obtainStyledAttributes(as, R.styleable.TalkingEditText, 0, 0);
    _readText = a.getString(R.styleable.TalkingEditText_readText);
    _prefsValue = a.getString(R.styleable.TalkingEditText_prefsValue);
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
  
  // TODO: Check How to connect to foreground and background color settings ???
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
}
