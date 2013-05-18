package com.yp2012g4.vision.tools;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;
import com.yp2012g4.vision.apps.settings.VisionApplication;

/**
 * This util class is used to answer, reject (and later call) calls and more
 * call related features.
 * 
 * 
 * @author Amit Yaffe
 * @version 1.1
 * 
 */
public class CallUtils {
  // Used by Log
  private static String TAG = "vison:CallUtils";
  // Used by the Incoming call receiver to transfer data to the activity.
  public static final String RANG_KEY = "rang";
  public static final String NUMBER_KEY = TelephonyManager.EXTRA_INCOMING_NUMBER;
  private com.android.internal.telephony.ITelephony telephonyService;
  private AudioManager _am;
  
  /**
   * Enables the Speakerphone
   * 
   * @param context
   */
  public static void enableSpeakerPhone(final Context c) {
    final AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
    audioManager.setSpeakerphoneOn(true);
  }
  
  public CallUtils(final Context c) {
    try {
      final TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
      final Class<?> cls = Class.forName(tm.getClass().getName());
      final Method m = cls.getDeclaredMethod("getITelephony");
      m.setAccessible(true);
      telephonyService = (ITelephony) m.invoke(tm);
      _am = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
    } catch (final Exception e) {
      Log.e(TAG, "Error starting CallUtils", e);
    }
  }
  
  /**
   * Answer phone call. Simulate a press of the headset button to pick up the
   * call
   * 
   * @param context
   */
  public static void answerCall(final Context c) {
    final String recPerm = "android.permission.CALL_PRIVILEGED";
    final Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
    buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
    c.sendOrderedBroadcast(buttonDown, recPerm);
    final Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
    buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
    c.sendOrderedBroadcast(buttonUp, recPerm);
  }
  
  /**
   * Terminate a phone call
   */
  public void endCall() {
    try {
      telephonyService.endCall();
      Thread.sleep(VisionApplication.DEFUALT_DELAY_TIME);
    } catch (final Exception e) {
      e.printStackTrace();
      Log.e(TAG, "FATAL ERROR: could not connect to telephony subsystem");
      Log.e(TAG, "Exception object: " + e);
    }
  }
  
  /**
   * Silence phone ringer.
   */
  public void silenceRinger() {
    try {
      _am.setStreamMute(AudioManager.STREAM_RING, true);
    } catch (final Exception e) {
      Log.e(TAG, "Error silencing Ringer", e);
    }
  }
  
  /**
   * Restore phone ringer.
   */
  public void restoreRinger() {
    try {
      _am.setStreamMute(AudioManager.STREAM_RING, false);
    } catch (final Exception e) {
      Log.e(TAG, "Error silencing Ringer", e);
    }
  }
}
