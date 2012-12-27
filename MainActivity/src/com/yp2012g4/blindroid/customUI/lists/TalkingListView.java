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
 * 
 * 
 * @author Roman
 * 
 */
public class TalkingListView extends ListView implements OnScrollListener, OnItemClickListener, OnTouchListener, OnGestureListener {
  private ViewListRun run = null;
  private GestureDetector gDetector = new GestureDetector(this);
  private int selectedItem = 0;
  private boolean isInit = false;
  
  @Override public void onWindowFocusChanged(boolean hasWindowFocus) {
    super.onWindowFocusChanged(hasWindowFocus);
    this.requestFocusFromTouch();
    this.setSelection(selectedItem);
    if (run != null && !isInit) {
      run.onInitSpeak(selectedItem);
      isInit = true;
    }
  }
  
  private void init() {
    this.setOnScrollListener(this);
    this.setOnItemClickListener(this);
    this.setOnTouchListener(this);
  }
  
  public synchronized ViewListRun getRun() {
    return run;
  }
  
  public synchronized void setRun(ViewListRun run) {
    this.run = run;
  }
  
  public TalkingListView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }
  
  public TalkingListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }
  
  public TalkingListView(Context context) {
    super(context);
    init();
  }
  
  @Override public boolean onDown(MotionEvent e) {
    // TODO Auto-generated method stub
    return false;
  }
  
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
  
  @Override public void onLongPress(MotionEvent e) {
    // TODO Auto-generated method stub
  }
  
  @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override public void onShowPress(MotionEvent e) {
    // TODO Auto-generated method stub
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    if (run != null) {
      run.onClick(selectedItem);
    }
    return false;
  }
  
  @Override public boolean onTouch(View v, MotionEvent event) {
    gDetector.onTouchEvent(event);
    this.requestFocusFromTouch();
    this.setSelection(selectedItem);
    return true;
  }
  
  @Override public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    // TODO Auto-generated method stub
  }
  
  @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    // TODO Auto-generated method stub
  }
  
  @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
    // TODO Auto-generated method stub
  }
}
