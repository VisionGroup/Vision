package com.yp2012g4.blindroid;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;

import com.yp2012g4.blindroid.tools.BlindroidActivity;
import com.yp2012g4.blindroid.tools.LocationFinder;
import com.yp2012g4.blindroid.tools.LocationHandler;

/**
 * This class is an activity which sends a pre-defined SOS message to a
 * pre-defined contact
 * 
 * @author Amir
 * @version 1.0
 */
public class SOSActivity extends BlindroidActivity {
  LocationFinder f;
  Lock l = null;
  String lastProvider = "", address = "";
  double latitude, longitude;
  
  @Override public int getViewId() {
    return R.id.SOS_textview;
  }
  
  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.Send_SOS_Message:
        String messageToSend = "I need your help!";
        String number = "0529240424";
        SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
        speakOut("SOS message has been sent");
        mHandler.postDelayed(mLaunchTask, 1300);
        break;
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
        speakOut("This is " + getString(R.string.title_activity_sos));
        break;
      default:
        break;
    }
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sos);
    l = new ReentrantLock();
  }
  
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus)
      speakOut("SOS screen");
  }
  
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_sos, menu);
    return true;
  }
  
  void makeUseOfNewLocation(double lon, double lat, String provider, String addr) {
    if (provider == LocationManager.NETWORK_PROVIDER && lastProvider == LocationManager.GPS_PROVIDER)
      return;
    l.lock();
    address = addr;
    lastProvider = provider;
    latitude = lat;
    longitude = lon;
    l.unlock();
  }
  
  @Override protected void onStart() {
    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    f = new LocationFinder(manager);
    LocationHandler h = new LocationHandler() {
      @Override public void handleLocation(double lon, double lat, String provider, String addr) {
        makeUseOfNewLocation(lon, lat, provider, addr);
      }
    };
    f.run(h, true, true);
    super.onStart();
  }
  
  @Override protected void onStop() {
    f.stop();
    super.onStop();
  }
}
