/**
 * @author Maytal
 * 
 */
package com.yp2012g4.vision.settings;

import java.util.Locale;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.tools.VisionActivity;

public class DisplaySettingsActivity extends VisionActivity {
	/**
	 * get the activity's main view ID
	 * 
	 */
	@Override
	public int getViewId() {
		return R.id.displaySettingsActivity;
	}

	/**
	 * finish activity when back button pressed
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		DisplaySettingsActivity.this.finish();
	}

	
	/**
	 * Adds onSingleTapUp events to buttons in this view.
	 * 
	 * @param e
	 *            - motion event
	 * 
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		super.onSingleTapUp(e);
		Intent intent;
		switch (curr_view.getId()) {
		case R.id.button_set_colors:
			intent = new Intent(DisplaySettingsActivity.this,
					ColorSettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.button_set_theme:
			intent = new Intent(DisplaySettingsActivity.this,
					ThemeSettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.button_exit_launcher:
			PackageManager pm = getPackageManager();
			pm.clearPackagePreferredActivities(getPackageName());
			vibrate(300);
			break;
		case R.id.locale:
			myLocale = Locale.getDefault();
			Configuration config = new Configuration();
			if (myLocale.equals(Locale.US)) {
				Locale locale = new Locale("iw");
				Locale.setDefault(locale);
				config.locale = locale;
				speakOut(getString(R.string.switched_to_hebrew));
			} else if (myLocale.toString().equals("iw_IL")
					|| myLocale.toString().equals("iw")) { // Hebrew
				Locale.setDefault(Locale.US);
				config.locale = Locale.US;
				speakOut(getString(R.string.switched_to_english));
			}
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
			myLocale = Locale.getDefault();
			break;
		default:
			break;
		}
		return false;
	}

	/*
	 * @Override public void onConfigurationChanged(Configuration newConfig){
	 * Log.i("MyLog", "DisplaySettingsActivity::onConfigurationChanged");
	 * super.onConfigurationChanged(newConfig); if (myLocale != null){
	 * newConfig.locale = myLocale; Locale.setDefault(myLocale);
	 * getBaseContext().getResources().updateConfiguration(newConfig,
	 * getBaseContext().getResources().getDisplayMetrics()); } }
	 */

	/**
	 * Called when the activity is first created.
	 */
	/** */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_settings);
		// adjustLayoutSize(3);
		init(0, getString(R.string.display_settings_screen),
				getString(R.string.settings_help));
	}
}
