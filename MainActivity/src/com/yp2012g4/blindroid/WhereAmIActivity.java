package com.yp2012g4.blindroid;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.yp2012g4.blindroid.tools.BlindroidActivity;
import com.yp2012g4.blindroid.tools.LocationFinder;
import com.yp2012g4.blindroid.tools.LocationHandler;

/**
 * An activity which will reads to the user his current location.
 * 
 * @author Olivier Hofman
 * @version 1.0
 */
public class WhereAmIActivity extends BlindroidActivity {
  private static String TAG = "bd.WhereAmIActivity";
  
  // TODO: add a button to know the current status
  private static void log(String s) {
    Log.d(TAG, s);
  }
  
  Lock l = null;
  LocationFinder f;
  
  @Override public int getViewId() {
    return R.id.where_am_i_Activity;
  }
  
  void makeUseOfNewLocation(double longitude, double latitude, String provider, String address) {
    f.stop(); // we got our location: now, stop the finder.
    log("longitude = " + longitude + "\n");
    log("latitude = " + latitude + "\n");
    log("provider = " + provider + "\n");
    log("address: " + address);
    String toSpeak = "Your Location is: " + address;
    log("speaking out location: " + toSpeak + "\n");
    ((TextView) findViewById(R.id.where_am_i_textview)).setText(toSpeak);
    speakOut(toSpeak);
  }
  
  @Override public void onClick(View v) {
    super.onClick(v);
  }
  
  private void initialize() {
    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    log("Got location manager");
    f = new LocationFinder(manager);
    log("Got location finder");
    LocationHandler h = new LocationHandler() {
      @Override public void handleLocation(double longitude, double latitude, String provider, String address) {
        makeUseOfNewLocation(longitude, latitude, provider, address);
      }
    };
    log("Got location handler");
    f.run(h, true, false);
    log("Now running");
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_where_am_i);
    log("WhereAmIActivity::onCreate");
    l = new ReentrantLock();
    init(0/* TODO Check what icon goes here */, getString(R.string.whereami_whereami), getString(R.string.whereami_help));
  }
  
  @Override protected void onStart() {
    log("onStart");
    initialize();
    super.onStart();
  }
  
  @Override protected void onStop() {
    log("onstop");
    f.stop();
    log("Location finder stopped");
    super.onStop();
  }
  
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }
}
