package com.yp2012g4.vision.apps.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yp2012g4.vision.apps.telephony.CallUtils.CALL_TYPE;

/**
 * This receiver will activate the IncomingCallActivity when an incoming call
 * has been detected.
 * 
 * @author Amit Yaffe
 * @version 1.1
 * 
 */
public class IncomingCallReceiver extends BroadcastReceiver {
  final static String TAG = "vision:IncomingCallReceiver";
  private static boolean _rang = false;
  
  @Override public void onReceive(final Context c, final Intent i) {
    final Bundle b1 = i.getExtras();
    if (null == b1)
      return;
    final String state = b1.getString(TelephonyManager.EXTRA_STATE);
    Log.d(TAG, "State: " + state);
    if (state != null && state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
      _rang = true;
      try {
        final String phonenumber = b1.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        Log.i(TAG, "Incoming call from:" + phonenumber);
        CallService.sendMessage(c, phonenumber, CALL_TYPE.INCOMING_CALL);
      } catch (final Exception e) {
        Log.d(TAG, "Failed to load incoming call screen.");
      }
      return;
    }
    if (state != null && state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE) && _rang) {
      _rang = false;
      Log.i(TAG, "Call ended.");
      try {
        CallService.sendMessage(c, "", CALL_TYPE.CALL_ENDED);
      } catch (final Exception e) {
        Log.d(TAG, "Failed to execute end call code.");
      }
      return;
    }
    if (state != null && state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK))
      _rang = true;
  }
}