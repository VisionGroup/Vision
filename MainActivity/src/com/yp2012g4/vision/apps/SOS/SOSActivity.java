package com.yp2012g4.vision.apps.SOS;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.settings.VisionApplication;
import com.yp2012g4.vision.tools.VisionActivity;
import com.yp2012g4.vision.tools.location.LocationFinder;
import com.yp2012g4.vision.tools.location.LocationHandler;


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
  static final int DEFAULT_LAT_LONG = 10000;
  static final int SEND_DELAY = 5000;
  double latitude = DEFAULT_LAT_LONG, longitude = DEFAULT_LAT_LONG;
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
      if (latitude != DEFAULT_LAT_LONG) {
        messageToSend += "\nMy location is: latitude = " + latitude + "; longitude = " + longitude + ";\nMy address is ";
        if (address.length() > maxLengthOfAddress)
          messageToSend += address.substring(0, maxLengthOfAddress);
        else
          messageToSend += address;
      }
      l.unlock();
      final String number = "0529240424";
      Log.d(TAG, "Sending SOS : " + messageToSend);
      Log.d(TAG, "number : " + number);
      Log.d(TAG, "latitude =  " + latitude);
      Log.d(TAG, "longitude =  " + longitude);
      final SmsManager sms = SmsManager.getDefault();
      if (sms == null)
        Log.e(TAG, "SMS Manager is null! Not sending the message");
      else {
        Log.d(TAG, "SMS Manager is not null! Sending the message");
        speakOutAsync(getString(R.string.SOS_message_has_been_sent));
      }
      _mHandler.postDelayed(mLaunchTask, VisionApplication.DEFUALT_DELAY_TIME);
    }
  };
  
  @Override public boolean onSingleTapUp(final MotionEvent me) {
    if (super.onSingleTapUp(me))
      return true;
    if (getButtonByMode().getId() == R.id.Send_SOS_Message) {
      speakOutAsync(getString(R.string.sending_SOS_message));
      _mHandler.postDelayed(sendSOSMessage, SEND_DELAY);
    }
    return false;
  }
  
  @Override protected void onCreate(final Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_sos);
    init(0, getString(R.string.sos_screen), getString(R.string.sos_help));
    l = new ReentrantLock();
  }
  
  /**
   * Updates the location according to new info.
   * 
   * @param d1
   *          longitude
   * @param d2
   *          latitude
   * @param s1
   *          the information's provider
   * @param s1
   *          the address
   */
  void makeUseOfNewLocation(final double d1, final double d2, final String s1, final String s2) {
    f.stop();
    l.lock();
    address = s2;
    lastProvider = s1;
    latitude = d2;
    longitude = d1;
    l.unlock();
  }
  
  @Override protected void onStart() {
    super.onStart();
    f = new LocationFinder((LocationManager) getSystemService(Context.LOCATION_SERVICE));
    final LocationHandler h = new LocationHandler() {
      @Override public void handleLocation(final double d1, final double d2, final String s1, final String s2) {
        makeUseOfNewLocation(d1, d2, s1, s2);
      }
    };
    f.run(h);
  }
  
  @Override protected void onStop() {
    f.stop();
    super.onStop();
  }
}
