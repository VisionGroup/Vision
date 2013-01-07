package com.yp2012g4.blindroid.tools;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * This is a service which will wait for updates from a Location Provider
 * (Network, GPS), and call a listener, providing an address corresponding to
 * the current locations
 * 
 * @author Olivier Hofman
 * @version 1.0
 */
public class LocationFinder {
  List<LocationListener> listeners = null;
  LocationManager locationManager;
  LocationHandler handler;
  
  public LocationFinder(LocationManager newLocationManager) {
    locationManager = newLocationManager;
    log("created");
  }
  
  /**
   * Start the daemon.
   * 
   * @param h
   *          the handler to call when getting location update
   * @param useGPS
   *          Do we try to use the GPS
   * @param useNetwork
   *          Do we try to use the Network based location. Note: if this is set
   *          to false, but we couldn't use the GPS, we will still use the
   *          network.
   */
  public void run(LocationHandler h, boolean useGPS, boolean useNetwork) {
    log("run");
    if (!useGPS && !useNetwork)
      Log.e("LocationFinder", "both useGPS and useNetwork are false");
    listeners = new ArrayList<LocationListener>();
    handler = h;
    String p = "";
    boolean GPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    if (useGPS && GPSenabled) {
      p = LocationManager.GPS_PROVIDER;
      log("enabling GPS");
      LocationListener l = createLocationListener(p);
      listeners.add(l);
      locationManager.requestLocationUpdates(p, 0, 0, l);
    } else
      log("Could not use GPS because it is disabled");
    p = LocationManager.NETWORK_PROVIDER;
    if (useNetwork || !GPSenabled)
      if (locationManager.isProviderEnabled(p)) {
        log("enabling Network location");
        LocationListener l = createLocationListener(p);
        listeners.add(l);
        locationManager.requestLocationUpdates(p, 0, 0, l);
      } else
        log("Could not use Network location because it is disabled");
  }
  
  void makeUseOfNewLocation(Location location, String provider) {
    log("Making use of new location from provider");
    double latitude = location.getLatitude();
    double longitude = location.getLongitude();
    log("latitude = " + latitude + ", longitude = " + longitude);
    OpenStreetMapGeocoding(latitude, longitude, provider);
  }
  
  /**
   * Log a string for this class
   * 
   * @param s
   *          the message to log
   */
  static void log(String s) {
    Log.d("LocationFinder", s);
  }
  
  /**
   * Create a location listener.
   * 
   * @param Provider
   *          The provider this listener will listen to
   * @return The listener
   */
  private LocationListener createLocationListener(final String Provider) {
    LocationListener l = new LocationListener() {
      @Override public void onLocationChanged(Location location) {
        log("listener of provider " + Provider + ": location changed");
        makeUseOfNewLocation(location, Provider);
      }
      
      @Override public void onProviderEnabled(String provider) {
        // No modifications
      }
      
      @Override public void onProviderDisabled(String provider) {
        // No modifications
      }
      
      @Override public void onStatusChanged(String provider, int status, Bundle extras) {
        // No modifications
      }
    };
    return l;
  }
  
  /**
   * Start an asynchronous task to convert coordinates to an address
   * 
   * @param latitude
   *          the latitude
   * @param longitude
   *          the longitude
   * @param provider
   *          the provider of the current location
   */
  void OpenStreetMapGeocoding(final double latitude, final double longitude, final String provider) {
    AsyncTask<Void, Void, String> downloader = new AsyncTask<Void, Void, String>() {
      @Override protected String doInBackground(Void... params) {
        return OpenStreetMapGeocoder.GetAddress(latitude, longitude);
      }
      
      @Override protected void onPostExecute(String result) {
        super.onPostExecute(result);
        log("Geocoding: " + result);
        handler.handleLocation(longitude, latitude, provider, result);
      }
    };
    try {
      downloader.execute();
    } catch (Exception e) {
      log("Error: " + e.getMessage());
    }
  }
  
  /**
   * Stop the service
   */
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
