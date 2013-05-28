package com.yp2012g4.vision.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yp2012g4.vision.apps.main.MainActivity;
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
  
  // private static ServiceManager callScreenServiceManager;
  @Override public void onReceive(final Context c, final Intent i) {
    final Bundle b1 = i.getExtras();
    if (null == b1)
      return;
    final String state = b1.getString(TelephonyManager.EXTRA_STATE);
    Log.d(TAG, "State: " + state);
    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
      final String phonenumber = b1.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
      Log.i(TAG, "Incoming call from:" + phonenumber);
      final Message m = new Message();
      final Bundle b2 = new Bundle();
      b2.putString(CallUtils.NUMBER_KEY, phonenumber);
      b2.putInt(CallUtils.CALL_TYPE_KEY, CALL_TYPE.INCOMING_CALL.ordinal());
      m.setData(b2);
      try {
        MainActivity.callScreenServiceManager.send(m);
      } catch (final RemoteException e) {
        Log.d(TAG, "Unable to send message to callScreenService.", e);
      }
    }
  }
}