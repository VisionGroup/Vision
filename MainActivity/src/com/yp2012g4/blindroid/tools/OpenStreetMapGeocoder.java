package com.yp2012g4.blindroid.tools;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.util.Log;

/**
 * Converts geographic coordinates to an address, using OpenStreetMap
 * 
 * @author Olivier Hofman
 * @version 1.0
 */
public class OpenStreetMapGeocoder {
  /**
   * Return an address.
   * 
   * @param latitude
   *          The latitude
   * @param longitude
   *          The longitude
   * @return A string describing the address
   */
  public static String GetAddress(final double latitude, final double longitude) {
    String r = "";
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    try {
      URL sourceUrl = new URL("http://nominatim.openstreetmap.org/reverse?accept-language=en&lat=" + latitude + "&lon=" + longitude);
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document d = dBuilder.parse(new InputSource(sourceUrl.openStream()));
      r = d.getElementsByTagName("result").item(0).getChildNodes().item(0).getNodeValue();
    } catch (Exception e) {
      Log.d("OpenStreetMapGeocoder", "Error: " + e.getMessage());
    }
    return r;
  }
}
