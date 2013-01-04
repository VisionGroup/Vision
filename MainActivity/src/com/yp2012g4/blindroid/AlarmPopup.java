/***
 * This class is responsible for the alarm popup when the alarm is beeping
 * 
 * @author Amir Blumental
 * @version 1.0
 */
package com.yp2012g4.blindroid;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class AlarmPopup extends Activity implements TextToSpeech.OnInitListener {
  static public MediaPlayer mp = null;
  protected TextToSpeech tts;
  private boolean left = false;
  
  /***
   * On the creation of the class we display the dialog and sound the alarm
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    tts = new TextToSpeech(this, this);
    soundAlarm();
  }
  
  @Override public void onDestroy() {
    if (tts != null) {
      speakOut("stop");
      tts.stop();
      tts.shutdown();
    }
    super.onDestroy();
  }
  
  @Override public void onInit(int status) {
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
   * on any button press or screen touch we turn the snooze off
   */
  @Override public void onUserInteraction() {
    if (left)
      return;
    left = true;
    mp.stop();
    AlarmActivity.alarmIsSet = false;
    speakOut("Alarm is turned Off");
    while (tts.isSpeaking()) {
      // Wait for message to finish playing and then finish the activity
    }
    finish();
    super.onUserInteraction();
  }
  
  /**
   * Sounds the alarm and on completion of the alarm set the alarm to snoozeTime
   */
  private void soundAlarm() {
    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
    mp.start();
    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override public void onCompletion(MediaPlayer mpc) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(AlarmPopup.this, AlarmService.class);
        AlarmActivity.pendingIntent = PendingIntent.getService(AlarmPopup.this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String snooze = getResources().getString(R.string.snooze_time);
        int snoozeTime = 5;
        try {
          snoozeTime = Integer.valueOf(snooze).intValue();
        } catch (NumberFormatException e) {
          Log.e("amir", "exception in parseInt!!! with " + snooze);
        }
        calendar.roll(Calendar.MINUTE, snoozeTime);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmActivity.pendingIntent);
        AlarmActivity.alarmIsSet = true;
        AlarmActivity.alarmTime = calendar;
        speakOut("snooze for " + snooze + "minutes");
        while (tts.isSpeaking()) {
          // Wait for message to finish playing and then finish the activity
        }
        finish();
      }
    });
  }
  
  public void speakOut(String s) {
    tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
  }
}
