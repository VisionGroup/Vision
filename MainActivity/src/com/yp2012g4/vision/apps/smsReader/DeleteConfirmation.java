package com.yp2012g4.vision.apps.smsReader;

/**
 * An activity offering the option to change the text size
 * 
 * @author Maytal
 * 
 */
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.tools.VisionActivity;

public class DeleteConfirmation extends VisionActivity {
  public static final String ACTIVITY_EXTRA = "activity";
  public static final String CANCEL_FLAG = "CANCEL";
  public static final String DELETE_FLAG = "DELETE";
  @SuppressWarnings("rawtypes") private Class _caller;
  private static final String TAG = "vision:DeleteConfirmation";
  
  /**
   * get the activity's main view ID
   * 
   */
  @Override public int getViewId() {
    return R.id.delete_confirmation;
  }
  
  /**
   * Adds onSingleTapUp events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   * 
   * @param e
   *          - motion event
   * 
   */
  /**
   * on any button press or screen touch we turn the snooze off
   */
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    Log.d(TAG, _caller.toString());
    final Intent intent = newFlaggedIntent(getApplicationContext(), _caller).putExtra(ACTION_EXTRA, CANCEL_FLAG);
    Log.d(TAG, intent.toString());
    startActivity(intent);
    finish();
    return false;
  }
  
  @Override public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float f1, final float f2) {
    final Intent intent = newFlaggedIntent(getApplicationContext(), _caller).putExtra(ACTION_EXTRA, DELETE_FLAG);
    startActivity(intent);
    finish();
    return super.onFling(e1, e2, f1, f2);
  }
  
  /**
   * Called when the activity is first created.
   * */
  @Override public void onCreate(final Bundle b) {
    super.onCreate(b);
    setContentView(R.layout.activity_delete_confirmation);
    init(0, getString(R.string.delete_confirmation_screen), getString(R.string.delete_confirmation_help));
    try {
      _caller = Class.forName(getIntent().getStringExtra(ACTIVITY_EXTRA));
    } catch (final ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
