package com.yp2012g4.blindroid.tools;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
    coder = new Geocoder(con, Locale.US);
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
  
  void makeUseOfNewLocation(Location location, String provider) {
    log("Making use of new location from provider");
    double latitude = location.getLatitude();
    double longitude = location.getLongitude();
    log("latitude = " + latitude + ", longitude = " + longitude);
    if (coder == null)
      log("Error in makeUseOfNewLocation: the coder was not initialised");
    List<Address> addresses = null;
    OpenStreetMapGeocoding(latitude, longitude);
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
  
  static void log(String s) {
    Log.d("LocationFinder", s);
  }
  
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
  
  static void OpenStreetMapGeocoding(double latitude, double longitude) {
    AsyncTask<URL, Void, String> downloader = new AsyncTask<URL, Void, String>() {
      @Override protected String doInBackground(URL... params) {
        String r = "";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
          DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
          Document d = dBuilder.parse(new InputSource(params[0].openStream()));
          r = d.getElementsByTagName("result").item(0).getChildNodes().item(0).getNodeValue();
        } catch (Exception e) {
          log("Error: " + e.getMessage());
        }
        return r;
      }
      
      @Override protected void onPostExecute(String result) {
        super.onPostExecute(result);
        log("Geocoding: " + result);
      }
    };
    try {
      URL sourceUrl = new URL("http://nominatim.openstreetmap.org/reverse?accept-language=en&lat=" + latitude + "&lon=" + longitude);
      // String result = log("OpenStreetMap Geocoding: " + result);
      downloader.execute(sourceUrl);
    } catch (Exception e) {
      log("Error: " + e.getMessage());
    }
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
