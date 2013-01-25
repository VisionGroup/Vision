package com.yp2012g4.vision.customUI.lists;

/**
 * interface for Talking List View.
 * 
 * @author Roman
 * 
 */
public interface ViewListRun {
  /**
   * set your own behavior of onClick in TalkingListView
   * @param selectedItem
   */
  public void onClick(int selectedItem);
  
  /**
   * set your own behavior of Init in TalkingListView
   * @param selectedItem
   */
  public void onInitSpeak(int selectedItem);
  
  /**
   * set your own behavior of onFling in TalkingListView
   * @param selectedItem
   */
  public void onFling(int selectedItem);
}
