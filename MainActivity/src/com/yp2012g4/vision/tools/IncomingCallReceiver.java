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
  
  @Override public void onReceive(final Context c, final Intent i) {
    final Bundle bundle = i.getExtras();
    if (null == bundle)
      return;
    Log.d(TAG, bundle.toString());
    final String state = bundle.getString(TelephonyManager.EXTRA_STATE);
    Log.d(TAG, "State: " + state);
    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
      final String phonenumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
      Log.i(TAG, "Incoming call from:" + phonenumber);
      processIncomingCall(c, phonenumber);
      return;
    }
    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
      final String phonenumber = "";// bundle.getString(CallUtils.NUMBER_KEY);
      Log.i(TAG, "Call to:" + phonenumber);
      processIncomingCall(c, phonenumber);
      return;
    }
  }
  
  @SuppressWarnings({ "static-method" }) private void processIncomingCall(final Context context, final String phonenumber) {
    try {
      Log.d(TAG, "Creating IncomingCAllActivity intent");
      final Intent i = new Intent(context, IncomingCallActivity.class);
      i.putExtra(CallUtils.RANG_KEY, true);
      i.putExtra(CallUtils.NUMBER_KEY, phonenumber);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Log.d(TAG, "Starting IncomingCAllActivity");
      Thread.sleep(1000);
      context.startActivity(i);
    } catch (final Exception e) {
      Log.e(TAG, "error starting IncomingCAllActivity", e);
    }
  }
}