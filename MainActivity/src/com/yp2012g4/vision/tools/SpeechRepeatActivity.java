package com.yp2012g4.vision.tools;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yp2012g4.vision.R;

public class SpeechRepeatActivity extends Activity implements OnClickListener, OnInitListener {
  // voice recognition and general variables
  // variable for checking Voice Recognition support on user device
  private static final int VR_REQUEST = 999;
  // ListView for displaying suggested words
  private ListView wordList;
  // Log tag for output information
  private final String LOG_TAG = "SpeechRepeatActivity";// ***enter your own
  
  // tag here***
  // TTS variables
  // variable for checking TTS engine data on user device
  // private int MY_DATA_CHECK_CODE = 0;
  // Text To Speech instance
  // private TextToSpeech repeatTTS;
  @Override protected void onCreate(Bundle savedInstanceState) {
    // call superclass
    super.onCreate(savedInstanceState);
    // set content view
    setContentView(R.layout.activity_activity_voice_input);
    // gain reference to speak button
    Button speechBtn = (Button) findViewById(R.id.speech_btn);
    // gain reference to word list
    wordList = (ListView) findViewById(R.id.word_list);
    // find out whether speech recognition is supported
    PackageManager packManager = getPackageManager();
    List<ResolveInfo> intActivities = packManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
    if (intActivities.size() != 0)
      // speech recognition is supported - detect user button clicks
      speechBtn.setOnClickListener(this);
    else {
      // speech recognition not supported, disable button and output
      // message
      speechBtn.setEnabled(false);
      Toast.makeText(this, "Oops - Speech recognition not supported!", Toast.LENGTH_LONG).show();
    }
    // detect user clicks of suggested words
    wordList.setOnItemClickListener(new OnItemClickListener() {
      // click listener for items within list
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // cast the view
        TextView wordView = (TextView) view;
        // retrieve the chosen word
        String wordChosen = (String) wordView.getText();
        // output for debugging
        Log.v(LOG_TAG, "chosen: " + wordChosen);
        // output Toast message
        // Alter for your activity name
        Toast.makeText(SpeechRepeatActivity.this, "You said: " + wordChosen, Toast.LENGTH_SHORT).show();
      }
    });
  }
  
  @Override public void onInit(int arg0) {
    // Auto-generated method stub
  }
  
  /**
   * Called when the user presses the speak button
   */
  @Override public void onClick(View v) {
    if (v.getId() == R.id.speech_btn)
      // listen for results
      listenToSpeech();
  }
  
  /**
   * Instruct the app to listen for user speech input
   */
  private void listenToSpeech() {
    // start the speech recognition intent passing required data
    Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    // indicate package
    listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
    // message to display while listening
    listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a word!");
    // set speech model
    listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    // specify number of results to retrieve
    listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
    // start listening
    startActivityForResult(listenIntent, VR_REQUEST);
  }
  
  /**
   * onActivityResults handles: - retrieving results of speech recognition
   * listening - retrieving result of TTS data check
   */
  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // check speech recognition result
    /*
     * if (requestCode == VR_REQUEST && resultCode == RESULT_OK) { //store the
     * returned word list as an ArrayList //ArrayList<String> suggestedWords =
     * data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); //set the
     * retrieved list to display in the ListView using an ArrayAdapter
     * //wordList.setAdapter(new ArrayAdapter<String> (this, R.layout.word,
     * suggestedWords)); }
     */
    // tss code here
    // call superclass method
    super.onActivityResult(requestCode, resultCode, data);
  }
}
