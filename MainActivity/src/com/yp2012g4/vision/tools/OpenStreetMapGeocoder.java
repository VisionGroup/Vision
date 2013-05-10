package com.yp2012g4.vision.tools;

import java.net.URL;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;

import android.util.Log;

/**
 * Converts geographic coordinates to an address, using OpenStreetMap
 * 
 * @author Olivier Hofman
 * @version 1.0
 */
public class OpenStreetMapGeocoder {
  private static final String TAG = "vision:OpenStreetMapGeocoder";
  private static final String _srcUrl = "http://nominatim.openstreetmap.org/reverse?accept-language=en&lat=%1$f&lon=%2$f";
  private static final String _adrTag = "result";
  
  /**
   * Return an address.
   * 
   * @param latitude
   *          The latitude
   * @param longitude
   *          The longitude
   * @return A string describing the address
   */
  public static String getAddress(final double lat, final double lon) {
    String r = "";
    final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    try {
      r = dbFactory
          .newDocumentBuilder()
          .parse(new InputSource(new URL(String.format(Locale.US, _srcUrl, Double.valueOf(lat), Double.valueOf(lon))).openStream()))
          .getElementsByTagName(_adrTag).item(0).getChildNodes().item(0).getNodeValue();
    } catch (final Exception e) {
      Log.e(TAG, "Error: " + e.getMessage());
    }
    return r;
  }
}
