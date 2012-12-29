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
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

public class DisplaySettingsActivity extends BlindroidActivity implements
    OnClickListener {
 
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_settings);
    mHandler = new Handler();
    TalkingImageButton b = (TalkingImageButton)findViewById(R.id.button_set_colors);
    b.setOnClickListener(this);
    b.setOnTouchListener(this);
    
    b = (TalkingImageButton)findViewById(R.id.button_set_theme);
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

   @Override
  public void onClick(View v)
   {
     if (v instanceof TalkingImageButton)
       speakOut(((TalkingImageButton) v).getContentDescription().toString());
     Intent intent;
    switch (v.getId())
    {
    case R.id.button_set_colors:
      intent = new Intent(DisplaySettingsActivity.this, ColorSettingsActivity.class);
      startActivity(intent);

     break;
    case R.id.button_set_theme:
      intent = new Intent(DisplaySettingsActivity.this, ThemeSettingsActivity.class);
      startActivity(intent);
     break;
    case R.id.settings_button:
      speakOut("Settings");
      break;
    case R.id.back_button:
      speakOut("Previous screen");
      mHandler.postDelayed(mLaunchTask, 1000);
      break;
    case R.id.home_button:
      speakOut("Home");
      mHandler.postDelayed(mLaunchTask, 1000);
      break;
    case R.id.current_menu_button:
      speakOut("This is " + getString(R.string.title_activity_display_settings));
      break;
    default :
     break;

    }

   }
   @Override
       public void onBackPressed() {
    // TODO Auto-generated method stub
    super.onBackPressed();
    DisplaySettingsActivity.this.finish();

   }
    

    @Override
    public int getViewId() {
      return R.id.displaySettingsActivity;
    }

}


