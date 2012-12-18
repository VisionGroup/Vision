package com.yp2012g4.blindroid.tools;

import java.util.List;

import android.location.Address;

public interface LocationHandler {
  void handleLocation(double longitude, double latitude, String provider, List<Address> addresses);
}
