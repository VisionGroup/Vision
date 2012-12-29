package com.yp2012g4.blindroid;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

public class MainActivity extends BlindroidActivity implements OnClickListener {
  /*
   * @Override public void onRestart() { super.onRestart();
   * 
   * DisplaySettingsApplication appState = ((DisplaySettingsApplication)
   * getApplication());
   * appState.settings.applyButtonSettings(findViewById(R.id.sos_button));
   * appState.settings
   * .applyButtonSettings(findViewById(R.id.alarm_clock_button));
   * appState.settings.applyButtonSettings(findViewById(R.id.back_button));
   * appState.settings.applyButtonSettings(findViewById(R.id.time_button));
   * appState
   * .settings.applyButtonSettings(findViewById(R.id.phone_status_button));
   * appState.settings
   * .applyButtonSettings(findViewById(R.id.phone_status_button));
   * appState.settings.applyButtonSettings(findViewById(R.id.next_button));
   * appState.settings .applyButtonSettings(findViewById(R.id.settings_button));
   * appState.settings
   * .applyButtonSettings(findViewById(R.id.where_am_i_button));
   * appState.settings
   * .applyButtonSettings(findViewById(R.id.quick_dial_button));
   * appState.settings
   * .applyButtonSettings(findViewById(R.id.quick_sms_button));
   * 
   * }
   */
  @Override
  public int getViewId() {
    return R.id.MainActivityView;
  }
  
  @Override
  public void onClick(View v) {
    Intent intent;
    // speakOut(((Button) v).getText().toString());
    switch (v.getId()) {
      case R.id.sos_button:
        speakOut("SOS");
        intent = new Intent(MainActivity.this, SOSActivity.class);
        startActivity(intent);
        break;
      case R.id.time_button:
        speakOut("Time");
        intent = new Intent(MainActivity.this, SpeakingClockActivity.class);
        startActivity(intent);
        break;
      case R.id.where_am_i_button:
        speakOut("Where am I?");
        intent = new Intent(MainActivity.this, WhereAmIActivity.class);
        startActivity(intent);
        break;
      case R.id.phone_status_button:
        speakOut("Phone status");
        intent = new Intent(MainActivity.this, PhoneStatusActivity.class);
        startActivity(intent);
        break;
      case R.id.alarm_clock_button:
        speakOut("Alarm clock");
        intent = new Intent(MainActivity.this, AlarmActivity.class);
        startActivity(intent);
        break;
      case R.id.quick_dial_button:
        speakOut("Quick dial");
        intent = new Intent(MainActivity.this, QuickDialActivity.class);
        startActivity(intent);
        break;
      case R.id.quick_sms_button:
        speakOut("Quick SMS");
        intent = new Intent(MainActivity.this, QuickSMSActivity.class);
        startActivity(intent);
        break;
      case R.id.read_sms_button:
        speakOut("Starting SMS reader please wait");
        intent = new Intent(MainActivity.this, TalkingSmsList.class);
        startActivity(intent);
        break;
      case R.id.back_button:
        speakOut("Previous screen");
        break;
      case R.id.settings_button:
        speakOut("Settings");
        intent = new Intent(MainActivity.this, DisplaySettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.home_button:
        speakOut("Home");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.current_menu_button:
        speakOut("This is " + "the home screen");
        break;
      default:
        break;
    }
  }
  
  /** Called when the activity is first created. */
  // private TextToSpeech tts;
  // private Rect rect;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // TODO: Add to every activity!! or to the new Activity intrerface Yaron
    // is making
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    // tts = new TextToSpeech(this, this);
    TalkingImageButton b = (TalkingImageButton) findViewById(R.id.sos_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingImageButton) findViewById(R.id.time_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingImageButton) findViewById(R.id.where_am_i_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingImageButton) findViewById(R.id.phone_status_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingImageButton) findViewById(R.id.alarm_clock_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingImageButton) findViewById(R.id.quick_dial_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingImageButton) findViewById(R.id.quick_sms_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingImageButton) findViewById(R.id.read_sms_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    back = (TalkingImageButton) findViewById(R.id.back_button);
    back.setOnClickListener(this);
    back.setOnTouchListener(this);
    next = (TalkingImageButton) findViewById(R.id.settings_button);
    next.setOnClickListener(this);
    next.setOnTouchListener(this);
    settings = (TalkingImageButton) findViewById(R.id.home_button);
    settings.setOnClickListener(this);
    settings.setOnTouchListener(this);
    wai = (TalkingImageButton) findViewById(R.id.current_menu_button);
    wai.setOnClickListener(this);
    wai.setOnTouchListener(this);
  }
}