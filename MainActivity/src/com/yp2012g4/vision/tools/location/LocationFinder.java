package com.yp2012g4.vision.tools.location;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.yp2012g4.vision.tools.StackTraceToString;

/**
 * This is a service which will wait for updates from a Location Provider
 * (Network, GPS), and call a listener, providing an address corresponding to
 * the current locations
 * 
 * @author Olivier Hofman
 * @version 1.0
 */
public class LocationFinder {
  private static final String TAG = "vision:LocationFinder";
  List<LocationListener> _listeners = null;
  LocationManager _locationManager;
  LocationHandler _handler;
  boolean _running = false; // to ensure we don't stop it twice
  
  public LocationFinder(final LocationManager newLocationManager) {
    _locationManager = newLocationManager;
  }
  
  /**
   * Start the daemon.
   * 
   * @param h
   *          the handler to call when getting location update
   * @return true if succeeded, false otherwise
   */
  public boolean run(final LocationHandler h) {
    if (_running)
      stop();
    _listeners = new ArrayList<LocationListener>();
    _handler = h;
    setUpLocationListener(LocationManager.GPS_PROVIDER);
    setUpLocationListener(LocationManager.NETWORK_PROVIDER);
    return _running;
  }
  
  private void setUpLocationListener(final String p) {
    if (_locationManager.isProviderEnabled(p)) {
      final LocationListener l = createLocationListener(p);
      _listeners.add(l);
      _locationManager.requestLocationUpdates(p, 0, 0, l);
      _running = true;
    }
  }
  
  void makeUseOfNewLocation(final Location location, final String provider) {
    Log.i(TAG, "Making use of new location from provider");
    final double latitude = location.getLatitude();
    final double longitude = location.getLongitude();
    Log.i(TAG, "latitude = " + latitude + ", longitude = " + longitude);
    OpenStreetMapGeocoding(latitude, longitude, provider);
  }
  
  /**
   * Log a string for this class
   * 
   * @param s
   *          the message to log
   */
  static void log(final String s) {
    Log.d(TAG, s);
  }
  
  /**
   * Create a location listener.
   * 
   * @param Provider
   *          The provider this listener will listen to
   * @return The listener
   */
  private LocationListener createLocationListener(final String Provider) {
    final LocationListener _l = new LocationListener() {
      @Override public void onLocationChanged(final Location location) {
        Log.i(TAG, "listener of provider " + Provider + ": location changed");
        makeUseOfNewLocation(location, Provider);
      }
      
      @Override public void onProviderEnabled(final String provider) {
        // No modifications
      }
      
      @Override public void onProviderDisabled(final String provider) {
        // No modifications
      }
      
      @Override public void onStatusChanged(final String provider, final int status, final Bundle extras) {
        // No modifications
      }
    };
    return _l;
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
    final AsyncTask<Void, Void, String> _downloader = new AsyncTask<Void, Void, String>() {
      @Override protected String doInBackground(final Void... params) {
        return OpenStreetMapGeocoder.getAddress(latitude, longitude);
      }
      
      @Override protected void onPostExecute(final String result) {
        super.onPostExecute(result);
        Log.i(TAG, "Geocoding: " + result);
        _handler.handleLocation(longitude, latitude, provider, result);
      }
    };
    try {
      _downloader.execute();
    } catch (final Exception e) {
      Log.e(TAG, "Error: " + StackTraceToString.toString(e));
    }
  }
  
  /**
   * Stop the service
   */
  public void stop() {
    if (!_running)
      return;
    Log.i(TAG, "stopped");
    if (_listeners != null) {
      for (final LocationListener l : _listeners) {
        Log.i(TAG, "removed a listener");
        _locationManager.removeUpdates(l);
      }
      _listeners.clear();
      _listeners = null;
    }
    Log.i(TAG, "end of stop");
  }
}
