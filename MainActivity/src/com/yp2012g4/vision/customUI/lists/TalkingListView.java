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

	private int _h = 0;
	private int _w = 0;

	private Adapter _adapter;
	private boolean _init = false;
	private boolean _initDisp = false;

	private Context _context;

	public TalkingListView(Context context) {
		super(context);
		_context = context;
	}

	private void getAttr(Context context, AttributeSet attrs) {
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TalkingListView, 0, 0);

		try {
			_rows = a
					.getInteger(R.styleable.TalkingListView_rows, DEFAULT_ROWS);
			_cols = a
					.getInteger(R.styleable.TalkingListView_cols, DEFAULT_COLS);
		} finally {
			a.recycle();
		}
	}

	/**
	 * Initiate the layout, create initial view with the adopter. View ID will
	 * be set here once and be reused in setPage.
	 */
	private void initView() {

		assert (_page == 0);

		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				int itemId = _cols * i + j;
				View v = _adapter.getView(itemId, null, this);
				if (v == null) {
					return;
				}

				LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				if (_h != 0 && _w != 0) {
					params.height = _h / _rows;
					params.width = _w / _cols;
					_initDisp = true;
				}

				if (i == 0 && j == 0) {
					params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
							RelativeLayout.TRUE);
				} else if (j != 0) {
					params.addRule(RelativeLayout.RIGHT_OF, itemId);
					params.addRule(RelativeLayout.BELOW, itemId - _cols + 1);
				} else {
					params.addRule(RelativeLayout.BELOW, itemId - _cols + 1);
				}
				v.setId(itemId + 1);
				this.addView(v, params);
			}

		}

	}

	/**
	 * create dynamic view.
	 * 
	 * @param
	 */
	private void setPage(int delta) {

		if (_adapter == null) {
			return;
		}

		if (_init == false) {
			 initView();
			_init = true;
			return;
		}

		if (_page + delta < 0)
			return;
		if (_page + delta >= _numOfPages)
			return;

		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				int itemId = (_page + delta) * _rows * _cols + _cols * i + j;
				int replacingId = _page * _rows * _cols + _cols * i + j;
				// replacing the old view fields no need to add it to _view.
				View v = _adapter.getView(itemId,
						this.findViewById(replacingId + 1), this);
				if (v != null) {
					this.findViewById(replacingId + 1).setId(itemId + 1);
				}
			}
		}

		_page += delta;
	}

	public TalkingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		getAttr(context, attrs);
		initDisplayParams();
	}

	public TalkingListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		getAttr(context, attrs);
		initDisplayParams();
	}

	/**
	 * get layout display information. If the data exists means that list layout
	 * hight and width is hard coded.
	 */
	private void initDisplayParams() {

		_h = getHeight();
		_w = getWidth();

		if (_h != 0 && _w != 0) {
			_initDisp = true;
		}
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
	 * @param adapter
	 *            the adapter to set
	 */
	public void setAdapter(Adapter adapter) {
		_adapter = adapter;
		int mod = _adapter.getCount() / (_cols * _rows);
		if (_adapter.getCount() % (_cols * _rows) != 0) {
			_numOfPages = mod + 1;
		} else {
			_numOfPages = mod;
		}
		setPage(0);
	}

	/**
	 * sets the next page to be displayed on the screen
	 */
	public void nextPage() {
		setPage(1);
		//this.invalidate();
	}

	/**
	 * sets the previous page to be displayed on the screen
	 */
	public void prevPage() {
		setPage(-1);
		//this.invalidate();
	}

	/**
	 * 
	 * @return currently displayed items.
	 */
	public long[] getDisplayedItemIds() {
		long[] ids = new long[_rows * _cols];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = _page * _rows * _cols + i;
		}

		return ids;
	}

	/**
	 * 
	 * @param position
	 *            item position in the list
	 * @return true if the item currently displayed.
	 */
	public boolean isItemDisplayed(int position) {
		if (position < _page * _rows * _cols)
			return false;
		if (position >= _page * _rows * _cols + _rows * _cols)
			return false;
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		// at this point the height and width should be known.
		super.onLayout(changed, l, t, r, b);
		// initView();

		if (_initDisp != true) {
			int h = getHeight() / _rows;
			int w = getWidth() / _cols;

			for (int i = 1; i <= _rows * _cols; i++) {
				View v = this.findViewById(i);
				android.view.ViewGroup.LayoutParams lp = v.getLayoutParams();

				lp.height = h;
				lp.width = w;
				v.setLayoutParams(lp);
				v.invalidate();
				v.dispatchWindowFocusChanged(true);
			}
			_initDisp = true;

		}
		/**
		 * to update key's position
		 */
		VisionActivity host = (VisionActivity) this.getContext();
		host.getButtonsPosition(this);
	}

}
