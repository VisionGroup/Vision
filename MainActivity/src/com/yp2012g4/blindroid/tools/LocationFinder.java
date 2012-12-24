package com.yp2012g4.blindroid.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationFinder {
  List<LocationListener> listeners = null;
  LocationManager locationManager;
  LocationHandler handler;
  Geocoder coder = null;
  
  public LocationFinder(LocationManager newLocationManager) {
    locationManager = newLocationManager;
    log("created");
  }
  
  public void run(LocationHandler h, boolean useGPS, boolean useNetwork, Context con) {
    log("run");
    coder = new Geocoder(con);
    listeners = new ArrayList<LocationListener>();
    handler = h;
    String p = "";
    if (useGPS)
      p = LocationManager.GPS_PROVIDER;
    if (locationManager.isProviderEnabled(p)) {
      log("enabling GPS");
      LocationListener l = createLocationListener(p);
      listeners.add(l);
      locationManager.requestLocationUpdates(p, 0, 0, l);
    } else
      log("Could not use GPS because it is disabled");
    p = LocationManager.NETWORK_PROVIDER;
    if (useNetwork)
      if (locationManager.isProviderEnabled(p)) {
        log("enabling Network location");
        LocationListener l = createLocationListener(p);
        listeners.add(l);
        locationManager.requestLocationUpdates(p, 0, 0, l);
      } else
        log("Could not use Network location because it is disabled");
  }
  
  private void makeUseOfNewLocation(Location location, String provider) {
    log("Making use of new location from provider");
    double latitude = location.getLatitude();
    double longitude = location.getLongitude();
    log("latitude = " + latitude + ", longitude = " + longitude);
    if (coder == null)
      log("Error in makeUseOfNewLocation: the coder was not initialised");
    List<Address> addresses = null;
    try {
      addresses = coder.getFromLocation(latitude, longitude, 1);
      if (addresses.isEmpty())
        log("No address returned");
    } catch (Exception e) {
      log("Error in getFromLocation: " + e.getMessage());
      return;
    }
    handler.handleLocation(longitude, latitude, provider, addresses);
  }
  
  void log(String s) {
    Log.d("LocationFinder", s);
  }
  
  private LocationListener createLocationListener(final String Provider) {
    LocationListener l = new LocationListener() {
      @Override public void onLocationChanged(Location location) {
        log("listener of provider " + Provider + ": location changed");
        makeUseOfNewLocation(location, Provider);
      }
      
      @Override public void onProviderEnabled(String provider) {
      }
      
      @Override public void onProviderDisabled(String provider) {
      }
      
      @Override public void onStatusChanged(String provider, int status, Bundle extras) {
      }
    };
    return l;
  }
  
  public void stop() {
    log("stopped");
    for (LocationListener l : listeners) {
      log("removed a listener");
      locationManager.removeUpdates(l);
    }
    listeners.clear();
    listeners = null;
    log("end of stop");
  }
}
