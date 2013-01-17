package com.yp2012g4.blindroid.tools;

import java.util.Locale;
import java.util.regex.Pattern;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

// TODO: Code review 
// More documentation 
// Why use log::error?
// About language: there are some problems: 1) it's not global on a application level 2) other things ...
// Check if it's possible to spell strings instead of reading it, (phone numbers, etc...)
// Add javadoc
public class TTS {
  private TextToSpeech _tts;
  private int _qm;
  private Locale _language;
  
  public TTS(Context context, TextToSpeech.OnInitListener listener) {
    Log.e("TTS", "Constractor");
    // ?
    if (_tts != null)
      _tts.shutdown();
    _tts = new TextToSpeech(context, listener);
    // TODO: check this: _language == null ??
    _tts.setLanguage(_language);
    setQueueMode(TextToSpeech.QUEUE_FLUSH);
    setLanguage(Locale.US);
  }
  
  // TODO: is this needed? do this works? what should it do exactly?
  public boolean isRuning() {
    return _tts == null ? false : true;
  }
  
  public void setQueueMode(int queueMode) {
    Log.e("TTS", "setQueueMode");
    _qm = queueMode;
  }
  
  public void setLanguage(Locale l) {
    Log.e("TTS", "setLanguage");
    _language = l;
    // TODO: here, we need _tts.setLanguage()
  }
  
  public void speak(String s) {
    Log.e("TTS", "speak : " + s);
    if (null == s)
      return;
    if (!isPureEnglise(s))
      _tts.speak("Hebrew.", _qm, null);
    else
      _tts.speak(s, _qm, null);
  }
  
  public void stop() {
    Log.e("TTS", "stop");
    _tts.stop();
  }
  
  public void shutdown() {
    Log.e("TTS", "shutdown");
    stop();
    _tts.shutdown();
    _tts = null;
  }
  
  public boolean isSpeaking() {
    return _tts.isSpeaking();
  }
  
  /**
   * Returns true if s does not contain any hebrew charachters.
   * 
   * @param s
   * @return
   */
  public static boolean isPureEnglise(String s) {
    if (s == null)
      return true;
//    if (s == "")
//      return true;
//    final String tmp = new String(s);
    return !Pattern.compile("[*\\p{Hebrew}*]").matcher(s).find();
//    return true;
  }
}
