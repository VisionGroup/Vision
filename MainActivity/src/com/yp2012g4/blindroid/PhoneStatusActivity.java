package com.yp2012g4.blindroid;

import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class PhoneStatusActivity extends Activity implements
		TextToSpeech.OnInitListener {
	private TextToSpeech tts;
	private int _battery = -1;
	private int _signal = -1;
	SignalStrengthListener signalStrengthListener;

	private class SignalStrengthListener extends PhoneStateListener {
		@Override
		public void onSignalStrengthsChanged(
				android.telephony.SignalStrength signalStrength) {

			// get the signal strength (a value between 0 and 31)
			_signal = signalStrength.getGsmSignalStrength();

			// do something with it (in this case we update a text view)
			// signalStrengthTextView.setText(String.valueOf(strengthAmplitude));
			super.onSignalStrengthsChanged(signalStrength);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_status);

		BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
			int scale = -1;

			@Override
			public void onReceive(Context context, Intent intent) {
				_battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1); // TODO:
																			// CHeck
																			// if
																			// scale
																			// is
																			// mandatory.
				Log.e("BatteryManager", "level is " + _battery + "/" + scale);
			}
		};
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryReceiver, filter);

		// start the signal strength listener
		signalStrengthListener = new SignalStrengthListener();
		((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(
				signalStrengthListener,
				SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);

		tts = new TextToSpeech(this, this);
		Button b = (Button) findViewById(R.id.button_getBatteryStatus);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speakOut(((Button) v).getText().toString() + "is " + _battery
						+ "%");

			}
		});
		b.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				speakOut("on Touch " + ((Button) v).getText().toString());
				return false;
			}
		});
		b = (Button) findViewById(R.id.button_getReceptionStatus);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				speakOut(((Button) v).getText().toString() + "is" + _signal);
				// TODO: Add signal status text (out of service...) and check on
				// phone.
			}
		});
		b.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				speakOut("on Touch " + ((Button) v).getText().toString());
				return false;
			}
		});

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_phone_status, menu);
		return true;
	}*/

	private void speakOut(String s) {
		tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onDestroy() {
		if (tts != null) {
			// speakOut("stop");
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int r = tts.setLanguage(Locale.US);
			if (r == TextToSpeech.LANG_NOT_SUPPORTED
					|| r == TextToSpeech.LANG_MISSING_DATA) {
				Log.e("tts", "error setLanguage");
				return;
			}
			speakOut("Phone Status");
			return;
		}
		Log.e("tts", "error init language");

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		speakOut(event.toString());
		return true;
	}

}
