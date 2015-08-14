package com.nxtlink.kaprika.api;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nxtlink.kaprika.db.DataHelper;
import com.nxtlink.kaprika.interfaces.ProgressInterface;
import com.nxtlink.kaprika.models.Category;
import com.nxtlink.kaprika.models.Dish;
import com.nxtlink.kaprika.models.Ingredient;
import com.nxtlink.kaprika.models.Tag;
import com.nxtlink.kaprika.utils.Connectivity;

import android.util.Log;

import javax.inject.Inject;


public class ManagEatApi implements ManagEatApiInterface {

	private static final String TAG = ManagEatApi.class.getName();
	
	@Inject DataHelper dbHelper;

	private static String SERVER_ADDR = "http://54.246.122.90/";
	private static String API_URL = SERVER_ADDR + "api/";
	private static String SERVER_IMAGES = SERVER_ADDR + "images/";
	private static final String SERVER_VIDEOS = SERVER_ADDR + "videos/";
	private static String CURRENT_MENU = "currentMenu";
	private static String GET_MENU_URL = API_URL + CURRENT_MENU;
	private static String CATEGORIES = "categories";
	private static String GET_CATEGORIES_URL = API_URL + CATEGORIES;
	private static String TAGS = "tags";
	private static String GET_TAGS_URL = API_URL + TAGS;
	private static String INGREDIENTS = "ingredients";
	private static String GET_INGREDIENTS_URL = API_URL + INGREDIENTS;
	private static boolean firstUpdate = true;

	@Override
	public void updateCurrentMenu(ProgressInterface progress) {
		String menuList = null;
		JSONArray dishes = null;
		try {
			menuList = Connectivity.getDataFromUrl(GET_MENU_URL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			dishes = new JSONArray(menuList);
			int dishesLength = dishes.length();
			Integer[] values = new Integer [2];
			values[1] = dishesLength;
			for (int x = 0; x < dishesLength; x++) {
				values[0] = x;
				// notifiy every dish completed to progress
				progress.publish(values);
				Log.d(TAG, "current dish: " + dishes.getJSONObject(x).toString());
				JSONObject jsonDish = dishes.getJSONObject(x);
//				Dish currentDish = new Dish(jsonDish.getString("_id"),
//						jsonDish.getString("name"),
//						jsonDish.getString("description"),
//						(float) jsonDish.getDouble("price"),
//						jsonDish.getString("picture"),
//						jsonDish.getString("video"),
//						jsonDish.getBoolean("demo"));
				// save relations
//				JSONArray relations = jsonDish.getJSONArray("recommendations");
//				for (int y = 0; y < relations.length(); y ++){
//					dbHelper.saveRelation(currentDish.getId(), relations.getJSONObject(y).getString("_id"));
//				}
//				// save categories
//				JSONArray categories = jsonDish.getJSONArray("categories");
//				for (int y = 0; y < categories.length(); y ++){
//					dbHelper.saveRelatedCategory(currentDish.getId(), categories.getJSONObject(y).getString("_id"));
//				}
//				// save tags
//				JSONArray tags = jsonDish.getJSONArray("categories");
//				for (int y = 0; y < tags.length(); y ++){
//					dbHelper.saveRelatedTag(currentDish.getId(), tags.getJSONObject(y).getString("_id"));
//				}
//				// save ingredients
//				JSONArray ingredients = jsonDish.getJSONArray("categories");
//				for (int y = 0; y < ingredients.length(); y ++){
//					dbHelper.saveRelatedIngredient(currentDish.getId(), ingredients.getJSONObject(y).getString("_id"));
//				}
//				// save to BD dish element
//				dbHelper.saveDish(currentDish);
//				//download video and images
//				Connectivity.getFileFromUrlAndSave(SERVER_IMAGES + currentDish.getImage(), currentDish.getImage());
//				Connectivity.getFileFromUrlAndSave(SERVER_VIDEOS + currentDish.getVideo(), currentDish.getVideo());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "error generatin JSONarray: " + e.getMessage());
		}
	}

	@Override
	public void updateTags(ProgressInterface progress) {
		String tagList = null;
		JSONArray tags = null;
		try {
			tagList = Connectivity.getDataFromUrl(GET_TAGS_URL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			tags = new JSONArray(tagList);
			int tagsLength = tags.length();
			Integer[] values = new Integer [2];
			values[1] = tagsLength;
			for (int x = 0; x < tagsLength; x++) {
				Log.d(TAG, "current tAg: " + tags.getJSONObject(x).toString());
				// save to BD
				values[0] = x;
				// notifiy every dish completed to progress
				progress.publish(values);
				Log.d(TAG, "current dish: " + tags.getJSONObject(x).toString());
				JSONObject jsonTag = tags.getJSONObject(x);
				Tag currentTag = new Tag(jsonTag.getString("_id"),
						jsonTag.getString("name"),
						jsonTag.getString("description"));
				// save to BD dish element
				dbHelper.saveTag(currentTag);
				//download video and images
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "error generatin JSONarray: " + e.getMessage());
		}

	}

	@Override
	public void updateCategories(ProgressInterface progress) {
		String categoriesList = null;
		JSONArray categories = null;
		try {
			categoriesList = Connectivity.getDataFromUrl(GET_CATEGORIES_URL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			categories = new JSONArray(categoriesList);
			int catsLength = categories.length();
			Integer[] values = new Integer [2];
			values[1] = catsLength;
			for (int x = 0; x < catsLength; x++) {
				Log.d(TAG, "current tag: " + categories.getJSONObject(x).toString());
				// save to BD
				values[0] = x;
				// notifiy every dish completed to progress
				progress.publish(values);
				JSONObject jsonTag = categories.getJSONObject(x);
				Category currentCategory = new Category(jsonTag.getString("_id"),
						jsonTag.getString("name"),
						jsonTag.getString("description"));
				// save to BD dish element
				dbHelper.saveCategory(currentCategory);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "error generatin JSONarray: " + e.getMessage());
		}
	}

	@Override
	public void updateIngredients(ProgressInterface progress) {
		String ingredientsList = null;
		JSONArray ingredients = null;
		try {
			ingredientsList = Connectivity.getDataFromUrl(GET_INGREDIENTS_URL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			ingredients = new JSONArray(ingredientsList);
			int catsLength = ingredients.length();
			Integer[] values = new Integer [2];
			values[1] = catsLength;
			for (int x = 0; x < catsLength; x++) {
				Log.d(TAG, "current tag: " + ingredients.getJSONObject(x).toString());
				// save to BD
				values[0] = x;
				// notifiy every dish completed to progress
				progress.publish(values);
				JSONObject jsonTag = ingredients.getJSONObject(x);
				Ingredient currentIngredient = new Ingredient(jsonTag.getString("_id"),
						jsonTag.getString("name"),
						jsonTag.getString("description"));
				// save to BD dish element
				dbHelper.saveIngredient(currentIngredient);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "error generatin JSONarray: " + e.getMessage());
		}
	}

	@Override
	public boolean checkForUpdates() {
		if (firstUpdate) {
			firstUpdate = false;
			return true;
		} else {
			return false;
		}
		
	}
}
