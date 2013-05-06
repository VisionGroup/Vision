package com.yp2012g4.vision.tools;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

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
  public static final String INCOING_NUMBER_KEY = "iNumber";
  private com.android.internal.telephony.ITelephony telephonyService;
  AudioManager audioManager;
  
  // ListenToPhoneState listener;
  /**
   * Enables the use of speakerphone
   * 
   * @param context
   */
  public static void enableSpeakerPhone(Context context) {
    final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    audioManager.setSpeakerphoneOn(true);
  }
  
  /**
   * Use answerPhoneHeadsethook instead.
   * 
   * @throws Exception
   */
  @Deprecated public void answerCall() throws Exception {
    // Silence the ringer and answer the call!
    try {
      telephonyService.silenceRinger();
      telephonyService.answerRingingCall();
    } catch (final Exception e) {
      Log.e(TAG, "Error answerig calls", e);
    }
  }
  
  public CallUtils(Context context) {
    super();
    try {
      final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      final Class<?> c = Class.forName(tm.getClass().getName());
      final Method m = c.getDeclaredMethod("getITelephony");
      m.setAccessible(true);
      telephonyService = (ITelephony) m.invoke(tm);
      audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    } catch (final Exception e) {
      Log.e(TAG, "Error starting CallUtils", e);
    }
  }
  
  /**
   * Answer phone call.
   * 
   * @param context
   */
  public static void answerPhoneHeadsethook(Context context) {
    // Simulate a press of the headset button to pick up the call
    // SettingsClass.logMe(tag, "Simulating headset button");
    final Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
    buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
    context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");
    // froyo and beyond trigger on buttonUp instead of buttonDown
    final Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
    buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
    context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
  }
  
  /**
   * Terminate a phone call
   */
  public void endCall() {
    try {
      // Java reflection to gain access to TelephonyManager's
      // ITelephony getter
      telephonyService.endCall();
      Thread.sleep(1000);
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
      audioManager.setStreamMute(AudioManager.STREAM_RING, true);
    } catch (final Exception e) {
      Log.e(TAG, "Error silencing Ringer", e);
    }
  }
  
  /**
   * Restore phone ringer.
   */
  public void restoreRinger() {
    try {
      audioManager.setStreamMute(AudioManager.STREAM_RING, false);
    } catch (final Exception e) {
      Log.e(TAG, "Error silencing Ringer", e);
    }
  }
}
