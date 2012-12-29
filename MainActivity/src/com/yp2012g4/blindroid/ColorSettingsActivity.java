/**
 * @author Maytal
 * 
 */
package com.yp2012g4.blindroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

public class ColorSettingsActivity extends BlindroidActivity implements OnClickListener {
  private static void changeSettings(int int1, int int2) {
    DisplaySettings.setColors(int1, int2);
  }
  
  @Override public int getViewId() {
    return R.id.ColorSettingsActivity;
  }
  
  @Override public void onClick(View v) {
    Intent intent = new Intent(ColorSettingsActivity.this, MainActivity.class);
    if (v instanceof TalkingButton)
      speakOut(((TalkingButton) v).getText().toString());
    switch (v.getId()) {
      case R.id.WhiteBlack:
        changeSettings(R.color.WHITE, R.color.BLACK);
        break;
      case R.id.WhiteRed:
        changeSettings(R.color.WHITE, R.color.RED);
        break;
      case R.id.RedBlack:
        changeSettings(R.color.RED, R.color.BLACK);
        break;
      case R.id.WhiteGreen:
        changeSettings(R.color.WHITE, R.color.GREEN);
        break;
      case R.id.GreenBlack:
        changeSettings(R.color.GREEN, R.color.BLACK);
        break;
      case R.id.WhiteBlue:
        changeSettings(R.color.WHITE, R.color.BLUE);
        break;
      case R.id.BlueBlack:
        changeSettings(R.color.BLUE, R.color.BLACK);
        break;
      case R.id.settings_button:
        speakOut("Settings");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.back_button:
        speakOut("Previous screen");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.home_button:
        speakOut("Home");
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.current_menu_button:
        speakOut("This is " + getString(R.string.title_activity_color_settings));
        return;
      default:
        break;
    }
    mHandler.postDelayed(mLaunchTask, 1000);
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_color_settings);
    // tts = new TextToSpeech(this, this);
    mHandler = new Handler();
    TalkingButton b = (TalkingButton) findViewById(R.id.WhiteBlack);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.WhiteRed);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.RedBlack);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.WhiteGreen);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.GreenBlack);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.WhiteBlue);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.BlueBlack);
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
  
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    return true;
  }
  
  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
  }
}
