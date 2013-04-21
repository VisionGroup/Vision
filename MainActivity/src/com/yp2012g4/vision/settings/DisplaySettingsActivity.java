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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.CalcActivity;
import com.yp2012g4.vision.MainActivity;
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
		if (super.onSingleTapUp(e))
			return true;
		Intent intent;
		View button = getButtonByMode();
		switch (button.getId()) {
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
			config = new Configuration();
			if (myLocale.equals(Locale.US)) {
				Locale locale = new Locale("iw");
				Locale.setDefault(locale);
				config.locale = locale;
				// _t._tts.setEngineByPackageName("il.co.aharontts.android");
				speakOut(getString(R.string.switched_to_hebrew));
			} else if (myLocale.toString().equals("iw_IL")
					|| myLocale.toString().equals("iw")) { // Hebrew
				Locale.setDefault(Locale.US);
				config.locale = Locale.US;
				// _t._tts.setEngineByPackageName("com.ivona.tts");
				speakOut(getString(R.string.switched_to_english));
			}
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
			myLocale = Locale.getDefault();
			intent = new Intent(this, MainActivity.class);
			// setResult(RESULT_OK, null);
			startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			finish();

			break;
		case R.id.button_selecting_mode:
			switch (selectButtonMode) {
			case 0:
				selectButtonMode = 1;
				speakOut(getString(R.string.sticky_buttons_mode));
				break;
			case 1:
				selectButtonMode = 0;
				speakOut(getString(R.string.regular_buttons_mode));
				break;
			default:
				break;
			}
			break;
		case R.id.calculator:
			intent = new Intent(this, CalcActivity.class);
			startActivity(intent/* .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) */);
		default:
			break;
		}
		return false;
	}

	/**
	 * Called when the activity is first created.
	 */
	/** */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("MyLog", "DisplaySettings:: onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_settings);
		// adjustLayoutSize(3);
		init(0, getString(R.string.display_settings_screen),
				getString(R.string.settings_help));
	}

}
