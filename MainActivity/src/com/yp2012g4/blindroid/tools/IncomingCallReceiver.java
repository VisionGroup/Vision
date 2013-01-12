package com.yp2012g4.blindroid.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yp2012g4.blindroid.telephony.IncomingCallActivity;

/**
 * This receiver will activate the IncomingCallActivity when an incoming call
 * has been detected.
 * 
 * @author Amit Yaffe
 * @version 1.1
 * 
 */
public class IncomingCallReceiver extends BroadcastReceiver {
  // private ITelephony telephonyService;
  // private static boolean rang = false;
  /*
   * private ArrayList<String> blackList = new ArrayList<String>(); private
   * DBAdapter dbHelper; private Context ct; private boolean reject = false;
   */
  private final static String TAG = "bd:IncomingCallReceiver";
  
  @Override public void onReceive(Context context, Intent intent) {
    final Bundle bundle = intent.getExtras();
    if (null == bundle)
      return;
    Log.i("IncomingCallReceiver", bundle.toString());
    final String state = bundle.getString(TelephonyManager.EXTRA_STATE);
    Log.i("IncomingCallReceiver", "State: " + state);
    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
      final String phonenumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
      Log.i(TAG, "Incoming call from:" + phonenumber);
      // rang = true;
      processIncomingCall(context);
    }
  }
  
  @SuppressWarnings("static-method") private void processIncomingCall(Context context) {
    // final TelephonyManager telMan = (TelephonyManager)
    // context.getSystemService(Context.TELEPHONY_SERVICE);
    try {
//      final Class classTemp = Class.forName(telMan.getClass().getName());
//      final Method methodTemp = classTemp.getDeclaredMethod("getITelephony");
//      methodTemp.setAccessible(true);
//      telephonyService = (ITelephony) methodTemp.invoke(telMan);
//      final Bundle bundle = intent.getExtras();
//      final String phoneNumber = bundle.getString("incoming_number");
//      Log.v(TAG, "Incomig call " + phoneNumber);
//      telephonyService.endCall();
      Log.d(TAG, "Creatin IncomingCAllActivity intent");
      final Intent i = new Intent(context, IncomingCallActivity.class);
      i.putExtra("rang", true);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Log.d(TAG, "Starting IncomingCAllActivity");
      context.startActivity(i);
    } catch (final Exception e) {
      // e.printStackTrace();
      Log.e(TAG, "error starting IncomingCAllActivity", e);
    }
  }
}