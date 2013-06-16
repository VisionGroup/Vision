package com.yp2012g4.vision.test.tools;

import junit.framework.TestCase;

import com.yp2012g4.vision.tools.location.OpenStreetMapGeocoder;

/**
 * 
 * @author Amit Yaffe
 * 
 */
public class OpenStreetMapGeocoderTest extends TestCase {
  public OpenStreetMapGeocoderTest(final String name) {
    super(name);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
  }
  
  @Override protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  @SuppressWarnings("static-method") public void testGeoLocation() {
    assertEquals(
        "137, Pilkington Avenue, Castle Vale, Birmingham, West Midlands, England, B72 1LH, United Kingdom, European Union",
        OpenStreetMapGeocoder.getAddress(52.5487429714954, -1.81602098644987));
  }
}
