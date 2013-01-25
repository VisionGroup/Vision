/***
 * Class that set a clock and returns the set time
 * 
 * @author Amir Blumental
 * @version 1.0
 */
package com.yp2012g4.vision;

import java.util.Calendar;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.yp2012g4.vision.tools.VisionActivity;

public class SetClockActivity extends VisionActivity {
  public final static int HOUR_CODE = 0;
  public final static int MIN_CODE = 1;
  private TextView tvNum;
  private Calendar cal;
  // Can be either HOUR_CODE or MIN_CODE
  private int type;
  
  @Override
  public int getViewId() {
    return R.id.set_alarm_view;
  }
  
  /**
   * In order that the back key will be the same as the control bar's back
   */
  @Override
  public void onBackPressed() {
    setResult(-1);
    mHandler.postDelayed(mLaunchTask, 1000);
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_set_clock);
    init(0, getString(R.string.title_activity_set_clock), getString(R.string.set_clock_help));
//    gestureScanner = new GestureDetector(this);
    Bundle b = getIntent().getExtras();
    type = b.getInt("type");
    cal = Calendar.getInstance();
    cal.setTimeInMillis(System.currentTimeMillis());
    tvNum = (TextView) findViewById(R.id.number);
    TextView tvTitle = (TextView) findViewById(R.id.textView1);
    int number;
    String t;
    if (type == HOUR_CODE) {
      number = cal.get(Calendar.HOUR_OF_DAY);
      t = getString(R.string.setHour);
    } else {
      number = cal.get(Calendar.MINUTE);
      t = getString(R.string.setMinutes);
    }
    tvNum.setText(Integer.valueOf(number).toString());
    tvTitle.setText(t);
  }
  
  /**
   * change the displayed time when user fling the screen
   */
  @Override
  public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX, float velocityY) {
    int change = start.getRawY() < finish.getRawY() ? -1 : 1;
    int field = type == HOUR_CODE ? Calendar.HOUR_OF_DAY : Calendar.MINUTE;
    cal.roll(field, change);
    int value = cal.get(field);
    tvNum.setText(Integer.valueOf(value).toString());
    speakOut(Integer.valueOf(value).toString());
    return true;
  }
  
  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    switch (curr_view.getId()) {
      case R.id.back_button:
        setResult(-1);
        //$FALL-THROUGH$
      case R.id.tool_tip_button:
      case R.id.home_button:
      case R.id.current_menu_button:
        break;
      default:
        int result = Integer.valueOf(tvNum.getText().toString()).intValue();
        setResult(result);
        finish();
    }
    return false;
  }
  
  /**
   * Perform actions when the window get into focus we start the activity by
   * reading out loud the current title
   */
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    while (_t.isSpeaking()) {
      // Wait for message to finish playing and then finish the activity
    }
    if (!hasFocus)
      return;
    TextView tvTitle = (TextView) findViewById(R.id.textView1);
    speakOut(tvTitle.getText().toString());
    while (_t.isSpeaking()) {
      // Wait for message to finish playing and then finish the activity
    }
  }
}
