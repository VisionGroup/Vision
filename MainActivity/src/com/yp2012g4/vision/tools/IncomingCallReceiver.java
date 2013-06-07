package com.yp2012g4.vision.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yp2012g4.vision.tools.CallUtils.CALL_TYPE;

/**
 * This receiver will activate the IncomingCallActivity when an incoming call
 * has been detected.
 * 
 * @author Amit Yaffe
 * @version 1.1
 * 
 */
//TODO: Spartanize
public class IncomingCallReceiver extends BroadcastReceiver {
  private final static String TAG = "vision:IncomingCallReceiver";
  private static boolean _rang = false;
  
  @Override public void onReceive(final Context c, final Intent i) {
    final Bundle b1 = i.getExtras();
    if (null == b1)
      return;
    final String state = b1.getString(TelephonyManager.EXTRA_STATE);
    Log.d(TAG, "State: " + state);
    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
      _rang = true;
      final String phonenumber = b1.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
      Log.i(TAG, "Incoming call from:" + phonenumber);
      _sendMessage(c, phonenumber, CALL_TYPE.INCOMING_CALL);
    }
    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE) && _rang) {
      _rang = false;
      // final String phonenumber =
      // b1.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
      Log.i(TAG, "Call ended.");
      _sendMessage(c, "", CALL_TYPE.CALL_ENDED);
    }
    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK))
      _rang = true;
  }
  
  private static void _sendMessage(final Context c, final String number, final CALL_TYPE ct) {
    final Message m = new Message();
    final Bundle b2 = new Bundle();
    b2.putString(CallUtils.NUMBER_KEY, number);
    b2.putInt(CallUtils.CALL_TYPE_KEY, ct.ordinal());
    m.setData(b2);
    try {
      if (CallService.callScreenServiceManager == null)
        CallService.initialise(c);
      CallService.callScreenServiceManager.send(m);
    } catch (final RemoteException e) {
      Log.d(TAG, "Unable to send message to callScreenService.", e);
    }
  }
}