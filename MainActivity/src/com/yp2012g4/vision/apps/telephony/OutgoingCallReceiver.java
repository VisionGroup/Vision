package com.yp2012g4.vision.apps.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.yp2012g4.vision.apps.telephony.CallUtils.CALL_TYPE;

public class OutgoingCallReceiver extends BroadcastReceiver {
  private static final String TAG = "vision:OutGoingCallReceiver";
  public static final String ABORT_PHONE_NUMBER = "1231231234";
  private static final String OUTGOING_CALL_ACTION = "android.intent.action.NEW_OUTGOING_CALL";
  private static final String INTENT_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
  
  @Override public void onReceive(final Context c, final Intent i) {
    Log.v(TAG, "OutgoingCallReceiver onReceive");
    if (i.getAction().equals(OutgoingCallReceiver.OUTGOING_CALL_ACTION)) {
      Log.v(TAG, "OutgoingCallReceiver NEW_OUTGOING_CALL received");
      // get phone number from bundle
      final Bundle b = i.getExtras();
      if (null == b)
        return;
      final String phoneNumber = b.getString(OutgoingCallReceiver.INTENT_PHONE_NUMBER);
      Log.v(TAG, "Incoming phonenumber: " + phoneNumber);
      final Message m = CallUtils.newMessage(phoneNumber, CALL_TYPE.INCOMING_CALL);
      try {
        CallService.callScreenServiceManager.send(m);
      } catch (final RemoteException e) {
        Log.d(TAG, "Unable to send message to callScreenService.", e);
      }
    }
  }
}
