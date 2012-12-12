package com.yp2012g4.blindroid;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.TextView;

public class SpeakingClockActivity extends Activity implements TextToSpeech.OnInitListener {
	private TextToSpeech tts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speaking_clock);
		tts = new TextToSpeech(this,this);
		 
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		TextView tvh= (TextView) findViewById(R.id.textView1);
		Calendar cal = Calendar.getInstance();
		String date = DateFormat.getDateInstance().format(cal.getTime());
		tvh.setText(date);
		tvh.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				String date = DateFormat.getDateInstance().format(cal.getTime());
				speakOut(date);
			}
		});
					
		AnalogClock ac = (AnalogClock)findViewById(R.id.analogClock1);
			ac.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Calendar cal = Calendar.getInstance();
					speakOut(DateFormat.getTimeInstance().format(cal.getTime()));
				}
			});	
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void onDestroy(){
		if (tts!= null){
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
			return;
		}
		Log.e("tts","error init language");
				
		}
	
	private void speakOut(String s) {
		tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
	}

}
