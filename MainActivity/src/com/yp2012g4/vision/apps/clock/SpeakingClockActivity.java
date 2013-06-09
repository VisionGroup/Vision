/***
 * This Activity display the time and date to the user and allow him to get read
 * this values aloud
 * 
 * @author Amir Blumental
 * @version 1.1
 */
package com.yp2012g4.vision.apps.clock;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.settings.VisionApplication;
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
    return DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(Calendar.getInstance().getTime());
  }
  
  /**
   * Parse the Calendar to a string to speak
   * 
   * @param cal
   *          the Calendar to parse string
   * @return string to speak
   */
  public static String parseTime(final Calendar cal, final Resources res) {
    final String ampm = cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
    final int h = cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR);
    return String.format(res.getString(R.string.hourFormat), Integer.valueOf(h), Integer.valueOf(cal.get(Calendar.MINUTE)), ampm);
  }
  
  /**
   * Parse the current time to a string;
   * 
   * @return string to speak
   */
  public static String parseTime(final Resources res) {
    return parseTime(Calendar.getInstance(), res);
  }
  
  @Override public int getViewId() {
    return R.id.SpeakingClockActivity;
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.TimeButton:
        speakOutAsync(parseTime(getResources()));
        break;
      case R.id.DateButton:
        speakOutAsync(getDateFormat());
        break;
      default:
        break;
    }
    return false;
  }
  
  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_speaking_clock);
    init(0, getString(R.string.ClockTitle), getString(R.string.speaking_clock_help));
    final TalkingButton dateButton = getTalkingButton(R.id.DateButton);
    dateButton.setText(getDateFormat());
    new Timer().schedule(new MyTimerTask(), 100, VisionApplication.DEFUALT_DELAY_TIME);
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
          final TalkingButton timeButton = getTalkingButton(R.id.TimeButton);
          final Calendar cal = Calendar.getInstance();
          final String ampm = cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
          final int h = cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR);
          final String s = h + " : " + cal.get(Calendar.MINUTE) + " " + ampm;
          timeButton.setText(s);
        }
      });
    }
  }
  
  /**
   * Perform actions when the window get into focus we start the activity by
   * reading out loud the current time
   */
  @Override public void onWindowFocusChanged(final boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus)
      speakOutAsync(parseTime(getResources()));
  }
}
