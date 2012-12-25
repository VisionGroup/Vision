package com.yp2012g4.blindroid.utils;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;

public abstract  class BlindroidActivity extends onTouchEventClass {

	private int _icon;
	private String _name;
	private String _toolTip;

	public int getIcon(){return _icon;}
	public String getName(){return _name;}
	public String getToolTip(){return _toolTip;}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	public void init(Activity activity,int icon,String name,String toolTip){
		_t = new TTS(activity,(OnInitListener) activity);
		_icon = icon;
		_name = name;
		_toolTip = toolTip;
	}
	
}
