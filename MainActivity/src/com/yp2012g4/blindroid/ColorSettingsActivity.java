/**
 * @author Maytal
 *
 */

package com.yp2012g4.blindroid;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

public class ColorSettingsActivity extends BlindroidActivity implements
		OnClickListener {
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_color_settings);
		//tts = new TextToSpeech(this, this);
		mHandler = new Handler();

		TalkingButton b = (TalkingButton)findViewById(R.id.WhiteBlack);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton)findViewById(R.id.WhiteRed);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton)findViewById(R.id.RedBlack);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton)findViewById(R.id.WhiteGreen);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton)findViewById(R.id.GreenBlack);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton)findViewById(R.id.WhiteBlue);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton)findViewById(R.id.BlueBlack);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);
		
		back = (TalkingImageButton) findViewById(R.id.back_button);
		back.setOnClickListener(this);
		back.setOnTouchListener(this);

		next = (TalkingImageButton) findViewById(R.id.settings_button);
		next.setOnClickListener(this);
		next.setOnTouchListener(this);

		settings = (TalkingImageButton) findViewById(R.id.home_button);
		settings.setOnClickListener(this);
		settings.setOnTouchListener(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		//super.onWindowFocusChanged(hasFocus);
		ViewGroup ColorSettingsView = (ViewGroup) findViewById(getViewId());
		getButtonsPosition(ColorSettingsView);
	}

	public void onClick(View v) {
		if (v instanceof TalkingButton)
			speakOut(((TalkingButton) v).getText().toString());
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
		case R.id.back_button:
			speakOut("Previous screen");
			break;
		case R.id.settings_button:
			speakOut("Settings");
			// intent = new Intent(MainActivity.this,
			// ColorSettingsActivity.class);
			// startActivity(intent);
			break;
		case R.id.home_button:
			speakOut("Next screen");
			break;
		}
		 mHandler.postDelayed(mLaunchTask,1000);
		
	}

	private void changeSettings(int int1, int int2) {
		DisplaySettingsApplication appState = ((DisplaySettingsApplication)this.getApplication());
		appState.settings.setColors(int1, int2);

	}
	
	 //will launch the activity
    private Runnable mLaunchTask = new Runnable() {
        public void run() {
        	finish();
        }
     };


    @Override
    public int getViewId() {
      return R.id.ColorSettingsActivity;
    }

}

