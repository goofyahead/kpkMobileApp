package kpklib.db;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.LinkedList;

import javax.inject.Inject;

import kpklib.constants.Credentials;
import kpklib.interfaces.Injector;
import kpklib.models.Category;
import kpklib.models.Dish;
import kpklib.models.Ingredient;
import kpklib.models.Recommendation;
import kpklib.models.Tag;

public class DataHelper {

	private static final String TAG = DataHelper.class.getName();
	private DbHelper openHelper;
    private Context mContext;
    @Inject
    DownloadManager dm;


    public DataHelper(Context context) {
        mContext = context;
        ((Injector) context.getApplicationContext()).inject(this);
        openHelper = new DbHelper(context);
	}

	public void deleteDB () {
		boolean status = openHelper.deleteDB();

        Log.d(TAG, "database deleted: " + status);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Credentials.FOLDER_KPK_PICTURES);
        File [] pictureFiles = path.listFiles();
        LinkedList<String> filesPath = new LinkedList<>();

        if (pictureFiles != null) {
            for (int x = 0; x < pictureFiles.length; x++) {
                Log.d(TAG, "file deleted: " + pictureFiles[x].getName());
                pictureFiles[x].delete();
                filesPath.add(pictureFiles[x].getAbsolutePath());
            }
        }

        File pathVideos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + Credentials.FOLDER_KPK_VIDEOS);
        File [] videoFiles = pathVideos.listFiles();

        if (videoFiles != null) {
            for (int x = 0; x < videoFiles.length; x++){
                Log.d(TAG, "file deleted: " + videoFiles[x].getName());
                videoFiles[x].delete();
                filesPath.add(videoFiles[x].getAbsolutePath());
            }
        }

        MediaScannerConnection.scanFile(mContext, filesPath.toArray(new String[filesPath.size()]), null, null);
	}

	public void saveDish(Dish dish) {
		SQLiteDatabase db = openHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DbHelper.DISH_ID, dish.getId());
		values.put(DbHelper.DISH_NAME, dish.getName());
		values.put(DbHelper.DISH_DESCRIPTION, dish.getDescription());
		values.put(DbHelper.DISH_IMAGE, dish.getImage());
		values.put(DbHelper.DISH_VIDEO, dish.getVideo());
		values.put(DbHelper.DISH_PRICE, dish.getPrice());
		values.put(DbHelper.DISH_DEMO, dish.isDemo());
		db.insert(DbHelper.TABLE_NAME_DISHES, null, values);

		db.close();

        DownloadManager.Request requestPicture = new DownloadManager.Request(Uri.parse(Credentials.SERVER_IP + Credentials.IMAGES_PATH + dish.getImage()));
        requestPicture.setVisibleInDownloadsUi(false);
        requestPicture.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, Credentials.FOLDER_KPK_PICTURES + dish.getImage());
        requestPicture.allowScanningByMediaScanner();
        dm.enqueue(requestPicture);

        DownloadManager.Request requestVideo = new DownloadManager.Request(Uri.parse(Credentials.SERVER_IP + Credentials.VIDEOS_PATH + dish.getVideo()));
        requestVideo.setVisibleInDownloadsUi(false);
        requestVideo.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, Credentials.FOLDER_KPK_VIDEOS + dish.getVideo());
        requestVideo.allowScanningByMediaScanner();
        dm.enqueue(requestVideo);

        /// save recommendations
		for (Recommendation recommendation : dish.getRecommendations()){
            saveRelation(dish.getId(), recommendation.getId());
        }

        // save categories
        for (Category category : dish.getCategories()) {
            saveRelatedCategory(dish.getId(), category.getId());
        }

        // save tags
        for (Tag tag : dish.getTags()) {
            saveRelatedTag(dish.getId(), tag.getId());
        }

        // save ingredients
        for (Ingredient ingredient : dish.getIngredients()) {
            saveRelatedIngredient(dish.getId(), ingredient.getId());
        }
	}

	public LinkedList<Category> getCategories() {
		LinkedList<Category> categories = new LinkedList<>();

		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.query(DbHelper.TABLE_CATEGORIES_NAME, null, null, null, null, null, DbHelper.CATEGORY_NAME + " ASC");
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(DbHelper.CATEGORY_NAME));
			String description = cursor.getString(cursor.getColumnIndex(DbHelper.CATEGORY_DESCRIPTION));
			String id = cursor.getString(cursor.getColumnIndex(DbHelper.CATEGORY_ID));
			categories.add(new Category(id, name, description));
		}
		cursor.close();

        db.close();

		return categories;
	}

	public void saveTag(Tag currentTag) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbHelper.TAG_ID, currentTag.getId());
		values.put(DbHelper.TAG_NAME, currentTag.getName());
		values.put(DbHelper.TAG_DESCRIPTION, currentTag.getDescription());
		db.insert(DbHelper.TABLE_TAGS_NAME, null, values);
		db.close();
	}

	public void saveCategory(Category currentCategory) {
		SQLiteDatabase db = openHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DbHelper.CATEGORY_ID, currentCategory.getId());
		values.put(DbHelper.CATEGORY_NAME, currentCategory.getName());
		values.put(DbHelper.CATEGORY_DESCRIPTION,
				currentCategory.getDescription());
		db.insert(DbHelper.TABLE_CATEGORIES_NAME, null, values);

		db.close();
	}

	public void saveIngredient(Ingredient currentIngredient) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbHelper.INGREDIENT_ID, currentIngredient.getId());
		values.put(DbHelper.INGREDIENT_NAME, currentIngredient.getName());
		values.put(DbHelper.INGREDIENT_DESCRIPTION,
				currentIngredient.getDescription());
		db.insert(DbHelper.TABLE_INGREDIENTS_NAME, null, values);
		db.close();
	}

	public void saveRelation(String id, String related) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbHelper.RELATION_ID_A, id);
		values.put(DbHelper.RELATION_ID_B, related);
		db.insert(DbHelper.TABLE_NAME_RELATIONS, null, values);		
		db.close();
	}

	public void saveRelatedCategory(String id, String categoryId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbHelper.RELATION_CAT_DISH_ID, id);
		values.put(DbHelper.RELATION_CAT_ID, categoryId);
		db.insert(DbHelper.TABLE_NAME_RELATED_CATEGORIES, null, values);
		db.close();
	}

	public void saveRelatedTag(String id, String tagId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbHelper.RELATION_TAG_DISH_ID, id);
		values.put(DbHelper.RELATION_TAG_ID, tagId);
		db.insert(DbHelper.TABLE_NAME_RELATED_TAGS, null, values);
		db.close();
	}

	public void saveRelatedIngredient(String id, String ingredientId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbHelper.RELATION_INGREDIENT_DISH_ID, id);
		values.put(DbHelper.RELATION_INGREDIENT_ID, ingredientId);
		db.insert(DbHelper.TABLE_NAME_RELATED_INGREDIENTS, null, values);
		db.close();
	}
	
	public Cursor getDishCursor () {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		return db.query(DbHelper.TABLE_NAME_DISHES, null, null, null, null, null, DbHelper.DISH_NAME + " ASC");
	}
	
	public Cursor getDishCursor (String selection, String args) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		final String SELECTION = selection + "=?";
	    final String[] SELECTION_ARGS = {args};
		return db.query(DbHelper.TABLE_NAME_DISHES, null, SELECTION, SELECTION_ARGS, null, null, DbHelper.DISH_NAME + " ASC");
	}

	public LinkedList<Dish> getDemoDishes(){
		Cursor cursor = getDishCursor(DbHelper.DISH_DEMO, "1");
		LinkedList<Dish> retrieved = new LinkedList<>();
		while(cursor.moveToNext()) {
			String video = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_VIDEO));
			String name = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_NAME));
			float price = cursor.getFloat(cursor.getColumnIndex(DbHelper.DISH_PRICE));
			String description = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_DESCRIPTION));
			String id = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_ID));
			String image = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_IMAGE));
			boolean demo = cursor.getInt(cursor.getColumnIndex(DbHelper.DISH_DEMO)) == 0 ? false : true;
			retrieved.add(new Dish(id, name, description, price, image, video, demo, null, null, null, null));
		}
		return retrieved;
	}

	public Dish getDishById (String id) {
		return getDishFromCursor(getDishCursor(DbHelper.DISH_ID, id));
	}
	
	private Dish getDishFromCursor (Cursor cursor) {
		Dish searchedDish = null;
		 if (cursor.moveToNext()) {
			 String video = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_VIDEO));
			 String name = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_NAME));
			 float price = cursor.getFloat(cursor.getColumnIndex(DbHelper.DISH_PRICE));
			 String description = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_DESCRIPTION));
			 String id = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_ID));
			 String image = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_IMAGE));
			 boolean demo = cursor.getInt(cursor.getColumnIndex(DbHelper.DISH_DEMO)) == 0 ? false : true;
			 searchedDish = new Dish(id, name, description, price, image, video, demo, null, null, null, null);
		 }
		cursor.close();
		return searchedDish;
	}


}
