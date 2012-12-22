package com.yp2012g4.blindroid;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.yp2012g4.blindroid.customUI.TalkingImageButton;

public class SOSActivity extends onTouchEventClass implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sos);
		tts = new TextToSpeech(this, this);
		mHandler = new Handler();

		back = (TalkingImageButton) findViewById(R.id.back_button);
		back.setOnClickListener(this);
		back.setOnTouchListener(this);

		next = (TalkingImageButton) findViewById(R.id.settings_button);
		next.setOnClickListener(this);
		next.setOnTouchListener(this);

		settings = (TalkingImageButton) findViewById(R.id.next_button);
		settings.setOnClickListener(this);
		settings.setOnTouchListener(this);

		// RelativeLayout t = (RelativeLayout)findViewById(R.id.SOS_textview);
		TalkingImageButton tb = (TalkingImageButton) findViewById(R.id.Send_SOS_Message);

		tb.setOnClickListener(this);
		tb.setOnTouchListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sos, menu);
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ViewGroup sosView = (ViewGroup) findViewById(R.id.SOS_textview);
		getButtonsPosition(sosView);
	}

//	// will launch the activity
//	private Runnable mLaunchTask = new Runnable() {
//		public void run() {
//			finish();
//		}
//	};

	@Override
	public void onClick(View v) {
		// Intent intent;
		// speakOut(((Button) v).getText().toString());
		switch (v.getId()) {
		case R.id.Send_SOS_Message:
			String messageToSend = "I need your help!";
			String number = "0524484993";
			SmsManager.getDefault().sendTextMessage(number, null,
					messageToSend, null, null);
			speakOut("SOS message has been sent");
			mHandler.postDelayed(mLaunchTask, 1400);
			break;
		case R.id.back_button:
			speakOut("Previous screen");
			mHandler.postDelayed(mLaunchTask, 1000);
			break;
		case R.id.settings_button:
			speakOut("Settings");
			// intent = new Intent(MainActivity.this,
			// ColorSettingsActivity.class);
			// startActivity(intent);
			break;
		case R.id.next_button:
			speakOut("Next screen");
			break;
		}
	}
}
