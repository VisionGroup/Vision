package com.yp2012g4.vision.customUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.yp2012g4.vision.R;

public class TalkingEditText extends EditText {
  private final String _readText; // the text for the TTS
  
  /**
   * c'tor
   */
  public TalkingEditText(final Context c, final AttributeSet as) {
    super(c, as);
    final TypedArray a = c.obtainStyledAttributes(as, R.styleable.TalkingEditText, 0, 0);
    _readText = a.getString(R.styleable.TalkingEditText_readText);
  }
  
  /**
   * Returns the text that should be used for TTS when touching the button.
   * 
   * @return String ReadText
   */
  public String getReadText() {
    return _readText;
  }
}
