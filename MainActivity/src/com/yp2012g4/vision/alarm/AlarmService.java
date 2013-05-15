/***
 * alarm service that wait for a set alarm to go off
 * 
 * @author Amir Blumental
 * @version 1.0
 */
package com.yp2012g4.vision.alarm;

import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class AlarmService extends Service {
  static public MediaPlayer mp = null;
  
  @Override public IBinder onBind(final Intent intent) {
    Toast.makeText(AlarmService.this, "onBind", Toast.LENGTH_LONG).show();
    return null;
  }
  
  /***
   * this method is called when a scheduled Alarm time arrives
   */
  @Override public void onStart(final Intent intent, final int startId) {
    super.onStart(intent, startId);
    final Calendar maxTime = Calendar.getInstance();
    maxTime.add(Calendar.MINUTE, 1);
    final Calendar minTime = Calendar.getInstance();
    minTime.add(Calendar.MINUTE, -1);
    if (AlarmActivity.alarmTime.before(maxTime) && AlarmActivity.alarmTime.after(minTime)) {
      final Intent dialogIntent = new Intent(this, AlarmPopup.class);
      dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      getApplication().startActivity(dialogIntent);
      AlarmActivity.alarmIsSet = false;
    }
  }
}
