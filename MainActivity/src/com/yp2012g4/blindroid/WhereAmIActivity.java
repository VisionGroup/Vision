package com.yp2012g4.blindroid;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;

import com.yp2012g4.blindroid.tools.LocationFinder;
import com.yp2012g4.blindroid.tools.LocationHandler;

/**
 * 
 * 
 * @author Olivier Hofman
 * 
 */
public class WhereAmIActivity extends onTouchEventClass {
  Lock l = null;
  String lastProvider = "";
  Date lastUpdate = null;
  long updateTimeOut = 60 * 1000; // 1 minute
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_where_am_i);
    log("WhereAmIActivity::onCreate");
    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    log("Got location manager");
    LocationFinder f = new LocationFinder(manager);
    log("Got location finder");
    LocationHandler h = new LocationHandler() {
      @Override public void handleLocation(double longitude, double latitude, String provider, List<Address> addresses) {
        makeUseOfNewLocation(longitude, latitude, provider, addresses);
      }
    };
    log("Got location handler");
    f.run(h, true, true, this);
    log("Now running");
    l = new ReentrantLock();
    tts = new TextToSpeech(this, this);
  }
  
  void makeUseOfNewLocation(double longitude, double latitude, String provider, List<Address> addresses) {
    log("longitude = " + longitude + "\n");
    log("latitude = " + latitude + "\n");
    log("provider = " + provider + "\n");
    for (Address a : addresses) {
      log("new address: ");
      for (int i = 0; i <= a.getMaxAddressLineIndex(); ++i)
        log("\t" + a.getAddressLine(i));
    }
    if (addresses.isEmpty()) {
      speakOut("No addresses found");
      log("No addresses");
      return;
    }
    Date d = new Date();
    l.lock();
    if (lastUpdate == null || d.getTime() - lastUpdate.getTime() > updateTimeOut
        || lastProvider == LocationManager.NETWORK_PROVIDER && provider == LocationManager.GPS_PROVIDER) {
      lastUpdate = d;
      lastProvider = provider;
    }
    l.unlock();
    String toSpeak = "Your Location is: ";
    Address a = addresses.get(0);
    for (int i = 0; i <= a.getMaxAddressLineIndex(); ++i)
      toSpeak += a.getAddressLine(i) + " ";
    tts.speak(toSpeak, TextToSpeech.QUEUE_ADD, null);
  }
  
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }
  
  private static void log(String s) {
    Log.d("WhereAmIActivity", s);
  }
  
  @Override public int getViewId() {
    return R.id.where_am_i_Activity;
  }
}
