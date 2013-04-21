package com.yp2012g4.vision.customUI;

import com.yp2012g4.vision.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * This is a Talking button used in a TTS project to provide the additional
 * functionality and information required by tts.
 * 
 * @version 1.0
 * @author Amit Yaffe
 * 
 */
public class TalkingButton extends Button {
  public TalkingButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TalkingButton, 0, 0);
    ReadText = a.getString(R.styleable.TalkingButton_ReadText);
  }
  
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
  
  private String ReadText="";
  private String ReadToolTip="";
  // TODO: Check How to connect to foreground and background color settings
}
