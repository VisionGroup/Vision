package com.yp2012g4.vision.apps.telephony;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.yp2012g4.vision.apps.telephony.CallUtils.CALL_TYPE;
import com.yp2012g4.vision.tools.ServiceManager;

public class CallService {
  public static ServiceManager callScreenServiceManager;
  private static String TAG = "vision:CallService";
  
  // private static boolean _initStarted = false;
  public static void initialise(final Context c) {
    /* Call service manager initialization. */
    if (callScreenServiceManager == null)
      callScreenServiceManager = new ServiceManager(c, CallScreenService.class, null);
    if (!callScreenServiceManager.isRunning())
      callScreenServiceManager.start();
    // _initStarted = true;
    Log.d(TAG, "Initialised.");
  }
  
  public static void sendMessage(final Context c, final String number, final CALL_TYPE ct) {
    final Message m = CallUtils.newMessage(number, ct);
    if (callScreenServiceManager == null)
      initialise(c);
    try {
      callScreenServiceManager.send(m);
    } catch (final Exception e) {
      Log.d(TAG, "Unable to send message to callScreenService.", e);
    }
  }
}
