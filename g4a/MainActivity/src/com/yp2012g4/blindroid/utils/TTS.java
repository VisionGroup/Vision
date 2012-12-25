package com.yp2012g4.blindroid.utils;

import java.util.Locale;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class TTS {
	private static TextToSpeech _tts;
	private int _qm;
	private Locale _language;
	
	public TTS(Context context, TextToSpeech.OnInitListener listener){
		Log.e("TTS", "Constractor");
		_tts = new TextToSpeech(context,listener);
		_tts.setLanguage(_language);
		setQueueMode(TextToSpeech.QUEUE_FLUSH);
		setLanguage(Locale.US);
	}

	public void setQueueMode(int qm){
		Log.e("TTS", "setQueueMode");
		_qm=qm;
	}
	public void setLanguage(Locale l){
		Log.e("TTS", "setLanguage");
		_language=l;
	}
	public void speak(String s) {
		Log.e("TTS", "speak : "+s);
		_tts.speak(s, _qm, null);
	}
	public void stop(){
		Log.e("TTS", "stop");
		_tts.stop();
	}
	public void shutdown(){
		Log.e("TTS", "shutdown");
		stop();
		_tts.shutdown();
	}
	
	
}
