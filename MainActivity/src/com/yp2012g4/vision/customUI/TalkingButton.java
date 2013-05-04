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
	
    public TalkingButton(Context context, AttributeSet attrs) {
	super(context, attrs);
	final TypedArray a = context.obtainStyledAttributes(attrs,
		R.styleable.TalkingButton, 0, 0);
	ReadText = a.getString(R.styleable.TalkingButton_ReadText);
	PrefsValue = a.getString(R.styleable.TalkingButton_PrefsValue);
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
    private String PrefsValue = "";

    // TODO: Check How to connect to foreground and background color settings
    public String getPrefsValue() {
	return PrefsValue;
    }

    public void setPrefsValue(String prefsValue) {
	PrefsValue = prefsValue;
    }

    /**
     * set runnable class to button.
     * @param run
     */
    public void setRun(Runnable run){
    	_run = run;
    }
    
        
	@Override
	public void run() {
		if (_run != null){
			_run.run();
		}
	}
}
