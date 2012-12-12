package com.yp2012g4.blindroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Rect;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;

public class onTouchEventClass extends Activity implements OnTouchListener,
	TextToSpeech.OnInitListener {
    protected Rect rect;
    protected TextToSpeech tts;
    protected Button tool_tip;
    protected Button home_screen;
    protected Button help;
    protected Button back;
    // protected Rect[] buttonsArray;
    // protected Map<Rect, Button> rb = new HashMap<Rect, Button>();
    protected List<Button> list_of_buttons = new ArrayList<Button>();
    // protected List<Rect> list_of_rects = new ArrayList<Rect>();

    protected List<Float> list_of_x_coords = new ArrayList<Float>();
    protected List<Float> list_of_y_coords = new ArrayList<Float>();
    protected List<Float> list_of_widths = new ArrayList<Float>();
    protected List<Float> list_of_heights = new ArrayList<Float>();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
	// View curr_view = v;

	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    // Construct a rect of the view's bounds
	    rect = new Rect(((Button) v).getLeft(), ((Button) v).getTop(),
		    ((Button) v).getRight(), ((Button) v).getBottom());
	    // Log.i("MyLog" , rect)
	    speakOut(((Button) v).getText().toString());
	    // Log.i("MyLog", "position of: " + ((Button)
	    // v).getText().toString()
	    // + " left = " + getRelativeLeft(v) + " ------ top = "
	    // + getRelativeTop(v) + " -------  width = " + v.getWidth()
	    // + " ------ height = " + v.getHeight());
	    // accurateX = getRelativeLeft(v) + event.getX();
	    // accurateY = getRelativeTop(v) + event.getY();

	}
	if (event.getAction() == MotionEvent.ACTION_MOVE) {
	    float accurateX = getRelativeLeft(v) + event.getX();
	    float accurateY = getRelativeTop(v) + event.getY();
	    // View new_view = getView(accurateX, accurateY);
	    // if (new_view != curr_view){
	    // curr_view.setPressed(false);
	    // new_view.setPressed(true);
	    // }

	    for (int i = 0; i < list_of_buttons.size(); i++)
		if (accurateX >= list_of_x_coords.get(i)
			&& accurateX <= list_of_x_coords.get(i)
				+ list_of_widths.get(i)
			&& accurateY >= list_of_y_coords.get(i)
			&& accurateY <= list_of_y_coords.get(i)
				+ list_of_heights.get(i)) {

		    Log.i("MyLog", "moved to Button: "
			    + list_of_buttons.get(i).getText().toString());
		    speakOut(list_of_buttons.get(i).getText().toString());
		    // list_of_buttons.get(i).setPressed(true);
		}

	}

	return false;
    }

    public void speakOut(String s) {
	tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
	if (tts != null) {
	    speakOut("stop");
	    tts.stop();
	    tts.shutdown();
	}
	super.onDestroy();
    }

    @Override
    public void onInit(int status) {
	if (status == TextToSpeech.SUCCESS) {
	    int r = tts.setLanguage(Locale.US);
	    if (r == TextToSpeech.LANG_NOT_SUPPORTED
		    || r == TextToSpeech.LANG_MISSING_DATA) {
		Log.e("tts", "error setLanguage");
		return;
	    }
	    speakOut("start");
	    return;
	}
	Log.e("tts", "error init language");

    }

    public void getButtonsPosition(View v) {
	// View v = (View)ll;
	if (v instanceof Button) {
	    list_of_x_coords.add(getRelativeLeft(v));
	    list_of_y_coords.add(getRelativeTop(v));
	    list_of_widths.add(getRelativeLeft(v) + v.getWidth());
	    list_of_heights.add(getRelativeTop(v) + v.getHeight());
	    list_of_buttons.add((Button) v);
	    return;
	}
	ViewGroup vg = (ViewGroup) v;
	for (int i = 0; i < vg.getChildCount(); i++)
	    getButtonsPosition(vg.getChildAt(i));

	/*
	 * try {
	 * 
	 * DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	 * DocumentBuilder db;
	 * 
	 * db = dbf.newDocumentBuilder();
	 * 
	 * Document doc = db.parse(is); Log.i("MyLog" ,
	 * "ENTERED!!!!!!!!!!!!!!");
	 * 
	 * doc.getDocumentElement().normalize(); NodeList nodeLst =
	 * doc.getElementsByTagName("Button"); Log.i("MyLog" ,
	 * "-----size------ = " + nodeLst.getLength()); for (int i = 0; i <
	 * nodeLst.getLength(); i++) { Log.i("MyLog" ,
	 * nodeLst.item(i).toString());
	 * list_of_x_coords.add(getRelativeLeft((Button) nodeLst.item(i)));
	 * list_of_y_coords.add(getRelativeTop((Button) nodeLst.item(i)));
	 * list_of_widths.add(getRelativeLeft((Button) nodeLst.item(i)) +
	 * ((Button) nodeLst.item(i)).getWidth());
	 * list_of_heights.add(getRelativeTop((Button) nodeLst.item(i)) +
	 * ((Button) nodeLst.item(i)).getHeight()); list_of_buttons.add((Button)
	 * nodeLst.item(i)); } } catch (Exception e) { e.printStackTrace(); }
	 */
	/*
	 * for (int i = 0; i < ll.getChildCount(); i++) { View view =
	 * ll.getChildAt(i);
	 * 
	 * // Log.i("Mylog", "LinearLayout" + view.toString() + "num " + i); for
	 * (int j = 0; j < ((LinearLayout) view).getChildCount(); j++) { View v
	 * = ((LinearLayout) view).getChildAt(j);
	 * 
	 * // int[] location = new int[2];
	 * 
	 * if (v instanceof Button) { Log.i("Mylog", "left position = " +
	 * getRelativeLeft(v) + " and top = " + getRelativeTop(v)); //
	 * rb.put(new Rect(v.getLeft(), v.getTop(), v.getRight(), v //
	 * .getBottom()), (Button) v); // v.getLocationOnScreen(location); //
	 * Log.i("MyLog", "location of " // + ((Button) v).getText().toString()
	 * + "is: " // + location[0] + " , " + location[1]);
	 * 
	 * list_of_x_coords.add(getRelativeLeft(v));
	 * list_of_y_coords.add(getRelativeTop(v));
	 * list_of_widths.add(getRelativeLeft(v) + v.getWidth());
	 * list_of_heights.add(getRelativeTop(v) + v.getHeight());
	 * list_of_buttons.add((Button) v);
	 * 
	 * // Rect rect = new Rect(); // ((Button)v).getLocalVisibleRect(rect);
	 * // Log.i("MyLog", "location of " // + ((Button)
	 * v).getText().toString() + "is: " // +rect.left + " , "+rect.right +
	 * " , "+rect.bottom + // " , "+rect.top + "and width = " + rect.width()
	 * + // "and height = " + rect.height());
	 * 
	 * // Rect newRect = new Rect(((Button) v).getLeft(), // ((Button)
	 * v).getTop(), ((Button) v).getRight(), // ((Button) v).getBottom());
	 * // Log.i("MyLog", ((Button) v).getText().toString() // +
	 * "location is: " + newRect.toShortString()); //
	 * list_of_rects.add(newRect);
	 * 
	 * } } } // for (Map.Entry<Rect, Button> entry : rb.entrySet()) { //
	 * Log.i("MyLog", "BLABLA  " + entry.getValue().getText().toString());
	 * // }
	 */return;
    }

    private float getRelativeLeft(View myView) {
	if (myView.getParent() == myView.getRootView())
	    return myView.getLeft();
	else
	    return (myView.getLeft() + getRelativeLeft((View) myView
		    .getParent()));
    }

    private float getRelativeTop(View myView) {
	if (myView.getParent() == myView.getRootView())
	    return myView.getTop();
	else
	    return (myView.getTop() + getRelativeTop((View) myView.getParent()));
    }

    /*
     * private View getView(float x, float y) { for (int i = 0; i <
     * list_of_buttons.size(); i++) {
     * 
     * if (x >= list_of_x_coords.get(i) && x <= list_of_x_coords.get(i) +
     * list_of_widths.get(i) && y >= list_of_y_coords.get(i) && y <=
     * list_of_y_coords.get(i) + list_of_heights.get(i)) {
     * 
     * return list_of_buttons.get(i); } } return null; }
     */

    /*
     * private void buttonsPicker(ViewGroup v, Document doc) { XPath xpath =
     * XPathFactory.newInstance().newXPath(); XPathExpression expr = null; try {
     * expr = xpath.compile("//Button"); } catch (XPathExpressionException e) {
     * // TODO Auto-generated catch block e.printStackTrace(); } Object
     * exprResult = null; try { exprResult = expr.evaluate(doc,
     * XPathConstants.NODESET); } catch (XPathExpressionException e) { // TODO
     * Auto-generated catch block e.printStackTrace(); } NodeList nodeList =
     * (NodeList) exprResult; }
     */
}
