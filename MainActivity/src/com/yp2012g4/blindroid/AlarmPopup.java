package com.yp2012g4.blindroid;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class AlarmPopup extends onTouchEventClass{
  static public MediaPlayer mp = null;
  private AlertDialog ad;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    tts = new TextToSpeech(this, this);
    setDialog();
    soundAlarm();
  }
  
  private void setDialog() {
    ad = new AlertDialog.Builder(this).create();
    ad.setTitle("Alarm clock");
    ad.setMessage("The alarm you set is on");
    ad.setButton("Turn off", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mp.stop();
        finish();
      }
    });
    ad.setCanceledOnTouchOutside(true);
    ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
    	  mp.stop();
    	  finish();
      }
    });
//Showing Alert Message
    try {
      ad.show();
    } catch (Exception e) {
      Log.e("amir", "exception in show!!!" + getApplicationContext());
    }
  }
  
  private void soundAlarm() {
    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
    mp.start();
    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mpc) {
        ad.cancel();
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
  
}