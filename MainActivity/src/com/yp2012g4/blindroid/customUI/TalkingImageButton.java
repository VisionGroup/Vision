package com.yp2012g4.blindroid.customUI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * This is a generic Image button to inherit from. It contains methods vital for
 * TTS.
 * 
 * @author Dell
 * 
 */
public class TalkingImageButton extends ImageButton {

    public TalkingImageButton(Context context, AttributeSet attrs) {
	super(context, attrs);
	// TODO Auto-generated constructor stub
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

    private String ReadText = "";
    private String ReadToolTip = "";

    // TODO: Check How to connect to foreground and background color settings

}
