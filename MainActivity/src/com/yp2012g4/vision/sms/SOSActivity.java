package com.yp2012g4.vision.sms;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.tools.LocationFinder;
import com.yp2012g4.vision.tools.LocationHandler;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * This class is an activity which sends a pre-defined SOS message to a
 * pre-defined contact
 * 
 * @author Amir
 * @version 1.0
 */
public class SOSActivity extends VisionActivity {
  LocationFinder f;
  Lock l = null;
  String lastProvider = "", address = "";
  // these are non-initialized values
  double latitude = 10000, longitude = 10000;
  final int maxLengthOfAddress = 100;
  static final String TAG = "vision:SOSActivity";
  
  @Override public int getViewId() {
    return R.id.SOS_textview;
  }
  
  /**
   * Send an SOS message
   */
  public Runnable sendSOSMessage = new Runnable() {
    @Override public void run() {
      String messageToSend = "I need your help!";
      l.lock();
      if (latitude != 10000) {
        messageToSend += "\nMy location is: latitude = " + latitude + "; longitude = " + longitude + ";\nMy address is ";
        if (address.length() > maxLengthOfAddress)
          messageToSend += address.substring(0, maxLengthOfAddress);
        else
          messageToSend += address;
      }
      l.unlock();
      final String number = "0529240424";
      // String number = "0543064260"; // Olivier's number
      Log.d(TAG, "Sending SOS : " + messageToSend);
      Log.d(TAG, "number : " + number);
      Log.d(TAG, "latitude =  " + latitude);
      Log.d(TAG, "longitude =  " + longitude);
      final SmsManager sms = SmsManager.getDefault();
      if (sms == null)
        Log.e(TAG, "SMS Manager is null! Not sending the message");
      else {
        Log.d(TAG, "SMS Manager is not null! Sending the message");
        final ArrayList<String> parts = sms.divideMessage(messageToSend);
        sms.sendMultipartTextMessage(number, null, parts, null, null);
        speakOut(getString(R.string.SOS_message_has_been_sent));
      }
      mHandler.postDelayed(mLaunchTask, 1500);
    }
  };
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.Send_SOS_Message:
        speakOut(getString(R.string.sending_SOS_message));
        mHandler.postDelayed(sendSOSMessage, 5000);
        break;
      default:
        break;
    }
    return false;
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sos);
    init(0, getString(R.string.sos_screen), getString(R.string.sos_help));
    l = new ReentrantLock();
  }
  
  void makeUseOfNewLocation(double lon, double lat, String provider, String addr) {
    f.stop();
    l.lock();
    address = addr;
    lastProvider = provider;
    latitude = lat;
    longitude = lon;
    l.unlock();
  }
  
  @Override protected void onStart() {
    super.onStart();
    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    f = new LocationFinder(manager);
    final LocationHandler h = new LocationHandler() {
      @Override public void handleLocation(double lon, double lat, String provider, String addr) {
        makeUseOfNewLocation(lon, lat, provider, addr);
      }
    };
    f.run(h);
  }
  
  @Override protected void onStop() {
    f.stop();
    super.onStop();
  }
}
