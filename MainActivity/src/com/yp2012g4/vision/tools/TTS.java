package com.yp2012g4.vision.tools;

import java.util.Locale;
import java.util.regex.Pattern;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import com.yp2012g4.vision.VisionApplication;
import com.yp2012g4.vision.apps.settings.Language;

/**
 * This class is a wrapper class for the TTS engine.
 * 
 * @author Auster Yaron
 * 
 * @author Olivier (Modified the class so that it is a singleton)
 * 
 */
public class TTS implements OnInitListener {
  private static final String TAG = "vision:TTS";
  public TextToSpeech _tts;
  private int _qm;
  private Locale _language = Language.getDefaultLocale();
  private boolean _init = false;
  private static TTS staticTTS = null;
  private static String _engine = "";
  
  public static void init(final Context c) {
    if (staticTTS == null || !TTS.isRunning() || !_engine.equals(staticTTS._tts.getDefaultEngine())) {
      // I know this seems overly complex, but it is a workaround aroud the fact
      // that getDefaultEngine() sometimes returns an empty string
      if (staticTTS != null)
        _engine = staticTTS._tts.getDefaultEngine();
      staticTTS = new TTS(c);
      if (staticTTS._tts.getDefaultEngine().length() != 0)
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
    Log.d(TAG, "setLanguage to " + l.getLanguage());
    staticTTS._language = l;
    if (staticTTS._init)
      return staticTTS._tts.setLanguage(l);
    return -3;
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
    if (s == null)
      return;
    speak(s, staticTTS._qm);
  }
  
  /**
   * speak the given string in default queueMode, then wait for the TTS to
   * finish talking
   * 
   * @param s
   *          string to speak.
   */
  public static void speakSync(final String s) {
    if (s == null || s == "")
      return;
    speak(s, staticTTS._qm);
    waitUntilFinishTalking();
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
    final String ss = spellIfNeeded(s);
    if (VisionApplication.muted) {
      Log.d(TAG, "not speaking because application is muted");
      return;
    }
    Log.d(TAG, "speak : " + ss);
    staticTTS._tts.speak(ss, queueMode, null);
  }
  
  /**
   * Try to detect if we need to spell the string, and if yes, convert it to a
   * "spelled" string The TTS will sometimes automatically spell the string if
   * it detects it's a number or something, but this is TTS engine dependent, so
   * we cannot rely on it
   * 
   * @param s
   *          The string to potentially spell
   * @return The inputed string, transformed to "spelled" string if needed
   */
  private static String spellIfNeeded(final String s) {
    if (s.length() <= 3 || s.length() >= 15)
      return s;
    int digitCount = 0;
    for (final char c : s.toCharArray())
      if (c >= '0' && c <= '9' || c == '+')
        digitCount++;
    if (digitCount / (double) s.length() > 0.8)
      return spell(s);
    return s;
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
      setLanguage(_language);
    }
  }
  
  public static void waitUntilFinishTalking() {
    while (isSpeaking()) {
      // Wait for message to finish playing and then continue
    }
  }
  
  public static String spell(final String s) {
    if (s.length() == 0)
      return s;
    final StringBuilder $ = new StringBuilder();
    $.append(s.charAt(0));
    for (final char c : s.substring(1).toCharArray())
      $.append(' ').append(c);
    return $.toString();
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
