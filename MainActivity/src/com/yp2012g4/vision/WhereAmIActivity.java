package com.yp2012g4.vision;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.yp2012g4.vision.tools.LocationFinder;
import com.yp2012g4.vision.tools.LocationHandler;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * An activity which will reads to the user his current location.
 * 
 * @author Olivier Hofman
 * @version 1.0
 */
public class WhereAmIActivity extends VisionActivity {
  private static String TAG = "vision:WhereAmIActivity";
  
  // TODO: add a button to know the current status
  private static void log(String s) {
    Log.d(TAG, s);
  }
  
  Lock l = null;
  LocationFinder f;
  String provider;
  String text;
  
  @Override public int getViewId() {
    return R.id.where_am_i_Activity;
  }
  
  void makeUseOfNewLocation(double longitude, double latitude, String prov, String address) {
    f.stop(); // we got our location: now, stop the finder.
    l.lock();
    log("longitude = " + longitude + "\n");
    log("latitude = " + latitude + "\n");
    log("provider = " + prov + "\n");
    log("address: " + address);
    final String toSpeak = "Your Location is: " + address;
    setText(toSpeak);
    log("speaking out location: " + toSpeak + "\n");
    l.unlock();
    speakOut(toSpeak);
  }
  
  private void initialize() {
    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    log("Got location manager");
    f = new LocationFinder(manager);
    log("Got location finder");
    provider = f.run(new LocationHandler() {
      @Override public void handleLocation(double longitude, double latitude, String prov, String address) {
        makeUseOfNewLocation(longitude, latitude, prov, address);
      }
    });
    log("Now running");
    l.lock();
    if (provider.equals("")) {
      final String s = "Could not find a location.";
      setText(s);
      speakOut(s);
    } else if (provider.equals(LocationManager.GPS_PROVIDER))
      setText(getString(R.string.find_location_with_GPS));
    else if (provider.equals(LocationManager.NETWORK_PROVIDER))
      setText(getString(R.string.find_location_with_network));
    l.unlock();
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_where_am_i);
    init(0, getString(R.string.where_am_i_screen), getString(R.string.where_am_i_help));
    log("WhereAmIActivity::onCreate");
    l = new ReentrantLock();
    l.lock();
    setText("Initializing...");
    l.unlock();
  }
  
  private void setText(String s) {
    text = s;
    ((TextView) findViewById(R.id.where_am_i_textview)).setText(s);
  }
  
  @Override protected void onStart() {
    log("onStart");
    initialize();
    super.onStart();
  }
  
  @Override protected void onStop() {
    log("onStop");
    f.stop();
    log("Location finder stopped");
    super.onStop();
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    final int id = curr_view.getId();
    if (id == -1 || id == R.id.where_am_i_Activity || id == R.id.where_am_i_textview) {
      l.lock();
      speakOut(text);
      l.unlock();
      return false;
    }
    super.onSingleTapUp(e);
    return false;
  }
}
