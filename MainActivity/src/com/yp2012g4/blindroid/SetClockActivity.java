package com.yp2012g4.blindroid;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.utils.BlindroidActivity;

public class SetClockActivity extends BlindroidActivity implements OnClickListener, OnGestureListener {
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
  
  @Override
  public void onBackPressed() {
    speakOut("Previous screen");
    setResult(-1);
    mHandler.postDelayed(mLaunchTask, 1000);
  }
  
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.back_button:
        speakOut("Previous screen");
        setResult(-1);
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.settings_button:
        speakOut("Settings");
        Intent intent = new Intent(this, ColorSettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.home_button:
        speakOut("Home");
        setResult(-2);
        mHandler.postDelayed(mLaunchTask, 1000);
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
    mHandler = new Handler();
    back = (TalkingImageButton) findViewById(R.id.back_button);
    back.setOnClickListener(this);
    back.setOnTouchListener(this);
    settings = (TalkingImageButton) findViewById(R.id.settings_button);
    settings.setOnClickListener(this);
    settings.setOnTouchListener(this);
    wai = (TalkingImageButton) findViewById(R.id.current_menu_button);
    wai.setOnClickListener(this);
    wai.setOnTouchListener(this);
    home = (TalkingImageButton) findViewById(R.id.home_button);
    home.setOnClickListener(this);
    home.setOnTouchListener(this);
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
    if (!hasFocus)
      return;
    TextView tvTitle = (TextView) findViewById(R.id.textView1);
    speakOut(tvTitle.getText().toString());
    while (_t.isSpeaking() == Boolean.TRUE) {
      // Wait for message to finish playing and then finish the activity
    }
    super.onWindowFocusChanged(hasFocus);
  }
}
