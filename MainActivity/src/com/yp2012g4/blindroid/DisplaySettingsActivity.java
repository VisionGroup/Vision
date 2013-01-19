/**
 * @author Maytal
 * 
 */
package com.yp2012g4.blindroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yp2012g4.blindroid.tools.BlindroidActivity;

public class DisplaySettingsActivity extends BlindroidActivity {
  /**
   * get the activity's main view ID
   * 
   */
  @Override
  public int getViewId() {
    return R.id.displaySettingsActivity;
  }
  
  @Override
  public void onBackPressed() {
    // TODO Auto-generated method stub
    super.onBackPressed();
    DisplaySettingsActivity.this.finish();
  }
  
  /**
   * Adds onClick events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   * 
   * @param v
   *          - a View object on the screen
   */
  @Override
  public void onClick(View v) {
    super.onClick(v);
    Intent intent;
//    if (v instanceof TalkingImageButton)
//      speakOut(((TalkingImageButton) v).getReadText());
//    Intent intent = new Intent(DisplaySettingsActivity.this, MainActivity.class);
    switch (v.getId()) {
      case R.id.button_set_colors:
        intent = new Intent(DisplaySettingsActivity.this, ColorSettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.button_set_theme:
        intent = new Intent(DisplaySettingsActivity.this, ThemeSettingsActivity.class);
        startActivity(intent);
        break;
      default:
        break;
    }
  }
  
  /**
   * Called when the activity is first created.
   */
  /** */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_settings);
    init(0, getString(R.string.display_settings_screen), getString(R.string.settings_help));
  }
}
