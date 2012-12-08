package com.yp2012g4.blindroid;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener, OnTouchListener {
    /** Called when the activity is first created. */
    private TextToSpeech tts;
	private Rect rect;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this,this);
        Button b = (Button)findViewById(R.id.SOSbutton);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut(((Button)v).getText().toString());
				Intent intent = new Intent(MainActivity.this, SOSActivity.class);
				startActivity(intent);
			}
		});	
        b.setOnTouchListener(this);/*new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				speakOut("on Touch " + ((Button)v).getText().toString());
				return false;
			}
		});*/
        
        b = (Button)findViewById(R.id.quickDial);
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

        b = (Button)findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut(((Button)v).getText().toString());
			}
		});	
        b = (Button)findViewById(R.id.button4);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut(((Button)v).getText().toString());
			}
		});	
        b = (Button)findViewById(R.id.button5);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut(((Button)v).getText().toString());
			}
		});	
        b = (Button)findViewById(R.id.button6);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut(((Button)v).getText().toString());
			}
		});	
        b = (Button)findViewById(R.id.clock);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut(((Button)v).getText().toString());
				Intent intent = new Intent(MainActivity.this, SpeakingClockActivity.class);
				startActivity(intent);
			}
		});	
        b = (Button)findViewById(R.id.button8);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut(((Button)v).getText().toString());
				Intent intent = new Intent(MainActivity.this, PhoneStatusActivity.class);
				startActivity(intent);
			}
		});	
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
	public boolean onTouch(View v, MotionEvent event) {

		// TODO Auto-generated method stub
		//

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// Construct a rect of the view's bounds
			rect = new Rect(v.getLeft(), v.getTop(), v.getRight(),
					v.getBottom());
			speakOut(((Button) v).getText().toString());

		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// Tabk l = (TableLayout) v;

			if (rect.contains((int) event.getX(), (int) event.getY())) {
				// User moved outside bounds
				speakOut(((Button) v).getText().toString());
			} else
				speakOut("out");

		}

		return false;
	}
}