/**
 * @author Maytal
 *
 */

package com.yp2012g4.blindroid;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

public class ThemeSettingsActivity extends onTouchEventClass implements
    OnClickListener {
 
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
    super.onCreate(savedInstanceState);
    DisplaySettings.setThemeToActivity(this);
    setContentView(R.layout.activity_theme_settings);
    tts = new TextToSpeech(this, this);
    mHandler = new Handler();
    TalkingButton b = (TalkingButton)findViewById(R.id.Small_text_size_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    
    b = (TalkingButton)findViewById(R.id.Normal_text_size_button);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    
    b = (TalkingButton)findViewById(R.id.Large_text_size_button);
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
   }

   public void onClick(View v)
   {
     if (v instanceof TalkingButton)
       speakOut(((TalkingButton) v).getText().toString());
    switch (v.getId())
    {
    case R.id.Small_text_size_button:
        DisplaySettings.THEME="SMALL";
        DisplaySettings.settingChanged=true;
        DisplaySettings.SIZE="SMALL";
        mHandler.postDelayed(mLaunchTask,1000);

     break;
    case R.id.Normal_text_size_button:
        DisplaySettings.THEME="DEFAULT";
        DisplaySettings.settingChanged=true;
        DisplaySettings.SIZE="NORMAL";
        mHandler.postDelayed(mLaunchTask,1000);
     break;
    case R.id.Large_text_size_button:
      DisplaySettings.THEME="LARGE";
      DisplaySettings.settingChanged=true;
      DisplaySettings.SIZE="LARGE";
      mHandler.postDelayed(mLaunchTask,1000);
   break;
    default :
     break;

    }

   }
       public void onBackPressed() {
    // TODO Auto-generated method stub
    super.onBackPressed();
    ThemeSettingsActivity.this.finish();

   }
   
  
   //will launch the activity
    private Runnable mLaunchTask = new Runnable() {
        public void run() {
          finish();
        }
     };


    @Override
    public int getViewId() {
      return R.id.ThemeSettingsActivity;
    }

}

