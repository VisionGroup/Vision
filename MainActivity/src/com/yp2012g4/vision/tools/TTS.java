package com.yp2012g4.vision.tools;

import java.util.Locale;
import java.util.regex.Pattern;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import com.yp2012g4.vision.apps.settings.VisionApplication;

/**
 * This class is a wrapper class for the TTS engine.
 * 
 * @author Auster Yaron
 * 
 * @author Olivier Modified the class so that it is a singleton
 * 
 */
public class TTS implements OnInitListener {
  private static final String TAG = "vision:TTS";
  public TextToSpeech _tts;
  private int _qm;
  private Locale _language;
  private boolean _init = false;
  private static TTS staticTTS = null;
  private static String _engine = "";
  
  public static void init(final Context c) {
    if (staticTTS == null || !TTS.isRunning() || _engine != staticTTS._tts.getDefaultEngine()) {
      staticTTS = new TTS(c);
      _engine = staticTTS._tts.getDefaultEngine();
      Log.v(TAG, "TTS initialized");
    } else
      Log.v(TAG, "TTS already running so not initialized");
  }
  
  /**
   * 
   * 
   * @param c
   *          the context that is running the class.
   * @param listener
   *          the listener that is connected from the activity.
   */
  private TTS(final Context c) {
    _tts = new TextToSpeech(c, this);
  }
  
  /**
   * @return true if the TTS engine is initialized.
   */
  public static boolean isRunning() {
    return staticTTS._init;
  }
  
  /**
   * change the queue mode.
   * 
   * @param queueMode
   *          the new queue mode.
   */
  public static void setQueueMode(final int queueMode) {
    Log.d(TAG, "setQueueMode");
    staticTTS._qm = queueMode;
  }
  
  /**
   * change the language.
   * 
   * @param languag
   *          the new language.
   * @return
   */
  public static int setLanguage(final Locale l) {
    Log.d(TAG, "setLanguage");
    staticTTS._language = l;
    return staticTTS._tts.setLanguage(l);
  }
  
  /**
   * 
   * @return The language being set.
   */
  public static Locale getLanguage() {
    Log.d(TAG, "getLanguage");
    return staticTTS._language;
  }
  
  /**
   * speak the given string in default queueMode
   * 
   * @param s
   *          string to speak.
   */
  public static void speak(final String s) {
    speak(s, staticTTS._qm);
  }
  
  /**
   * speak the given string.
   * 
   * @param s
   *          string to speak.
   * @param queueMode
   *          The queueMode to use
   */
  public static void speak(final String s, final int queueMode) {
    if (VisionApplication.muted) {
      Log.d(TAG, "not speaking because application is muted");
      return;
    }
    Log.d(TAG, "speak : " + s);
    if (null != s)
      staticTTS._tts.speak(s, queueMode, null);
    final boolean debug = false;
    if (debug) {
      Log.d(TAG, "caller function : " + Thread.currentThread().getStackTrace()[3].getClassName() + ":"
          + Thread.currentThread().getStackTrace()[3].getMethodName());
      Log.d(TAG, "caller function 2: " + Thread.currentThread().getStackTrace()[4].getClassName() + ":"
          + Thread.currentThread().getStackTrace()[4].getMethodName());
    }
  }
  
  /**
   * speak the given string, in synchronous mode.
   * 
   * @param s
   *          string to speak.
   */
  public static void syncSpeak(final String s) {
    speak(s);
    while (isSpeaking())
      try {
        Thread.sleep(VisionApplication.DEFUALT_DELAY_TIME);
      } catch (final Exception e) {
        e.printStackTrace();
      }
  }
  
  /**
   * Stop speak!.
   * 
   */
  public static void stop() {
    Log.d(TAG, "stop");
    staticTTS._tts.stop();
  }
  
  /**
   * Close the TTS engine.
   * 
   */
  public static void shutdown() {
    stop();
    staticTTS._tts.shutdown();
    staticTTS._tts = null;
  }
  
  /**
   * 
   * @return true if currently speaking, false otherwise.
   */
  public static boolean isSpeaking() {
    return staticTTS._tts.isSpeaking();
  }
  
  /**
   * Check if the string contains pure English.
   * 
   * @param s
   *          string to check.
   * @return true if only English letters exist in the string.
   */
  public static boolean isPureEnglish(final String s) {
    if (s == null)
      return true;
    return !Pattern.compile("[\\p{InHebrew}]").matcher(s).find();
  }
  
  @Override public void onInit(final int status) {
    if (status == TextToSpeech.SUCCESS) {
      _init = true;
      Log.d(TAG, "TTS init completed succesfully.");
      setQueueMode(TextToSpeech.QUEUE_FLUSH);
      setLanguage(Locale.US);
    }
  }
  
  public static void waitUntilFinishTalking() {
    while (isSpeaking()) {
      // Wait for message to finish playing and then continue
    }
  }
  
  /**
   * Checks whether or not the language is supported by the current TTS engine.
   * 
   * @param l
   * @return
   */
  public static boolean isLanguageAvailable(final Locale l) {
    final int res = staticTTS._tts.isLanguageAvailable(l);
    return res != TextToSpeech.LANG_MISSING_DATA && res != TextToSpeech.LANG_NOT_SUPPORTED;
  }
}
