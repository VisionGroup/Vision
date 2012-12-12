package com.yp2012g4.blindroid;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends onTouchEventClass {
    /** Called when the activity is first created. */
    // private TextToSpeech tts;
    // private Rect rect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	// TODO: Add to every activity!! or to the new Activity intrerface Yaron
	// is making
	setVolumeControlStream(AudioManager.STREAM_MUSIC);

	tts = new TextToSpeech(this, this);

	// LinearLayout mainView = (LinearLayout)
	// findViewById(R.id.MainActivityView);
	// getButtonsPosition(mainView);
	Button b = (Button) findViewById(R.id.SOSbutton);
	b.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		speakOut(((Button) v).getText().toString());
		Intent intent = new Intent(MainActivity.this, SOSActivity.class);
		startActivity(intent);
	    }
	});
	b.setOnTouchListener(this);

	b = (Button) findViewById(R.id.quickDial);
	b.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		speakOut(((Button) v).getText().toString());
		Intent intent = new Intent(MainActivity.this,
			QuickDialActivity.class);
		startActivity(intent);
	    }
	});
	b.setOnTouchListener(this);

	b = (Button) findViewById(R.id.button3);
	b.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		speakOut(((Button) v).getText().toString());
	    }
	});
	b.setOnTouchListener(this);

	b = (Button) findViewById(R.id.button4);
	b.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		speakOut(((Button) v).getText().toString());
	    }
	});
	b.setOnTouchListener(this);

	b = (Button) findViewById(R.id.button5);
	b.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		speakOut(((Button) v).getText().toString());
	    }
	});
	b.setOnTouchListener(this);

	b = (Button) findViewById(R.id.button6);
	b.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		speakOut(((Button) v).getText().toString());
	    }
	});
	b.setOnTouchListener(this);

	b = (Button) findViewById(R.id.clock);
	b.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		speakOut(((Button) v).getText().toString());
	    }
	});
	b.setOnTouchListener(this);

	b = (Button) findViewById(R.id.button8);
	b.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		speakOut(((Button) v).getText().toString());
		Intent intent = new Intent(MainActivity.this,
			PhoneStatusActivity.class);
		startActivity(intent);
	    }
	});
	b.setOnTouchListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
	super.onWindowFocusChanged(hasFocus);
	ViewGroup mainView = (ViewGroup) findViewById(R.id.MainActivityView);
	// Resources res = getResources();
	// XmlResourceParser parser = res.getXml(R.layout.activity_main);
	// getButtonsPosition(mainView, parser);
	// InputStream inputStream =
	// getResources().openRawResource(R.layout.activity_main);
	// File file = new File(inputStream);
	getButtonsPosition(mainView);
    }

}