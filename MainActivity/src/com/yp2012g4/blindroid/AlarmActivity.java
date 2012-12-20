package com.yp2012g4.blindroid;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmActivity extends Activity implements TextToSpeech.OnInitListener {
  public TextToSpeech tts;
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
    Button buttonStart = (Button) findViewById(R.id.startalarm);
    Button buttonCancel = (Button) findViewById(R.id.cancelalarm);
    buttonStart.setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        Intent myIntent = new Intent(AlarmActivity.this, AlarmService.class);
        pendingIntent = PendingIntent.getService(AlarmActivity.this, 0, myIntent, 0);
        TimePicker tp = (TimePicker) findViewById(R.id.timePicker1);
        Integer hour = tp.getCurrentHour();
        Integer min = tp.getCurrentMinute();
        setAlarm(hour, min);
      }
    });
    buttonCancel.setOnClickListener(new Button.OnClickListener() {
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
        Toast.makeText(AlarmActivity.this, "Alarm is Canceled", Toast.LENGTH_LONG).show();
        speakOut("Alarm is Canceled");
      }
    });
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
  
  @Override
  public void onDestroy() {
    if (tts != null) {
      tts.stop();
      tts.shutdown();
    }
    super.onDestroy();
  }
  
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
  
  /**
   * set the alarm to hour:min
   * 
   * @param hour
   *          - hour in the 24 format
   * @param min
   *          - minutes between 0-59
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
    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
    alarmIsSet = true;
    String s = "Alarm is set to  " + SpeakingClockActivity.parseTime(alarmTime);
    Toast.makeText(AlarmActivity.this, s, Toast.LENGTH_LONG).show();
    speakOut(s);
  }
  
  public void speakOut(String s) {
    tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
  }
}