/***
 * Class that set a clock and returns the set time
 * 
 * @author Amir Blumental
 * @version 1.0
 */
package com.yp2012g4.blindroid;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yp2012g4.blindroid.tools.BlindroidActivity;

public class SetClockActivity extends BlindroidActivity implements OnGestureListener {
  public final static int HOUR_CODE = 0;
  public final static int MIN_CODE = 1;
  private GestureDetector gestureScanner;
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
    speakOut("Previous screen");
    setResult(-1);
    mHandler.postDelayed(mLaunchTask, 1000);
  }
  
  @Override
  public void onClick(View v) {
    Intent intent = new Intent(SetClockActivity.this, MainActivity.class);
    switch (v.getId()) {
      case R.id.back_button:
        speakOut("Previous screen");
        setResult(-1);
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.settings_button:
        speakOut("Settings");
        intent = new Intent(this, DisplaySettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.home_button:
        speakOut("Home");
        setResult(-2);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        break;
      case R.id.current_menu_button:
        speakOut("This is " + getString(R.string.title_activity_set_clock));
        break;
      default:
        break;
    }
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_set_clock);
    gestureScanner = new GestureDetector(this);
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
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_set_clock, menu);
    return true;
  }
  
  @Override
  public boolean onDown(MotionEvent e) {
    // TODO Auto-generated method stub
    return false;
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
  public void onLongPress(MotionEvent e) {
    // TODO Auto-generated method stub
  }
  
  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override
  public void onShowPress(MotionEvent e) {
    // TODO Auto-generated method stub
  }
  
  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    int result = Integer.valueOf(tvNum.getText().toString()).intValue();
    setResult(result);
    finish();
    return true;
  }
  
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return gestureScanner.onTouchEvent(event);
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
