package com.yp2012g4.vision.tools;

import java.util.Locale;
import java.util.regex.Pattern;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * This class is a wrapper class for the TTS engine.
 * 
 * @author Auster Yaron
 * 
 */
public class TTS implements OnInitListener {
  private static final String TAG = "vision:TTS";
  public TextToSpeech _tts;
  private int _qm;
  private Locale _language;
  private boolean _init = false;
  
  /**
   * 
   * 
   * @param context
   *          the context that is running the class.
   * @param listener
   *          the listener that is connected from the activity.
   */
  public TTS(final Context context) {
    _tts = new TextToSpeech(context, this);
  }
  
  /**
   * @return true if the TTS engine is initialized.
   */
  public boolean isRuning() {
    return _init;
  }
  
  /**
   * change the queue mode.
   * 
   * @param queueMode
   *          the new queue mode.
   */
  public void setQueueMode(final int queueMode) {
    Log.d(TAG, "setQueueMode");
    _qm = queueMode;
  }
  
  /**
   * change the language.
   * 
   * @param languag
   *          the new language.
   */
  public void setLanguage(final Locale l) {
    Log.d(TAG, "setLanguage");
    _language = l;
    _tts.setLanguage(l);
  }
  
  /**
   * 
   * @return The language being set.
   */
  public Locale getLanguage() {
    Log.d(TAG, "getLanguage");
    return _language;
  }
  
  /**
   * speak the given string.
   * 
   * @param s
   *          string to speak.
   */
  public void speak(final String s) {
    Log.d(TAG, "speak : " + s);
    if (null != s)
      _tts.speak(s, _qm, null);
  }
  
  /**
   * speak the given string, in synchronous mode.
   * 
   * @param s
   *          string to speak.
   */
  public void syncSpeak(final String s) {
    speak(s);
    while (isSpeaking())
      try {
        Thread.sleep(1000);
      } catch (final Exception e) {
        e.printStackTrace();
      }
  }
  
  /**
   * Stop speak!.
   * 
   */
  public void stop() {
    Log.d(TAG, "stop");
    _tts.stop();
  }
  
  /**
   * Close the TTS engine.
   * 
   */
  public void shutdown() {
    stop();
    _tts.shutdown();
    _tts = null;
  }
  
  /**
   * 
   * @return true if currently speaking, false otherwise.
   */
  public boolean isSpeaking() {
    return _tts.isSpeaking();
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
      setQueueMode(TextToSpeech.QUEUE_FLUSH);
      setLanguage(Locale.US);
    }
  }
  
  public void waitUntilFinishTalking() {
    while (_tts.isSpeaking()) {
      // Wait for message to finish playing and then finish the activity
    }
  }
}
