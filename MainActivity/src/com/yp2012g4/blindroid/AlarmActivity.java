/***
 * This is the alarm main activity
 * 
 * @author Amir Blumental
 * @version 1.0
 */
package com.yp2012g4.blindroid;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.tools.BlindroidActivity;

public class AlarmActivity extends BlindroidActivity {
  public static PendingIntent pendingIntent;
  public static boolean alarmIsSet = false;
  public int lastHour = -1;
  public int lastMin = -1;
  public static Calendar alarmTime = null;
  boolean waitForMinutes;
  private int reqHour;
  final static int REQUEST_CODE = 1234;
  
  /**
   * This method call set clock activity
   * 
   * @param isSettingMinutes
   *          - tell the activity if we want to set hours or minutes
   */
  public void callSetClock(boolean isSettingMinutes) {
    waitForMinutes = isSettingMinutes;
    Intent i = new Intent(AlarmActivity.this, SetClockActivity.class);
    int type = isSettingMinutes ? SetClockActivity.MIN_CODE : SetClockActivity.HOUR_CODE;
    i.putExtra("type", type);
    startActivityForResult(i, REQUEST_CODE);
  }
  
  @Override
  public int getViewId() {
    return R.id.AlarmActivity;
  }
  
  /**
   * This will be called when the result from the set clock activity returnes
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      // if user pressed back
      if (-1 == resultCode)
        return;
      // if user pressed home
      if (-2 == resultCode) {
        mHandler.postDelayed(mLaunchTask, 10);
        return;
      }
      if (waitForMinutes) {
        alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(System.currentTimeMillis());
        alarmTime.set(Calendar.HOUR_OF_DAY, reqHour);
        alarmTime.set(Calendar.MINUTE, resultCode);
        String s = "Alarm is set to  " + SpeakingClockActivity.parseTime(alarmTime);
        TalkingButton buttonStatus = (TalkingButton) findViewById(R.id.statusButton);
        buttonStatus.setReadText(SpeakingClockActivity.parseTime(alarmTime));
        speakOut(s);
        while (_t.isSpeaking() == true) {
          // Wait for message to finish playing and then finish the activity
        }
      } else {
        reqHour = resultCode;
        callSetClock(true);
      }
    }
  }
  
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    switch (curr_view.getId()) {
      case R.id.statusButton:
        String s;
        if (alarmTime == null)
          s = getString(R.string.noAlarm);
        else {
          if (alarmIsSet)
            s = "Alarm is On at ";
          else
            s = "Alarm is Off at ";
          s = s + SpeakingClockActivity.parseTime(alarmTime);
        }
        speakOut(s);
        break;
      case R.id.setButton:
        callSetClock(false);
        break;
      case R.id.startalarm:
        setAlarm();
        break;
      case R.id.cancelalarm:
        if (!alarmIsSet)
          return false;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        if (AlarmService.mp != null)
          AlarmService.mp.stop();
        alarmIsSet = false;
        speakOut("Alarm is Canceled");
        break;
    }
    return false;
  }
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alarm);
    init(0, getString(R.string.title_activity_alarm), getString(R.string.alarm_clock_help));
  }
  
  /**
   * set the alarm to hour:min
   * 
   * @param hour
   *          - hour in the 24 format
   * @param min
   *          - minutes between 0-59
   */
  public void setAlarm() {
    if (alarmTime == null) {
      speakOut("You need to set the alarm first ");
      while (_t.isSpeaking() == true) {
        // Wait for message to finish playing and then finish the activity
      }
      return;
    }
    Intent myIntent = new Intent(AlarmActivity.this, AlarmService.class);
    pendingIntent = PendingIntent.getService(AlarmActivity.this, 0, myIntent, 0);
    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    alarmTime.set(Calendar.SECOND, 0);
    if (alarmTime.before(calendar))
      alarmTime.roll(Calendar.DAY_OF_MONTH, true);
    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
    alarmIsSet = true;
    speakOut("Alarm is activated ");
    while (_t.isSpeaking() == true) {
      // Wait for message to finish playing and then finish the activity
    }
  }
}
