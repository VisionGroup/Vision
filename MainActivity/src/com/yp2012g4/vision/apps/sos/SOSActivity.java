package com.yp2012g4.vision.apps.SOS;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;
import com.yp2012g4.vision.tools.location.LocationFinder;
import com.yp2012g4.vision.tools.location.LocationHandler;

/**
 * This class is an activity which sends a pre-defined SOS message to
 * configurable content
 * 
 * @author Amir
 * @version 1.0
 */
public class SOSActivity extends VisionActivity {
  LocationFinder lf;
  Lock l = null;
  String lastProvider = "", address = "";
  static final int DEFAULT_LAT_LONG = 10000;
  static final int SEND_DELAY = 5000;
  double latitude = DEFAULT_LAT_LONG, longitude = DEFAULT_LAT_LONG;
  final int maxLengthOfAddress = 100;
  static final String TAG = "vision:SOSActivity";
  String _number = "";
  
  @Override public int getViewId() {
    return R.id.SOS_textview;
  }
  
  /**
   * Send an SOS message
   */
  public Runnable sendSOSMessage = new Runnable() {
    @Override public void run() {
      String messageToSend = getString(R.string.sos_msg_to_send);
      l.lock();
      if (latitude != DEFAULT_LAT_LONG) {
        messageToSend += "\nMy location is: latitude = " + latitude + "; longitude = " + longitude + ";\nMy address is ";
        if (address.length() > maxLengthOfAddress)
          messageToSend += address.substring(0, maxLengthOfAddress);
        else
          messageToSend += address;
      }
      l.unlock();
      Log.d(TAG, "Sending SOS: " + messageToSend + " to:" + _number + " lat=" + latitude + " long=" + longitude);
      final SmsManager sms = SmsManager.getDefault();
      if (sms == null)
        Log.e(TAG, "SMS Manager is null! Not sending the message");
      else {
        Log.d(TAG, "SMS Manager is not null! Sending the message");
        final ArrayList<String> parts = sms.divideMessage(messageToSend);
        sms.sendMultipartTextMessage(_number, null, parts, null, null);
        speakOutSync(getString(R.string.SOS_message_has_been_sent));
      }
      finish();
    }
  };
  
  @Override public boolean onSingleTapUp(final MotionEvent me) {
    if (super.onSingleTapUp(me))
      return true;
    switch (getButtonByMode().getId()) {
      case R.id.Send_SOS_Message:
        speakOutSync(getString(R.string.sending_SOS_message) + "to " + _number);
        _mHandler.postDelayed(sendSOSMessage, 5000);
        break;
      case R.id.SOS_Change_contact:
        final Intent i = new Intent(this, SOSconfig.class);
        startActivity(i);
        break;
      default:
        break;
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
    lf.stop();
    l.lock();
    address = s2;
    lastProvider = s1;
    latitude = d2;
    longitude = d1;
    l.unlock();
  }
  
  @Override protected void onStart() {
    super.onStart();
    lf = new LocationFinder((LocationManager) getSystemService(Context.LOCATION_SERVICE));
    final LocationHandler h = new LocationHandler() {
      @Override public void handleLocation(final double d1, final double d2, final String s1, final String s2) {
        makeUseOfNewLocation(d1, d2, s1, s2);
      }
    };
    lf.run(h);
    loadNumber();
  }
  
  private void loadNumber() {
    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    _number = sp.getString(getString(R.string.sos_number), _number);
    if (_number == "") {
      speakOutSync(getString(R.string.SOS_number_empty));
      final Intent i = new Intent(this, SOSconfig.class);
      startActivity(i);
    }
    final TalkingButton tb = (TalkingButton) findViewById(R.id.SOS_phone_number);
    tb.setText(_number);
    tb.setReadText(getString(R.string.sos_contact_number) + _number);
  }
  
  @Override protected void onStop() {
    lf.stop();
    super.onStop();
  }
}
