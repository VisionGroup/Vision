package com.yp2012g4.blindroid;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
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
public class WhereAmIActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    setContentView(R.layout.); // TODO
    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    LocationFinder f = new LocationFinder(manager);
    LocationHandler h = new LocationHandler() {
      @Override public void handleLocation(double longitude, double latitude, String provider, List<Address> addresses) {
        log("longitude = " + longitude + "\n");
        log("latitude = " + latitude + "\n");
        log("provider = " + provider + "\n");
        for (Address a : addresses) {
          log("new address: ");
          for (int i = 0; i <= a.getMaxAddressLineIndex(); ++i)
            log("\t" + a.getAddressLine(i));
        }
      }
    };
    f.run(h, true, true, this);
  }
  
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }
  
  private void log(String s) {
    Log.d("WhereAmIActivity", s);
  }
}
