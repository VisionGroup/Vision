package com.yp2012g4.vision.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yp2012g4.vision.apps.settings.VisionApplication;
import com.yp2012g4.vision.apps.telephony.IncomingCallActivity;

public class OutgoingCallReceiver extends BroadcastReceiver {
  private static final String TAG = "vision:OutGoingCallReceiver";
  public static final String ABORT_PHONE_NUMBER = "1231231234";
  private static final String OUTGOING_CALL_ACTION = "android.intent.action.NEW_OUTGOING_CALL";
  private static final String INTENT_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
  
//  @Override public void onReceive(final Context context, final Intent intent) {
//    final String action = intent.getAction();
//    Log.d(TAG, "onReceive, Action:" + intent.getAction());
//  }
  @Override public void onReceive(final Context c, final Intent i) {
    Log.v(TAG, "OutgoingCallReceiver onReceive");
    if (i.getAction().equals(OutgoingCallReceiver.OUTGOING_CALL_ACTION)) {
      // abortBroadcast();
      Log.v(TAG, "OutgoingCallReceiver NEW_OUTGOING_CALL received");
      // get phone number from bundle
      final Bundle b = i.getExtras();
      if (null == b)
        return;
      final String phoneNumber = b.getString(OutgoingCallReceiver.INTENT_PHONE_NUMBER);
      Log.v(TAG, "Incoming phonenumber: " + phoneNumber);
      // c.startService(new Intent(c, CallScreenService.class));// TODO:
      // REMoVE!!!
      processOutgoingCall(c, phoneNumber);
      // if ((phoneNumber != null) &&
      // phoneNumber.equals(OutgoingCallReceiver.ABORT_PHONE_NUMBER)) {
//          Toast.makeText(context, "NEW_OUTGOING_CALL intercepted to number 123-123-1234 - aborting call",
//                   Toast.LENGTH_LONG).show();
//          this.abortBroadcast();
//       }
    }
  }
  
  @SuppressWarnings({ "static-method" }) private void processOutgoingCall(final Context context, final String phonenumber) {
    try {
      Log.d(TAG, "Creating OutgoingCAllActivity intent");
      final Intent i = new Intent(context, IncomingCallActivity.class);
      i.putExtra(CallUtils.RANG_KEY, true);
      i.putExtra(CallUtils.NUMBER_KEY, phonenumber);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Log.d(TAG, "Starting OutgoingCAllActivity");
      Thread.sleep(VisionApplication.DEFUALT_DELAY_TIME);
      context.startActivity(i);
    } catch (final Exception e) {
      Log.e(TAG, "error starting OutgoingCAllActivity", e);
    }
  }
}
