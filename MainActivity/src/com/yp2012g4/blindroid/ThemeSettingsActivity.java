/**
 * @author Maytal
 * 
 */
package com.yp2012g4.blindroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

public class ThemeSettingsActivity extends BlindroidActivity implements OnClickListener {
  @Override public int getViewId() {
    return R.id.ThemeSettingsActivity;
  }
  
  @Override public void onClick(View v) {
    Intent intent = new Intent(ThemeSettingsActivity.this, MainActivity.class);
    if (v instanceof TalkingButton)
      speakOut(((TalkingButton) v).getText().toString());
    switch (v.getId()) {
      case R.id.Small_text_size_button:
        DisplaySettings.THEME = "SMALL";
        DisplaySettings.settingChanged = true;
        DisplaySettings.SIZE = "SMALL";
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.Normal_text_size_button:
        DisplaySettings.THEME = "DEFAULT";
        DisplaySettings.settingChanged = true;
        DisplaySettings.SIZE = "NORMAL";
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.Large_text_size_button:
        DisplaySettings.THEME = "LARGE";
        DisplaySettings.settingChanged = true;
        DisplaySettings.SIZE = "LARGE";
        mHandler.postDelayed(mLaunchTask, 1000);
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
        speakOut("This is " + getString(R.string.title_activity_theme_settings));
        break;
      default:
        break;
    }
  }
  
  /** Called when the activity is first created. */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_theme_settings);
    mHandler = new Handler();
    TalkingButton b = (TalkingButton) findViewById(R.id.Small_text_size_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.Normal_text_size_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    b = (TalkingButton) findViewById(R.id.Large_text_size_button);
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
