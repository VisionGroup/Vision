package com.yp2012g4.vision.apps.telephony;

import android.content.Context;
import android.util.Log;

import com.yp2012g4.vision.tools.ServiceManager;

public class CallService {
  public static ServiceManager callScreenServiceManager;
  private static String TAG = "vision:CallService";
  
  public static void initialise(final Context c) {
    /* Call service manager initialisation. */
    if (callScreenServiceManager == null)
      callScreenServiceManager = new ServiceManager(c, CallScreenService.class, null);
    if (!callScreenServiceManager.isRunning())
      callScreenServiceManager.start();
    Log.d(TAG, "Initialised.");
  }
}
