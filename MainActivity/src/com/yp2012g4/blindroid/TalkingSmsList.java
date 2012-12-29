package com.yp2012g4.blindroid;

import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.AdapterView;
import com.yp2012g4.blindroid.customUI.lists.SmsAdapter;
import com.yp2012g4.blindroid.customUI.lists.TalkingListView;
import com.yp2012g4.blindroid.customUI.lists.ViewListRun;

/**
 * Talking SMS list
 * 
 * @author Roman
 * 
 */
public class TalkingSmsList extends Activity implements OnInitListener {
  TalkingListView viewList;
  ArrayList<SmsType> details;
  AdapterView.AdapterContextMenuInfo info;
  protected TextToSpeech tts;
  
  /**
   * init when set on focus
   */
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    viewList.onWindowFocusChanged(hasFocus);
  }
  
  /**
   * on create init reads all the SMS messages and prepares the TalkingListView.
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_talking_sms_list);
    viewList = (TalkingListView) findViewById(R.id.TalkingSmsListView);
    tts = new TextToSpeech(getApplicationContext(), this);
    SmsReader smsReader = new SmsReader(getApplicationContext());
    details = smsReader.getIncomingMessages();
    viewList.setAdapter(new SmsAdapter(details, this));
    viewList.setRun(new ViewListRun() {
      @Override public void onClick(int selectedItem) {
        String onClickS = getName(selectedItem);
        onClickS += details.get(selectedItem).getBody();
        speakOut(onClickS);
      }
      
      @Override public void onFling(int selectedItem) {
        String onFlingS = getName(selectedItem);
        speakOut(onFlingS);
        // Toast.makeText(getApplicationContext(),
        // details.get(selectedItem).getAddress(), Toast.LENGTH_LONG).show();
      }
      
      @Override public void onInitSpeak(int selectedItem) {
        String onInitS = getName(0);
        speakOut(onInitS);
      }
    });
  }
  
  /**
   * initiate the TTS
   */
  @Override public void onInit(int status) {
    if (status == TextToSpeech.SUCCESS) {
      int r = tts.setLanguage(Locale.US);
      if (r == TextToSpeech.LANG_NOT_SUPPORTED || r == TextToSpeech.LANG_MISSING_DATA) {
        Log.e("tts", "error setLanguage");
        return;
      }
      return;
    }
    Log.e("tts", "error init language");
  }
  
  /**
   * 
   * @param selectedItem
   * @return the name or number of message sender.
   */
  String getName(int selectedItem) {
    if (details.size() >= selectedItem) {
      if (details.get(selectedItem).getPerson() != "")
        return "From " + details.get(selectedItem).getPerson() + "  ";
      String phoneNumber = details.get(selectedItem).getAddress();
      return "From" + phoneNumber + "  ";
    }
    return "";
  }
  
  /**
   * stop the TTS on back press.
   */
  @Override public void onBackPressed() {
    tts.stop();
    super.onBackPressed();
  }
  
  /**
   * 
   * @param s
   *          activate TTS speak with s.
   */
  void speakOut(String s) {
    tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
  }
}