package com.yp2012g4.vision.customUI.lists;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.RelativeLayout;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * List container for views. building static view, which contains grid of
 * rows*cols items. each items behavior should be implemented in the Adapter.
 * 
 * @author Roman
 * 
 */
public class TalkingListView extends RelativeLayout {
  private final int DEFAULT_ROWS = 1;
  private final int DEFAULT_COLS = 1;
  private int _rows;
  private int _cols;
  private int _page = 0;
  private int _numOfPages = 0;
  private Adapter _adapter;
  private View[] _dispView;
  private boolean _init = false;
  
  public TalkingListView(final Context context) {
    super(context);
  }
  
  public TalkingListView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    getAttr(context, attrs);
  }
  
  public TalkingListView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
    getAttr(context, attrs);
  }
  
  private void getAttr(final Context context, final AttributeSet attrs) {
    final TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TalkingListView, 0, 0);
    try {
      _rows = ta.getInteger(R.styleable.TalkingListView_rows, DEFAULT_ROWS);
      _cols = ta.getInteger(R.styleable.TalkingListView_cols, DEFAULT_COLS);
      _dispView = new View[_rows * _cols];
    } finally {
      ta.recycle();
    }
  }
  
  /**
   * Set new view array to be displayed.
   * 
   * @param n
   */
  private void setNewViewArr(final int n) {
    for (int i = 0; i < _rows; i++)
      for (int j = 0; j < _cols; j++) {
        final int pos = n * _rows * _cols + i * _cols + j;
        final int vp = i * _cols + j;
        final View oldV = _dispView[vp];
        if (pos < _adapter.getCount()) {
          _dispView[vp] = _adapter.getView(pos, oldV, this);
          if (_dispView[vp].getId() == NO_ID)
            _dispView[vp].setId(vp + 1);
        } else
          _dispView[vp] = null;
      }
  }
  
  /**
   * Remove old view's. Set new view's parameters and add it to the view.
   */
  private void setNewViewDisp() {
    removeAllViews();
    LayoutParams params = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
    setViewSize(params);
    addView(_dispView[0], params);
    for (int i = 1; i < _rows * _cols; i++) {
      params = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
          android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
      if (_dispView[i] == null)
        break;
      final int colPos = i % _cols, rowPos = i / _cols;
      if (colPos != 0)
        params.addRule(RelativeLayout.RIGHT_OF, _dispView[i - 1].getId());
      if (rowPos > 0)
        params.addRule(RelativeLayout.BELOW, _dispView[i - _cols].getId());
      setViewSize(params);
      addView(_dispView[i], params);
      _dispView[i].invalidate();
    }
    invalidate();
  }
  
  /**
   * sets view size according to the actual screen size.
   * 
   * @param v
   * @param p
   */
  private void setViewSize(final LayoutParams p) {
    if (_rows == 0 || _cols == 0)
      return;
    final int h = getHeight() / _rows;
    final int w = getWidth() / _cols;
    if (p != null) {
      p.height = h;
      p.width = w;
    }
  }
  
  /**
   * Set page number "pageNum" to be shown If page doesn't exist the view will
   * not change.
   * 
   * @param
   */
  private void setPage(final int n) {
    if (_adapter == null)
      return;
    if (n < 0)
      return;
    if (n >= _numOfPages)
      return;
    setNewViewArr(n);
    setNewViewDisp();
    _page = n;
  }
  
  /**
   * Initialize the layout.
   */
  @Override protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
    super.onLayout(changed, l, t, r, b);
    if (_adapter != null && _init != true) {
      setPage(_page);
      _init = true;
    }
    /**
     * to update key's position
     */
    final VisionActivity h = (VisionActivity) getContext();
    h.getButtonsPosition(this);
  }
  
  /**
   * @return the cols
   */
  public int getCols() {
    return _cols;
  }
  
  /**
   * @return the rows
   */
  public int getRows() {
    return _rows;
  }
  
  /**
   * 
   * @param a
   *          the adapter to set
   */
  public void setAdapter(final Adapter a) {
    _adapter = a;
    final int mod = _adapter.getCount() / (_cols * _rows);
    if (_adapter.getCount() % (_cols * _rows) != 0)
      _numOfPages = mod + 1;
    else
      _numOfPages = mod;
    setPage(0);
  }
  
  /**
   * sets the next page to be displayed on the screen
   */
  public boolean nextPage() {
    if (_page >= _numOfPages)
      return false;
    setPage(_page + 1);
    return true;
  }
  
  /**
   * sets the previous page to be displayed on the screen
   */
  public boolean prevPage() {
    if (_page <= 0)
      return false;
    setPage(_page - 1);
    return true;
  }
  
  /**
   * 
   * @return currently displayed items.
   */
  public long[] getDisplayedItemIds() {
    final long[] $ = new long[_rows * _cols];
    for (int i = 0; i < _dispView.length; i++)
      if (_dispView[i] != null)
        $[i] = _dispView[i].getId();
    return $;
  }
  
  /**
   * 
   * @param pos
   *          item position in the list
   * @return true if the item currently displayed.
   */
  public boolean isItemDisplayed(final int pos) {
    if (pos < _page * _rows * _cols || pos >= _page * _rows * _cols + _rows * _cols)
      return false;
    return true;
  }
  
  /**
   * 
   * @return current page number
   */
  public int getPage() {
    return _page;
  }
}
