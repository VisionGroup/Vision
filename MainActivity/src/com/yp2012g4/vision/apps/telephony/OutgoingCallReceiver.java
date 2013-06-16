package com.yp2012g4.vision.apps.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yp2012g4.vision.apps.telephony.CallUtils.CALL_TYPE;

public class OutgoingCallReceiver extends BroadcastReceiver {
  private static final String TAG = "vision:OutGoingCallReceiver";
  public static final String ABORT_PHONE_NUMBER = "1231231234";
  private static final String OUTGOING_CALL_ACTION = "android.intent.action.NEW_OUTGOING_CALL";
  public static final String INTENT_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
  
  @Override public void onReceive(final Context c, final Intent i) {
    Log.v(TAG, "OutgoingCallReceiver onReceive");
    try {
      if (i.getAction().equals(OutgoingCallReceiver.OUTGOING_CALL_ACTION)) {
        Log.v(TAG, "OutgoingCallReceiver NEW_OUTGOING_CALL received");
        // get phone number from bundle
        final Bundle b = i.getExtras();
        if (null == b)
          return;
        final String phoneNumber = b.getString(OutgoingCallReceiver.INTENT_PHONE_NUMBER);
        Log.v(TAG, "Incoming phonenumber: " + phoneNumber);
        try {
          CallService.sendMessage(c, phoneNumber, CALL_TYPE.OUTGOING_CALL);
        } catch (final Exception e) {
          Log.d(TAG, "Failed to execute outgoing call code.");
        }
      }
    } catch (final Exception e) {
      Log.d(TAG, "Failed outgoing call receiver.", e);
    }
  }
}
