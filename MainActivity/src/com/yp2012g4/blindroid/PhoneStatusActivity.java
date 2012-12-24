package com.yp2012g4.blindroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

public class PhoneStatusActivity extends onTouchEventClass implements
		OnClickListener {

	static final int MAX_SIGNAL = 31;
	// private final PhoneStatus _phoneStatus = new PhoneStatus(this);
	int _battery = -1;
	int _signal = -1;
	int _status;
	SignalStrengthListener signalStrengthListener;

	/**
	 * A signal strength listner. Updates _signal between 0-31.
	 * 
	 * @author Dell
	 * 
	 */
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
		tts = new TextToSpeech(this, this);
		mHandler = new Handler();

		BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				_battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				_status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			}
		};
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryReceiver, filter);

		// start the signal strength listener
		signalStrengthListener = new SignalStrengthListener();
		((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(
				signalStrengthListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

		back = (TalkingImageButton) findViewById(R.id.back_button);
		back.setOnClickListener(this);
		back.setOnTouchListener(this);

		next = (TalkingImageButton) findViewById(R.id.settings_button);
		next.setOnClickListener(this);
		next.setOnTouchListener(this);

		settings = (TalkingImageButton) findViewById(R.id.home_button);
		settings.setOnClickListener(this);
		settings.setOnTouchListener(this);

		TalkingImageButton b = (TalkingImageButton) findViewById(R.id.button_getBatteryStatus);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingImageButton) findViewById(R.id.button_getReceptionStatus);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);
	}

	public static int signalToPercent(int signal) {

		Log.d("Signal", String.valueOf((int) ((signal * 100.0f) / MAX_SIGNAL)));
		return (int) ((signal * 100.0f) / MAX_SIGNAL);
	}

	public String getChargeStatus(int status) {
		switch (status) {
		case BatteryManager.BATTERY_STATUS_CHARGING:
		case BatteryManager.BATTERY_STATUS_FULL:
			return getString(R.string.status_charging);
		default:
			return "";
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TalkingButton) {
			switch (v.getId()) {
			case R.id.button_getBatteryStatus:
				speakOut(((TalkingButton) v).getText().toString() + "is "
						+ _battery + "% " + getChargeStatus(_status));
				break;
			case R.id.button_getReceptionStatus:
				speakOut(((TalkingButton) v).getText().toString() + "is"
						+ signalToPercent(_signal) + "%");
				// TODO: Add signal status text (out of service...) and check on
				// phone.
				break;
			}
		}
		if (v instanceof TalkingImageButton) {
			switch (v.getId()) {
			case R.id.home_button:
				speakOut("Next screen");
				break;
			case R.id.settings_button:
				speakOut("Settings");
				break;
			case R.id.back_button:
				speakOut("Previous screen");
				mHandler.postDelayed(mLaunchTask, 1000);
				break;
			}
		}
	}

  @Override
  public int getViewId() {
    return R.id.phoneStatusActivity;
  }

}
