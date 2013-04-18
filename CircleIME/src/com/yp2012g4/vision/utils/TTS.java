package com.yp2012g4.vision.utils;

import java.util.Locale;
import java.util.regex.Pattern;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * 
 * @author Auster Yaron
 * 
 *         The TTS is make an handle to use the TTS class in android.
 * 
 */
public class TTS implements OnInitListener {
    private static final String TAG = "vision:TTS";
    private TextToSpeech _tts;
    private int _qm;
    private Locale _language;
    private boolean _init = false;

    /**
     * 
     * 
     * @param context
     *            the context that running the class.
     * @param listener
     *            the listener that connected from the activity.
     * @deprecated
     */
    @Deprecated
    public TTS(Context context, TextToSpeech.OnInitListener listener) {
	_tts = new TextToSpeech(context, listener);
	setQueueMode(TextToSpeech.QUEUE_FLUSH);
	setLanguage(Locale.US);
    }

    /**
     * 
     * 
     * @param context
     *            the context that running the class.
     * @param listener
     *            the listener that connected from the activity.
     */
    public TTS(Context context) {
	_tts = new TextToSpeech(context, this);
	setQueueMode(TextToSpeech.QUEUE_FLUSH); // TODO check if to move to
						// onInit - Amit
	setLanguage(Locale.US); // TODO check if to move to onInit - AMIT
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
     *            the new queue mode.
     */
    public void setQueueMode(int queueMode) {
	Log.i(TAG, "setQueueMode");
	_qm = queueMode;
    }

    /**
     * change the language.
     * 
     * @param languag
     *            the new language.
     */
    public void setLanguage(Locale languag) {
	Log.i(TAG, "setLanguage");
	_tts.setLanguage(_language);
    }

    /**
     * speak the given string.
     * 
     * @param s
     *            string to speak.
     */
    public void speak(String s) {
	Log.i(TAG, "speak : " + s);
	if (null == s)
	    return;
	// if (!isPureEnglise(s))
	// _tts.speak("Hebrew.", _qm, null);
	// else
	_tts.speak(s, _qm, null);
    }

    /**
     * speak the given string, in synchronous mode.
     * 
     * @param s
     *            string to speak.
     */
    public void syncSpeak(String s) {
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
	Log.i(TAG, "stop");
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
     * Check if speaking now.
     * 
     * @return true if speaking.
     */
    public boolean isSpeaking() {
	return _tts.isSpeaking();
    }

    /**
     * Check if the string contaion pure Englise.
     * 
     * @param s
     *            string to check.
     * @return true if only Englise letters.
     */
    public static boolean isPureEnglise(String s) {
	if (s == null)
	    return true;
	return !Pattern.compile("[\\p{InHebrew}]").matcher(s).find();
	// return !Pattern.compile("[*\\p{Hebrew}*]").matcher(s).find();
    }

    @Override
    public void onInit(int status) {
	if (status == TextToSpeech.SUCCESS)
	    _init = true;
    }
}
