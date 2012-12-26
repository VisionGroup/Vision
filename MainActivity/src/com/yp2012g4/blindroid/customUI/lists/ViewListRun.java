package com.yp2012g4.blindroid.customUI.lists;


/**
 * interface for Talking List View.
 * 
 * @author Roman
 * 
 */
public interface ViewListRun {
	
	public void onClick(int selectedItem);

  public void onInitSpeak(int selectedItem);

  public void onFling(int selectedItem);
	
}
