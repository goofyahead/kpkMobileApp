package com.nxtlink.kaprika.api;
import com.nxtlink.kaprika.interfaces.ProgressInterface;

public interface ManagEatApiInterface {

	/**
	 * Updates current menu from server
	 * @param progress 
	 */
	void updateCurrentMenu(ProgressInterface progress);
	
	void updateTags(ProgressInterface progress);
	
	void updateCategories(ProgressInterface progress);
	
	void updateIngredients(ProgressInterface progress);
	
	boolean checkForUpdates();
}
