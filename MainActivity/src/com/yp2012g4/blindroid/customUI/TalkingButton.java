package com.yp2012g4.blindroid.customUI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class TalkingButton extends ImageButton {

    public TalkingButton(Context context, AttributeSet attrs) {
	super(context, attrs);
	// TODO Auto-generated constructor stub
    }

    public String getReadText() {
	return ReadText;
    }

    public void setReadText(String readText) {
	ReadText = readText;
    }

    public String getReadToolTip() {
	return ReadToolTip;
    }

    public void setReadToolTip(String readToolTip) {
	ReadToolTip = readToolTip;
    }

    private String ReadText;
    private String ReadToolTip;

    // TODO: Check How to connect to foreground and background color settings
    // TODO: Set onTouch listener.

}
