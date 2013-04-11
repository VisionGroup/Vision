/***
 * This Activity display the time and date to the user and allow him to get read
 * this values aloud
 * 
 * @author Amir Blumental
 * @version 1.1
 */
package com.yp2012g4.vision.clock;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;

public class SpeakingClockActivity extends VisionActivity {
  final Handler handler = new Handler();
  
  /**
   * transform the system current date to string
   * 
   * @return the current system date in string
   */
  public static String getDateFormat() {
    Calendar cal = Calendar.getInstance();
    String date = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(cal.getTime());
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
    String ampm = cal.get(Calendar.AM_PM) == Calendar.AM ? "AM":"PM";//Resources.getSystem().getString(R.string.am) : Resources.getSystem().getString(R.string.pm);
    int h = cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR);
    String s = h + " " + " and "/*Resources.getSystem().getString(R.string.and)*/ + " " + cal.get(Calendar.MINUTE) + " " + " minutes "/*Resources.getSystem().getString(R.string.minutes)*/+ ampm;
    return s;
  }
  
  @Override public int getViewId() {
    return R.id.SpeakingClockSctivity;
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    switch (curr_view.getId()) {
      case R.id.TimeButton:
        Calendar cal = Calendar.getInstance();
        speakOut(parseTime(cal));
        break;
      case R.id.DateButton:
        String d = getDateFormat();
        speakOut(d);
        break;
      default:
        break;
    }
    return false;
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_speaking_clock);
    init(0, getString(R.string.ClockTitle), getString(R.string.speaking_clock_help));
    TalkingButton dateButton = (TalkingButton) findViewById(R.id.DateButton);
    dateButton.setText(getDateFormat());
    MyTimerTask myTask = new MyTimerTask();
    Timer myTimer = new Timer();
    myTimer.schedule(myTask, 100, 1000);
  }
  
  /***
   * Handle the clock display
   * 
   * @author Amir B
   */
  class MyTimerTask extends TimerTask {
    @Override public void run() {
      handler.post(new Runnable() {
        @Override public void run() {
          TalkingButton timeButton = (TalkingButton) findViewById(R.id.TimeButton);
          Calendar cal = Calendar.getInstance();
          String ampm = cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
          int h = cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR);
          String s = h + " : " + cal.get(Calendar.MINUTE) + " " + ampm;
          timeButton.setText(s);
        }
      });
    }
  }
  
  /**
   * Perform actions when the window get into focus we start the activity by
   * reading out loud the current time
   */
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      Calendar cal = Calendar.getInstance();
      speakOut(parseTime(cal));
    }
  }
}
