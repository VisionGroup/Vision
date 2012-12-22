package com.yp2012g4.blindroid;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.TextView;

import com.yp2012g4.blindroid.customUI.TalkingImageButton;

public class SpeakingClockActivity extends onTouchEventClass implements OnClickListener {
  /**
   * @param cal
   *          - the Calendar you want to parse
   * @return string to speak
   */
  public static String parseTime(Calendar cal) {
    String ampm = cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
    Integer h = cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR);
    String s = h + " " + " and " + cal.get(Calendar.MINUTE) + " minutes " + ampm;
    return s;
  }
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_speaking_clock);
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
  
    Time today = new Time(Time.getCurrentTimezone());
    today.setToNow();
    TextView tvh = (TextView) findViewById(R.id.textView1);
    Calendar cal = Calendar.getInstance();
    String date = DateFormat.getDateInstance().format(cal.getTime());
    tvh.setText(date);
    tvh.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar cal = Calendar.getInstance();
        String date = DateFormat.getDateInstance().format(cal.getTime());
        speakOut(date);
      }
    });
    AnalogClock ac = (AnalogClock) findViewById(R.id.analogClock1);
    ac.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar cal = Calendar.getInstance();
        speakOut(parseTime(cal));
      }
    });
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }
  
//  @Override
//  public void onDestroy() {
//    if (tts != null) {
//      tts.stop();
//      tts.shutdown();
//    }
//    super.onDestroy();
//  }
//  
  
  @Override
  public void onInit(int status) {
    if (status == TextToSpeech.SUCCESS) {
      int r = tts.setLanguage(Locale.US);
      if (r == TextToSpeech.LANG_NOT_SUPPORTED || r == TextToSpeech.LANG_MISSING_DATA) {
        Log.e("tts", "error setLanguage");
        return;
      }
      return;
    }
    Log.e("tts", "error init language");
  }
  
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    if (hasFocus) {
      Calendar cal = Calendar.getInstance();
      speakOut(parseTime(cal));
    }
    super.onWindowFocusChanged(hasFocus);
    ViewGroup speakingClockView = (ViewGroup) findViewById(R.id.SpeakingClockSctivity);
	getButtonsPosition(speakingClockView);
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
