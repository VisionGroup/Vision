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
//  void test() {
//    assertTrue(OpenStreetMapGeocoder.GetAddress(32.8, 35).equals("Hana Senesh, Ramat Hadar, Haifa, Haifa District, 34454, Israel"));
//    assertTrue(OpenStreetMapGeocoder.GetAddress(33, 34.992203).equals("Shave Ziyyon, North District, Israel"));
//    assertTrue(OpenStreetMapGeocoder.GetAddress(32.8155, 34.979564).equals(
//        "Yefe Nof, Carmel Zarphati (French Carmel), Haifa, Haifa District, 31064, Israel"));
//  }
}
