package com.yp2012g4.vision;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
  final private static String TAG = "vision:WhereAmIActivity";
  final private static int updateTimeOut = 60000; // in miliseconds
  
  // TODO: add a button to know the current status
  private static void log(final String s) {
    Log.d(TAG, s);
  }
  
  Lock l = null;
  LocationFinder f;
  boolean providerRunning;
  String text;
  Date lastUpdate;
  
  @Override public int getViewId() {
    return R.id.where_am_i_Activity;
  }
  
  void makeUseOfNewLocation(final double longitude, final double latitude, final String prov, final String address) {
    l.lock();
    if (new Date().getTime() - lastUpdate.getTime() < updateTimeOut) {
      l.unlock();
      return;
    }
    lastUpdate = new Date();
    log("longitude = " + longitude + "\n");
    log("latitude = " + latitude + "\n");
    log("provider = " + prov + "\n");
    log("address: " + address);
    final String toSpeak = getString(R.string.your_location_is) + ": " + address;
    setText(toSpeak);
    log("speaking out location: " + toSpeak + "\n");
    l.unlock();
    speakOutAsync(toSpeak);
  }
  
  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_where_am_i);
    init(0, getString(R.string.where_am_i_screen), getString(R.string.where_am_i_help));
    log("WhereAmIActivity::onCreate");
    l = new ReentrantLock();
    l.lock();
    lastUpdate = new GregorianCalendar(1800, 1, 1).getTime();
    setText(getString(R.string.initializing));
    l.unlock();
  }
  
  private void setText(final String s) {
    text = s;
    ((TextView) findViewById(R.id.where_am_i_textview)).setText(s);
  }
  
  @Override protected void onResume() {
    log("onResume");
    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    log("Got location manager");
    f = new LocationFinder(manager);
    log("Got location finder");
    providerRunning = f.run(new LocationHandler() {
      @Override public void handleLocation(final double longitude, final double latitude, final String prov, final String address) {
        makeUseOfNewLocation(longitude, latitude, prov, address);
      }
    });
    log("Now running");
    l.lock();
    if (!providerRunning) {
      final String s = getString(R.string.could_not_find_a_location);
      setText(s);
      speakOutAsync(s);
    } else
      setText(getString(R.string.finding_location));
    l.unlock();
    super.onResume();
  }
  
  @Override protected void onPause() {
    log("onPause");
    f.stop();
    log("Location finder stopped");
    super.onPause();
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View button = getButtonByMode();
    switch (button.getId()) {
      case -1:
      case R.id.where_am_i_Activity:
      case R.id.where_am_i_textview:
        l.lock();
        speakOutAsync(text);
        l.unlock();
        break;
      default:
        break;
    }
    return false;
  }
}
