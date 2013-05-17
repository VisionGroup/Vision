/***
 * This is the alarm main activity
 * 
 * @author Amir Blumental
 * @version 1.0
 */
package com.yp2012g4.vision.alarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.clock.SetClockActivity;
import com.yp2012g4.vision.clock.SpeakingClockActivity;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.settings.VisionApplication;
import com.yp2012g4.vision.tools.VisionActivity;

public class AlarmActivity extends VisionActivity {
  public static final int USER_PRESSED_HOME = -2;
  public static final int USER_PRESSED_BACK = -1;
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
  public void callSetClock(final boolean isSettingMinutes) {
    waitForMinutes = isSettingMinutes;
    final int type = isSettingMinutes ? SetClockActivity.MIN_CODE : SetClockActivity.HOUR_CODE;
    startActivityForResult(new Intent(AlarmActivity.this, SetClockActivity.class).putExtra("type", type), REQUEST_CODE);
  }
  
  @Override public int getViewId() {
    return R.id.AlarmActivity;
  }
  
  /**
   * This will be called when the result from the set clock activity returns
   */
  @Override protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      if (USER_PRESSED_BACK == resultCode)
        return;
      if (USER_PRESSED_HOME == resultCode) {
        _mHandler.postDelayed(mLaunchTask, VisionApplication.DEFUALT_DELAY_TIME);
        return;
      }
      if (!waitForMinutes) {
        reqHour = resultCode;
        callSetClock(true);
        return;
      }
      alarmTime = Calendar.getInstance();
      alarmTime.setTimeInMillis(System.currentTimeMillis());
      alarmTime.set(Calendar.HOUR_OF_DAY, reqHour);
      alarmTime.set(Calendar.MINUTE, resultCode);
      final String s = getString(R.string.alarm_is_set_to) + " " + SpeakingClockActivity.parseTime(alarmTime, getResources());
      final TalkingButton buttonStatus = getTalkingButton(R.id.statusButton);
      buttonStatus.setReadText(SpeakingClockActivity.parseTime(alarmTime, getResources()));
      speakOutSync(s);
    }
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.statusButton:
        pressedStatusAlarm();
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
        cancelAlarm();
        break;
      default:
        return false;
    }
    return true;
  }
  
  /**
   * performed when user pressed the cancel alarm button
   */
  private void cancelAlarm() {
    final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    alarmManager.cancel(pendingIntent);
    if (AlarmService.mp != null)
      AlarmService.mp.stop();
    alarmIsSet = false;
    speakOutAsync(getString(R.string.alarm_is_canceled));
  }
  
  /**
   * performed when user pressed the alarm status button
   */
  private void pressedStatusAlarm() {
    String s;
    if (alarmTime == null)
      s = getString(R.string.noAlarm);
    else {
      if (alarmIsSet)
        s = getString(R.string.alarm_is_on_at);
      else
        s = getString(R.string.alarm_is_off_at);
      s = s + " " + SpeakingClockActivity.parseTime(alarmTime, getResources());
    }
    speakOutAsync(s);
  }
  
  /** Called when the activity is first created. */
  @Override public void onCreate(final Bundle savedInstanceState) {
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
      speakOutAsync(getString(R.string.you_need_to_set_ther_alarm_first));
      _tts.waitUntilFinishTalking();
      return;
    }
    final Intent myIntent = new Intent(AlarmActivity.this, AlarmService.class);
    pendingIntent = PendingIntent.getService(AlarmActivity.this, 0, myIntent, 0);
    final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    final Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    alarmTime.set(Calendar.SECOND, 0);
    if (alarmTime.before(calendar))
      alarmTime.roll(Calendar.DAY_OF_MONTH, true);
    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
    alarmIsSet = true;
    speakOutSync(getString(R.string.alarm_is_activated));
  }
}
