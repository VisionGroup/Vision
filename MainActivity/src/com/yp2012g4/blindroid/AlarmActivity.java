package com.yp2012g4.blindroid;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

public class AlarmActivity extends onTouchEventClass implements OnClickListener {
	public static PendingIntent pendingIntent;
	public static boolean alarmIsSet = false;
	private Integer lastHour = -1;
	private Integer lastMin = -1;
	public static Calendar alarmTime = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
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

		TalkingButton buttonStart = (TalkingButton) findViewById(R.id.startalarm);
		TalkingButton buttonCancel = (TalkingButton) findViewById(R.id.cancelalarm);
		buttonStart.setOnClickListener(new TalkingButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(AlarmActivity.this,
						AlarmService.class);
				pendingIntent = PendingIntent.getService(AlarmActivity.this, 0,
						myIntent, 0);
				TimePicker tp = (TimePicker) findViewById(R.id.timePicker1);
				Integer hour = tp.getCurrentHour();
				Integer min = tp.getCurrentMinute();
				setAlarm(hour, min);
			}
		});
		buttonStart.setOnTouchListener(this);

		buttonCancel.setOnClickListener(new TalkingButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!alarmIsSet)
					return;
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.cancel(pendingIntent);
				if (AlarmService.mp != null)
					AlarmService.mp.stop();
				alarmIsSet = false;
				// Tell the user about what we did.
				Toast.makeText(AlarmActivity.this, "Alarm is Canceled",
						Toast.LENGTH_LONG).show();
				speakOut("Alarm is Canceled");
			}
		});
		buttonCancel.setOnTouchListener(this);

		TimePicker tp = (TimePicker) findViewById(R.id.timePicker1);
		lastHour = tp.getCurrentHour();
		lastMin = tp.getCurrentMinute();
		tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				String s = "";
				if (lastMin != minute) {
					s = String.valueOf(minute);
					lastMin = minute;
				} else {
					if (hourOfDay - lastHour == 12)
						s = "PM";
					else if (lastHour - hourOfDay == 12)
						s = "AM";
					else {
						int h = hourOfDay > 12 ? hourOfDay - 12 : hourOfDay;
						s = String.valueOf(h);
					}
					lastHour = hourOfDay;
				}
				speakOut(s);
			}
		});
	}

	/**
	 * set the alarm to hour:min
	 * 
	 * @param hour
	 *            - hour in the 24 format
	 * @param min
	 *            - minutes between 0-59
	 */
	private void setAlarm(Integer hour, Integer min) {
		if (min < 0 || min > 59 || hour < 0 || hour > 23)
			return;
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		alarmTime = (Calendar) calendar.clone();
		alarmTime.set(Calendar.SECOND, 0);
		alarmTime.set(Calendar.HOUR_OF_DAY, hour);
		alarmTime.set(Calendar.MINUTE, min);
		if (alarmTime.before(calendar))
			alarmTime.roll(Calendar.DAY_OF_MONTH, true);
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(),
				pendingIntent);
		alarmIsSet = true;
		String s = "Alarm is set to  "
				+ SpeakingClockActivity.parseTime(alarmTime);
		Toast.makeText(AlarmActivity.this, s, Toast.LENGTH_LONG).show();
		speakOut(s);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ViewGroup alarmView = (ViewGroup) findViewById(R.id.AlarmActivity);
		getButtonsPosition(alarmView);
	}

	@Override
	public void onClick(View v) {
		// Intent intent;
		// speakOut(((Button) v).getText().toString());
		switch (v.getId()) {
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