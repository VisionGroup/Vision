/***
 * @author Amir Blumental
 * @version 1.0
 */
package com.yp2012g4.blindroid;

import java.text.DateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AnalogClock;
import android.widget.TextView;

import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

public class SpeakingClockActivity extends BlindroidActivity implements OnClickListener {
  /**
   * transform the system current date to string
   * 
   * @return the current system date in string
   */
  public static String getDateFormat() {
    Calendar cal = Calendar.getInstance();
    String date = DateFormat.getDateInstance().format(cal.getTime());
    return date;
  }
  
  /**
   * Parse the Calendar to a string to speak
   * 
   * @param cal
   *          - the Calendar you want to parse
   * @return string to speak
   */
  public static String parseTime(Calendar cal) {
    String ampm = cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
    int h = cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR);
    String s = h + " " + " and " + cal.get(Calendar.MINUTE) + " minutes " + ampm;
    return s;
  }
  
  @Override
  public int getViewId() {
    return R.id.SpeakingClockSctivity;
  }
  
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.back_button:
        speakOut("Previous screen");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.settings_button:
        speakOut("Settings");
        Intent intent = new Intent(this, ThemeSettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.home_button:
        speakOut("Home");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.current_menu_button:
        speakOut("This is " + getString(R.string.ClockTitle));
        break;
      default:
        break;
    }
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_speaking_clock);
    mHandler = new Handler();
    back = (TalkingImageButton) findViewById(R.id.back_button);
    back.setOnClickListener(this);
    back.setOnTouchListener(this);
    settings = (TalkingImageButton) findViewById(R.id.settings_button);
    settings.setOnClickListener(this);
    settings.setOnTouchListener(this);
    wai = (TalkingImageButton) findViewById(R.id.current_menu_button);
    wai.setOnClickListener(this);
    wai.setOnTouchListener(this);
    home = (TalkingImageButton) findViewById(R.id.home_button);
    home.setOnClickListener(this);
    home.setOnTouchListener(this);
    Time today = new Time(Time.getCurrentTimezone());
    today.setToNow();
    TextView tvh = (TextView) findViewById(R.id.textView1);
    String date = getDateFormat();
    tvh.setText(date);
    tvh.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String d = getDateFormat();
        speakOut(d);
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
  
  /**
   * Perform actions when the window get into focus we start the activity by
   * reading out loud the current time
   */
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    if (hasFocus) {
      Calendar cal = Calendar.getInstance();
      speakOut(parseTime(cal));
    }
    super.onWindowFocusChanged(hasFocus);
  }
}
