package com.yp2012g4.blindroid.customUI.lists;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Extension of ListView with different gestures
 * 
 * @author Roman
 * 
 */
public class TalkingListView extends ListView implements OnScrollListener, OnItemClickListener, OnTouchListener, OnGestureListener {
  private ViewListRun run = null;
  private GestureDetector gDetector = new GestureDetector(this);
  private int selectedItem = 0;
  private boolean isInit = false;
  
  /**
   * On window focus change behavior
   */
  @Override public void onWindowFocusChanged(boolean hasWindowFocus) {
    super.onWindowFocusChanged(hasWindowFocus);
    this.requestFocusFromTouch();
    this.setSelection(selectedItem);
    if (run != null && !isInit) {
      run.onInitSpeak(selectedItem);
      isInit = true;
    }
  }
  
  /**
   * Init function.
   */
  private void init() {
    this.setOnScrollListener(this);
    this.setOnItemClickListener(this);
    this.setOnTouchListener(this);
  }
  
  /**
   * @return the auxiliary class
   */
  public synchronized ViewListRun getRun() {
    return run;
  }
  
  /**
   * set the auxiliary class
   * 
   * @param run
   */
  public synchronized void setRun(ViewListRun run) {
    this.run = run;
  }
  
  /**
   * constructor of the ListView
   * 
   * @param context
   * @param attrs
   * @param defStyle
   */
  public TalkingListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }
  
  /**
   * constructor of the ListView
   * 
   * @param context
   * @param attrs
   */
  public TalkingListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }
  
  /**
   * constructor of the ListView
   * 
   * @param context
   */
  public TalkingListView(Context context) {
    super(context);
    init();
  }
  
  /**
   * Overridden onDown.
   */
  @Override public boolean onDown(MotionEvent e) {
    // TODO Auto-generated method stub
    return false;
  }
  
  /**
   * onFling behavior
   */
  @Override public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX, float velocityY) {
    if (start.getRawY() < finish.getRawY()) {
      if (selectedItem < this.getCount() - 1) {
        selectedItem++;
      }
    } else {
      if (selectedItem > 0) {
        selectedItem--;
      }
    }
    if (run != null) {
      run.onFling(selectedItem);
    }
    return true;
  }
  
  /**
   * Overridden function to suppress the ability
   */
  @Override public void onLongPress(MotionEvent e) {
    // TODO Auto-generated method stub
  }
  
  /**
   * Overridden function to suppress the ability
   */
  @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    // TODO Auto-generated method stub
    return false;
  }
  
  /**
   * Overridden function to suppress the ability
   */
  @Override public void onShowPress(MotionEvent e) {
    // TODO Auto-generated method stub
  }
  
  /**
   * Overridden function to suppress the ability
   */
  @Override public boolean onSingleTapUp(MotionEvent e) {
    if (run != null) {
      run.onClick(selectedItem);
    }
    return false;
  }
  
  /**
   * Use our own gesture detection for custom gestures 
   */
  @Override public boolean onTouch(View v, MotionEvent event) {
    gDetector.onTouchEvent(event);
    this.requestFocusFromTouch();
    this.setSelection(selectedItem);
    return true;
  }
  /**
   * Overridden function to suppress the ability
   */
  @Override public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    // TODO Auto-generated method stub
  }
  /**
   * Overridden function to suppress the ability
   */
  @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    // TODO Auto-generated method stub
  }
  /**
   * Overridden function to suppress the ability
   */
  @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
    // TODO Auto-generated method stub
  }
}
