package com.yp2012g4.vision;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ActivityVoiceInput extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_voice_input);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_activity_voice_input, menu);
		return true;
	}

}