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
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

public class AlarmActivity extends BlindroidActivity implements OnClickListener {
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
  
  @Override public int getViewId() {
    return R.id.AlarmActivity;
  }
  
  /**
   * This will be called when the result from the set clock activity returnes
   */
  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        while (_t.isSpeaking() == Boolean.TRUE) {
          // Wait for message to finish playing and then finish the activity
        }
      } else {
        reqHour = resultCode;
        callSetClock(true);
      }
    }
  }
  
  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.back_button:
        speakOut("Previous screen");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.settings_button:
        speakOut("Settings");
        Intent intent = new Intent(this, DisplaySettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.home_button:
        speakOut("Home");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.current_menu_button:
        speakOut("This is " + getString(R.string.title_activity_alarm));
        break;
      default:
        break;
    }
  }
  
  /** Called when the activity is first created. */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alarm);
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
    TalkingButton buttonStart = (TalkingButton) findViewById(R.id.startalarm);
    TalkingButton buttonCancel = (TalkingButton) findViewById(R.id.cancelalarm);
    TalkingButton buttonSet = (TalkingButton) findViewById(R.id.setButton);
    TalkingButton buttonStatus = (TalkingButton) findViewById(R.id.statusButton);
    buttonStatus.setOnTouchListener(this);
    buttonStatus.setOnClickListener(new TalkingButton.OnClickListener() {
      @Override public void onClick(View v) {
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
      }
    });
    buttonSet.setOnClickListener(new TalkingButton.OnClickListener() {
      @Override public void onClick(View v) {
        callSetClock(false);
      }
    });
    buttonSet.setOnTouchListener(this);
    buttonStart.setOnClickListener(new TalkingButton.OnClickListener() {
      @Override public void onClick(View arg0) {
        setAlarm();
      }
    });
    buttonStart.setOnTouchListener(this);
    buttonCancel.setOnClickListener(new TalkingButton.OnClickListener() {
      @Override public void onClick(View arg0) {
        if (!alarmIsSet)
          return;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        if (AlarmService.mp != null)
          AlarmService.mp.stop();
        alarmIsSet = false;
        speakOut("Alarm is Canceled");
      }
    });
    buttonCancel.setOnTouchListener(this);
  }
  
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      speakOut("Alarm screen");
    }
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
      while (_t.isSpeaking() == Boolean.TRUE) {
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
    while (_t.isSpeaking() == Boolean.TRUE) {
      // Wait for message to finish playing and then finish the activity
    }
  }
}
