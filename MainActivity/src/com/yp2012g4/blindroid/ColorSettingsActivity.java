/**
 * @author Maytal
 *
 */

package com.yp2012g4.blindroid;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ColorSettingsActivity extends onTouchEventClass implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_color_settings);
		tts = new TextToSpeech(this, this);

		View b = findViewById(R.id.WhiteBlack);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = findViewById(R.id.WhiteRed);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = findViewById(R.id.RedBlack);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = findViewById(R.id.WhiteGreen);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = findViewById(R.id.GreenBlack);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = findViewById(R.id.WhiteBlue);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = findViewById(R.id.BlueBlack);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_color_settings, menu);
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ViewGroup ColorSettingsView = (ViewGroup) findViewById(R.id.ColorSettingsActivity);
		getButtonsPosition(ColorSettingsView);
	}

	public void onClick(View v) {
		if (v instanceof Button)
			speakOut(((Button) v).getText().toString());
		switch (v.getId()) {
		case R.id.WhiteBlack:
			changeSettings(R.color.WHITE, R.color.BLACK);
			break;
		case R.id.WhiteRed:
			changeSettings(R.color.WHITE, R.color.RED);
			break;
		case R.id.RedBlack:
			changeSettings(R.color.RED, R.color.BLACK);
			break;
		case R.id.WhiteGreen:
			changeSettings(R.color.WHITE, R.color.GREEN);
			break;
		case R.id.GreenBlack:
			changeSettings(R.color.GREEN, R.color.BLACK);
			break;
		case R.id.WhiteBlue:
			changeSettings(R.color.WHITE, R.color.BLUE);
			break;
		case R.id.BlueBlack:
			changeSettings(R.color.BLUE, R.color.BLACK);
			break;
		}
		finish();
	}

	private void changeSettings(int int1, int int2) {
		DisplaySettingsApplication appState = ((DisplaySettingsApplication)this.getApplication());
		appState.settings.setColors(int1, int2);

	}

}

