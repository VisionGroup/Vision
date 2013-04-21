package com.yp2012g4.vision.customUI.lists;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import com.yp2012g4.vision.R;

/**
 * Dynamically create static list container. Usage as regular ListView with
 * Adopter.
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

	private Adapter _adapter;
	private boolean init = false;

	private Context _context;
	private AttributeSet _attrs;

	public TalkingListView(Context context) {
		super(context);
		_context = context;
	}

	private void getAttr(Context context, AttributeSet attrs) {
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TalkingListView, 0, 0);

		try {
			_rows = a
					.getInteger(R.styleable.TalkingListView_Rows, DEFAULT_ROWS);
			_cols = a
					.getInteger(R.styleable.TalkingListView_Cols, DEFAULT_COLS);
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

		WindowManager wm = (WindowManager) _context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		int w = 0;
		if (dm != null) {
			w = dm.widthPixels / _cols;
		}

		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				int itemId = _cols * i + j;
				View v = _adapter.getView(itemId, null, this);

				LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				params.width = w;
				if (i == 0 && j == 0) {
					params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
				} else if (j != 0) {
					params.addRule(RelativeLayout.RIGHT_OF, itemId);
					params.addRule(RelativeLayout.BELOW, itemId - _cols +1);
				} else {
					params.addRule(RelativeLayout.BELOW, itemId - _cols +1 );
				}
				v.setId(itemId+1);
				this.addView(v, params);
			}

		}

	}

	/**
	 * create dynamic view.
	 */
	private void setPage(int num) {

		if (_adapter == null) {
			return;
		}

		if (init == false) {
			initView();
			init = true;
			return;
		}

		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				int itemId = _page * _rows * _cols + _cols * i + j;
				int replacingId = _cols * i + j;
				// replacing the old view fields no need to add it to _view.
				_adapter.getView(itemId, this.findViewById(replacingId), this);
			}
		}

	}

	public TalkingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		_attrs = attrs;
		getAttr(context, attrs);

	}

	public TalkingListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		_attrs = attrs;
		getAttr(context, attrs);
	}

	/**
	 * @return the cols
	 */
	public int getCols() {
		return _cols;
	}

	/**
	 * @param cols
	 *            the cols to set
	 */
	public void setCols(int cols) {
		_cols = cols;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return _rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(int rows) {
		_rows = rows;
	}

	/**
	 * 
	 * @param adapter
	 *            the adapter to set
	 */
	public void setAdapter(Adapter adapter) {
		_adapter = adapter;
		setPage(_page);
	}

	public void nextPage() {
		_page++;
		setPage(_page);
		// ???????????
		this.invalidate();
	}

	public void prevPage() {
		_page--;
		setPage(_page);
		// ???????????
		this.invalidate();
	}

}
