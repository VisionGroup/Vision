package com.yp2012g4.vision.tools;

/**
 * An interface for a location handler. When a location finder gets a new
 * update, he will call a location handler
 * 
 * @author Olivier Hofman
 * @version 1.0
 * 
 */
public interface LocationHandler {
  /**
   * Handles a location
   * 
   * @param longitude
   *          the longitude
   * @param latitude
   *          the latitude
   * @param provider
   *          the provider (a string which is mostly either
   *          LocationManager.NETWORK_PROVIDER or LocationManager.GPS_PROVIDER
   * @param address
   *          the address of the new location
   */
  void handleLocation(double longitude, double latitude, String provider, String address);
}
