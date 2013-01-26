package com.yp2012g4.vision.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yp2012g4.vision.telephony.IncomingCallActivity;

/**
 * This receiver will activate the IncomingCallActivity when an incoming call
 * has been detected.
 * 
 * @author Amit Yaffe
 * @version 1.1
 * 
 */
public class IncomingCallReceiver extends BroadcastReceiver {
  private final static String TAG = "vision:IncomingCallReceiver";
  
  @Override public void onReceive(Context context, Intent intent) {
    final Bundle bundle = intent.getExtras();
    if (null == bundle)
      return;
    Log.d(TAG, bundle.toString());
    final String state = bundle.getString(TelephonyManager.EXTRA_STATE);
    Log.d(TAG, "State: " + state);
    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
      final String phonenumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
      Log.i(TAG, "Incoming call from:" + phonenumber);
      processIncomingCall(context, phonenumber);
    }
  }
  
  @SuppressWarnings({ "static-method" }) private void processIncomingCall(Context context, String phonenumber) {
    try {
      Log.d(TAG, "Creatin IncomingCAllActivity intent");
      final Intent i = new Intent(context, IncomingCallActivity.class);
      i.putExtra(CallUtils.RANG_KEY, true);
      i.putExtra(CallUtils.INCOING_NUMBER_KEY, phonenumber);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Log.d(TAG, "Starting IncomingCAllActivity");
      Thread.sleep(1000);
      context.startActivity(i);
    } catch (final Exception e) {
      // e.printStackTrace();
      Log.e(TAG, "error starting IncomingCAllActivity", e);
    }
  }
}