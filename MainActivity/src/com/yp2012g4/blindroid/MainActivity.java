package com.yp2012g4.blindroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yp2012g4.blindroid.utils.BlindroidActivity;

public class MainActivity extends BlindroidActivity {
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
  @Override public int getViewId() {
    return R.id.MainActivityView;
  }
  
  @Override public void onClick(View v) {
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
        break;
      case R.id.current_menu_button:
        speakOut("This is " + "the home screen");
        break;
      default:
        break;
    }
  }
  
  /** Called when the activity is first created. */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);   
  }
  
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      speakOut("Main screen");
    }
  }
}