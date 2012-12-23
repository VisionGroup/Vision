package com.yp2012g4.blindroid;


import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends Service {
  static public MediaPlayer mp = null;
  
  @Override
  public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    Toast.makeText(AlarmService.this, "onBind", Toast.LENGTH_LONG).show();
    return null;
  }
  
  @Override
  public void onCreate() {
    // TODO Auto-generated method stub
  }
  
  @Override
  public void onDestroy() {
    // TODO Auto-generated method stub
  }
  
  @Override
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);
    Calendar maxTime = Calendar.getInstance();
    maxTime.add(Calendar.MINUTE, 1);
    Calendar minTime = Calendar.getInstance();
    minTime.add(Calendar.MINUTE, -1);
    if (AlarmActivity.alarmTime.before(maxTime) && AlarmActivity.alarmTime.after(minTime)) {
      Intent dialogIntent = new Intent(this, AlarmPopup.class);
      dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Log.i("amir","before start");
      getApplication().startActivity(dialogIntent);
      AlarmActivity.alarmIsSet = false;
    }
  }
  
  @Override
  public boolean onUnbind(Intent intent) {
    // TODO Auto-generated method stub
    return super.onUnbind(intent);
  }
}
