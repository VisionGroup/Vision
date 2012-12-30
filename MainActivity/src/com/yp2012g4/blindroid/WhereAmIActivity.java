package com.yp2012g4.blindroid;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.tools.LocationFinder;
import com.yp2012g4.blindroid.tools.LocationHandler;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

/**
 * An activity which will reads to the user his current location.
 * 
 * @author Olivier Hofman
 * @version 1.0
 */
public class WhereAmIActivity extends BlindroidActivity implements OnClickListener {
  private static void log(String s) {
    Log.d("WhereAmIActivity", s);
  }
  
  Lock l = null;
  String lastProvider = "";
  Date lastUpdate = null;
  long updateTimeOut = 60 * 1000; // 1 minute
  LocationFinder f;
  
  @Override public int getViewId() {
    return R.id.where_am_i_Activity;
  }
  
  void makeUseOfNewLocation(double longitude, double latitude, String provider, String address) {
    log("longitude = " + longitude + "\n");
    log("latitude = " + latitude + "\n");
    log("provider = " + provider + "\n");
    log("address: " + address);
    Date d = new Date();
    l.lock();
    if (lastUpdate == null || d.getTime() - lastUpdate.getTime() > updateTimeOut
        || lastProvider == LocationManager.NETWORK_PROVIDER && provider == LocationManager.GPS_PROVIDER) {
      lastUpdate = d;
      lastProvider = provider;
    }
    l.unlock();
    String toSpeak = "Your Location is: " + address;
    log("speaking out location: " + toSpeak + "\n");
    ((TextView) findViewById(R.id.where_am_i_textview)).setText(toSpeak);
    speakOut(toSpeak);
  }
  
  @Override public void onClick(View v) {
    switch (v.getId()) {
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
        speakOut("This is " + getString(R.string.title_activity_where_am_i));
        break;
      default:
        break;
    }
  }
  
  private void init() {
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
    f.run(h, true, true);
    log("Now running");
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_where_am_i);
    mHandler = new Handler();
    back = (TalkingImageButton) findViewById(R.id.back_button);
    back.setOnClickListener(this);
    back.setOnTouchListener(this);
    settings = (TalkingImageButton) findViewById(R.id.settings_button);
    settings.setOnClickListener(this);
    settings.setOnTouchListener(this);
    wai = (TalkingImageButton) findViewById(R.id.current_menu_button);
    wai.setOnClickListener(this);
    wai.setOnTouchListener(this);
    home = (TalkingImageButton) findViewById(R.id.home_button);
    home.setOnClickListener(this);
    home.setOnTouchListener(this);
    log("WhereAmIActivity::onCreate");
    l = new ReentrantLock();
  }
  
  @Override protected void onStart() {
    log("onStart");
    init();
    super.onStart();
  }
  
  @Override protected void onStop() {
    log("onstop");
    f.stop();
    log("f stopped");
    super.onStop();
  }
  
  @Override protected void onPause() {
    log("onpause");
    super.onPause();
  }
  
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }
}
