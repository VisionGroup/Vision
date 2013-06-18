package com.yp2012g4.vision.apps.telephony;

import android.content.Context;
import android.util.Log;

import com.yp2012g4.vision.apps.telephony.CallUtils.CALL_TYPE;
import com.yp2012g4.vision.tools.ServiceManager;
import com.yp2012g4.vision.tools.ThrowableToString;

public class CallService {
  public static ServiceManager callScreenServiceManager;
  private static String TAG = "vision:CallService";
  
  public static void initialise(final Context c) {
    /* Call service manager initialization. */
    if (callScreenServiceManager == null)
      callScreenServiceManager = new ServiceManager(c, CallScreenService.class, null);
    if (!callScreenServiceManager.isRunning())
      callScreenServiceManager.start();
    Log.d(TAG, "Initialised.");
  }
  
  public static void sendMessage(final Context c, final String number, final CALL_TYPE ct) {
    if (callScreenServiceManager == null || !callScreenServiceManager.isRunning())
      initialise(c);
    try {
      callScreenServiceManager.send(CallUtils.newMessage(number, ct));
    } catch (final Exception e) {
      Log.d(TAG, "Unable to send message to callScreenService " + ThrowableToString.toString(e));
    }
  }
}
