/***
 * Class that set a clock and returns the set time
 * 
 * @author Amir Blumental
 * @version 1.0
 */
package com.yp2012g4.vision.apps.alarm;

import java.util.Calendar;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.settings.VisionApplication;
import com.yp2012g4.vision.tools.VisionActivity;

public class SetClockActivity extends VisionActivity {
  public final static int HOUR_CODE = 0;
  public final static int MIN_CODE = 1;
  private TextView tvNum;
  private Calendar cal;
  // Can be either HOUR_CODE or MIN_CODE
  private int type;
  
  @Override public int getViewId() {
    return R.id.set_alarm_view;
  }
  
  /**
   * In order that the back key will be the same as the control bar's back
   */
  @Override public void onBackPressed() {
    setResult(AlarmActivity.USER_PRESSED_BACK);
    _mHandler.postDelayed(mLaunchTask, VisionApplication.DEFUALT_DELAY_TIME);
  }
  
  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_set_clock);
    init(0, getString(R.string.title_activity_set_clock), getString(R.string.set_clock_help));
    final Bundle b = getIntent().getExtras();
    type = b.getInt("type");
    updateDisplayByType();
  }
  
  /**
   * 
   */
  private void updateDisplayByType() {
    cal = Calendar.getInstance();
    cal.setTimeInMillis(System.currentTimeMillis());
    tvNum = (TextView) findViewById(R.id.number);
    final TextView tvTitle = (TextView) findViewById(R.id.textView1);
    int number;
    String t;
    if (type == HOUR_CODE) {
      number = cal.get(Calendar.HOUR_OF_DAY);
      t = getString(R.string.setHour);
    } else {
      number = cal.get(Calendar.MINUTE);
      t = getString(R.string.setMinutes);
    }
    tvNum.setText(Integer.toString(number));
    tvTitle.setText(t);
  }
  
  /**
   * change the displayed time when user fling the screen
   */
  @Override public boolean onFling(final MotionEvent start, final MotionEvent finish, final float velocityX, final float velocityY) {
    final int change = start.getRawY() < finish.getRawY() ? -1 : 1;
    final int field = type == HOUR_CODE ? Calendar.HOUR_OF_DAY : Calendar.MINUTE;
    cal.roll(field, change);
    final int value = cal.get(field);
    tvNum.setText(Integer.toString(value));
    speakOutAsync(Integer.toString(value));
    return true;
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View button = getButtonByMode();
    switch (button.getId()) {
      case R.id.back_button:
        setResult(AlarmActivity.USER_PRESSED_BACK);
        break;
      default:
        final int result = Integer.parseInt(tvNum.getText().toString());
        setResult(result);
        finish();
    }
    return false;
  }
  
  /**
   * Perform actions when the window get into focus we start the activity by
   * reading out loud the current title
   */
  @Override public void onWindowFocusChanged(final boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    _tts.waitUntilFinishTalking();
    if (!hasFocus)
      return;
    final TextView tvTitle = (TextView) findViewById(R.id.textView1);
    speakOutSync(tvTitle.getText().toString());
  }
}
