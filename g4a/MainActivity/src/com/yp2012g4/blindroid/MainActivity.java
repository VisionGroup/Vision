package com.yp2012g4.blindroid;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MainActivity extends onTouchEventClass implements OnClickListener {
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

	ImageButton b = (ImageButton) findViewById(R.id.sos_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (ImageButton) findViewById(R.id.time_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (ImageButton) findViewById(R.id.where_am_i_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (ImageButton) findViewById(R.id.phone_status_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (ImageButton) findViewById(R.id.signal_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (ImageButton) findViewById(R.id.alarm_clock_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (ImageButton) findViewById(R.id.quick_dial_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (ImageButton) findViewById(R.id.quick_sms_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (ImageButton) findViewById(R.id.back_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (ImageButton) findViewById(R.id.settings_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (ImageButton) findViewById(R.id.next_button);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);
    }

    @Override
    public void onRestart() {
	super.onRestart();

	DisplaySettingsApplication appState = ((DisplaySettingsApplication) getApplication());
	appState.settings.applyButtonSettings(findViewById(R.id.sos_button));
	appState.settings
		.applyButtonSettings(findViewById(R.id.alarm_clock_button));
	appState.settings.applyButtonSettings(findViewById(R.id.back_button));
	appState.settings.applyButtonSettings(findViewById(R.id.time_button));
	appState.settings.applyButtonSettings(findViewById(R.id.signal_button));
	appState.settings
		.applyButtonSettings(findViewById(R.id.phone_status_button));
	appState.settings.applyButtonSettings(findViewById(R.id.next_button));
	appState.settings
		.applyButtonSettings(findViewById(R.id.settings_button));
	appState.settings
		.applyButtonSettings(findViewById(R.id.where_am_i_button));
	appState.settings
		.applyButtonSettings(findViewById(R.id.quick_dial_button));
	appState.settings
		.applyButtonSettings(findViewById(R.id.quick_sms_button));

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

    @Override
    public void onClick(View v) {
	Intent intent;
	// speakOut(((Button) v).getText().toString());
	switch (v.getId()) {
	case R.id.sos_button:
	    speakOut("SOS");
	    intent = new Intent(MainActivity.this, SOSActivity.class);
	    startActivity(intent);
	    break;
	case R.id.time_button:
	    speakOut("Time");
	    intent = new Intent(MainActivity.this, SpeakingClockActivity.class);
	    startActivity(intent);
	    break;
	case R.id.where_am_i_button:
	    speakOut("Where am I?");
	    break;
	case R.id.phone_status_button:
	    speakOut("Phone status");
	    intent = new Intent(MainActivity.this, PhoneStatusActivity.class);
	    startActivity(intent);
	    break;
	case R.id.signal_button:
	    speakOut("Signal");
	    break;
	case R.id.alarm_clock_button:
	    speakOut("Alarm clock");
	    break;
	case R.id.quick_dial_button:
	    speakOut("Quick dial");
	    intent = new Intent(MainActivity.this, QuickDialActivity.class);
	    startActivity(intent);
	    break;
	case R.id.quick_sms_button:
	    speakOut("SMS");
	    break;
	case R.id.back_button:
	    speakOut("Previous screen");
	    break;
	case R.id.settings_button:
	    speakOut("Settings");
	    intent = new Intent(MainActivity.this, ColorSettingsActivity.class);
	    startActivity(intent);
	    break;
	case R.id.next_button:
	    speakOut("Next screen");
	    break;
	}
    }
}