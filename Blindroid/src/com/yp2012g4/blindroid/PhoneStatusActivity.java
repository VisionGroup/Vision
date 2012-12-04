package com.yp2012g4.blindroid;


import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class PhoneStatusActivity extends Activity implements TextToSpeech.OnInitListener{
	private TextToSpeech tts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_status);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this,this);
        Button b = (Button)findViewById(R.id.button_getBatteryStatus);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut(((Button)v).getText().toString());
			}
		});	
        b.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				speakOut("on Touch " + ((Button)v).getText().toString());
				return false;
			}
		});
        b = (Button)findViewById(R.id.button_getReceptionStatus);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut(((Button)v).getText().toString());
			}
		});	
        b.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				speakOut("on Touch " + ((Button)v).getText().toString());
				return false;
			}
		});
        
       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_phone_status, menu);
		return true;
	}
	private void speakOut(String s) {
		tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onDestroy(){
		if (tts!= null){
			speakOut("stop");
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}
	
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS){
			int r = tts.setLanguage(Locale.US);
			if (r==TextToSpeech.LANG_NOT_SUPPORTED || r==TextToSpeech.LANG_MISSING_DATA){
				Log.e("tts","error setLanguage");
				return;
			}
			speakOut("start");
			return;
		}
		Log.e("tts","error init language");
				
		}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		speakOut(event.toString());
		return true;
	}

}
