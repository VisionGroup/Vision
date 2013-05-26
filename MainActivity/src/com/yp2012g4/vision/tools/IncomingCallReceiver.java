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
import com.yp2012g4.vision.apps.settings.VisionApplication;
import com.yp2012g4.vision.apps.telephony.IncomingCallActivity;
import com.yp2012g4.vision.tools.CallUtils.CALL_TYPE;

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
  
  // private static ServiceManager callScreenServiceManager;
  @Override public void onReceive(final Context c, final Intent i) {
    final Bundle bundle = i.getExtras();
    if (null == bundle)
      return;
    // callScreenServiceManager = new ServiceManager(c.getApplicationContext(),
    // CallScreenService.class, null);
    Log.d(TAG, bundle.toString());
    final String state = bundle.getString(TelephonyManager.EXTRA_STATE);
    Log.d(TAG, "State: " + state);
    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
      final String phonenumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
      Log.i(TAG, "Incoming call from:" + phonenumber);
      final Message m = new Message();
      final Bundle b = new Bundle();
      b.putString(CallUtils.NUMBER_KEY, phonenumber);
      b.putInt(CallUtils.CALL_TYPE_KEY, CALL_TYPE.INCOMING_CALL.ordinal());
      m.setData(b);
      try {
        MainActivity.callScreenServiceManager.send(m);
      } catch (final RemoteException e) {
        Log.d(TAG, "Unable to send message to callScreenService.", e);
      }
      // processIncomingCall(c, phonenumber);
      return;
    }
    if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
      final String phonenumber = "";// bundle.getString(CallUtils.NUMBER_KEY);
      Log.i(TAG, "Call to:" + phonenumber);
      // processIncomingCall(c, phonenumber); //TODO: Checking.
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
      Thread.sleep(VisionApplication.DEFUALT_DELAY_TIME);
      context.startActivity(i);
    } catch (final Exception e) {
      Log.e(TAG, "error starting IncomingCAllActivity", e);
    }
  }
}