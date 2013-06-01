package com.yp2012g4.vision.apps.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.yp2012g4.vision.apps.settings.VisionApplication;
import com.yp2012g4.vision.tools.AbstractService;
import com.yp2012g4.vision.tools.CallUtils;

public class CallScreenService extends AbstractService {
  private static final String TAG = "vision:CallScreenService";
  
  /**
   * Receives a message containing bundle with a phone number (using the
   * CallUtils.NUMBER_KEY) and a call type (using CallUtils.CALL_TYPE_KEY)
   * Starts the call activity.
   */
  @Override public void onReceiveMessage(final Message m) {
    Log.d(TAG, "On receive message");
    if (m == null)
      return;
    try {
      final Bundle b = m.getData();
      final String phoneNumber = b.getString(CallUtils.NUMBER_KEY);
      Log.d(TAG, phoneNumber);
      processCall(this, phoneNumber);
    } catch (final Exception e) {
      Log.e(TAG, "Error parsing message.", e);
    }
  }
  
  private static void processCall(final Context c, final String phoneNumber) {
    try {
      Log.d(TAG, "Creating CAllActivity intent");
      final Intent i = new Intent(c, IncomingCallActivity.class);
      i.putExtra(CallUtils.RANG_KEY, true);
      i.putExtra(CallUtils.NUMBER_KEY, phoneNumber);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Log.d(TAG, "Starting CAllActivity");
      Thread.sleep(VisionApplication.DEFUALT_DELAY_TIME);
      c.startActivity(i);
    } catch (final Exception e) {
      Log.e(TAG, "error starting CAllActivity", e);
    }
  }
  
  @Override public void onStartService() {
    Log.d(TAG, "Service Started.");
  }
  
  @Override public void onStopService() {
    Log.d(TAG, "Service Stopped.");
  }
}