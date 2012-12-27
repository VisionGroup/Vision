package com.yp2012g4.blindroid;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.AdapterView;

import com.yp2012g4.blindroid.customUI.lists.ContactsAdapter;
import com.yp2012g4.blindroid.customUI.lists.SmsAdapter;
import com.yp2012g4.blindroid.customUI.lists.TalkingListView;
import com.yp2012g4.blindroid.customUI.lists.ViewListRun;

public class ContactsListActivity extends Activity implements OnInitListener {
  TalkingListView viewList;
  ArrayList<ContactType> details;
  AdapterView.AdapterContextMenuInfo info;
  protected TextToSpeech tts;
  
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    viewList.onWindowFocusChanged(hasFocus);
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.contacts_list);
    viewList = (TalkingListView) findViewById(R.id.ContactsListView);
    tts = new TextToSpeech(getApplicationContext(), this);
    SmsReader smsReader = new SmsReader(getApplicationContext());
//    details = smsReader.getIncomingMessages();
    viewList.setAdapter(new ContactsAdapter(details, this));
    
    
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
  
  private String getName(int selectedItem) {
    if (details.size() >= selectedItem) {
      if (details.get(selectedItem).getPerson() != "") {
        return "From " + details.get(selectedItem).getPerson() + "  ";
      } else {
        String phoneNumber = details.get(selectedItem).getAddress();
        return "From" + phoneNumber + "  ";
      }
    }
    return "";
  }
  
  @Override public void onBackPressed() {
    tts.stop();
    super.onBackPressed();
  }
  
  
  private void speakOut(String s) {
    tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
  }
}