package com.yp2012g4.blindroid;

import com.yp2012g4.blindroid.R;
import android.graphics.PorterDuff.Mode;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.Color;

/**
 * @author Maytal
 *
 */
public class DisplaySettings {
	
	protected SparseIntArray color_to_string = new SparseIntArray();
	
	private int textColor = R.color.WHITE;
	private int backgroundColor = R.color.BLACK;
	
	public DisplaySettings() {
		color_to_string.append(R.color.BLACK, Color.parseColor("#000000"));
		color_to_string.append(R.color.WHITE, Color.parseColor("#FFFFFF"));
		color_to_string.append(R.color.RED, Color.parseColor("#FF0000"));
		color_to_string.append(R.color.GREEN, Color.parseColor("#04B431"));
		color_to_string.append(R.color.BLUE, Color.parseColor("#2E9AFE"));
	}
	
	public void setColors(int int1, int int2) {
		textColor = int1;
		backgroundColor = int2;
	}
	
	public void applyButtonSettings (View v) {
		
		if (v instanceof ImageButton) {
			// Set the correct new color
			if (textColor == R.color.WHITE)
				((ImageView)v).setColorFilter(color_to_string.get(backgroundColor), Mode.LIGHTEN);
			else
				((ImageView)v).setColorFilter(color_to_string.get(textColor), Mode.DARKEN);
			((ImageView)v).setBackgroundColor(color_to_string.get(backgroundColor));
		}
	}
}
