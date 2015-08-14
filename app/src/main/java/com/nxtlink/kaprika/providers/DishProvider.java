package com.nxtlink.kaprika.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.nxtlink.kaprika.db.DbHelper;

public class DishProvider extends ContentProvider {
	private DbHelper openHelper;
	public static final String AUTHORITY = "ourContentProviderAuthorities";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	@Override
	public boolean onCreate() {
		openHelper = new DbHelper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String table = getTableName(uri);
		SQLiteDatabase dataBase = openHelper.getWritableDatabase();
		return dataBase.delete(table, selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = getTableName(uri);
		SQLiteDatabase dataBase = openHelper.getWritableDatabase();
		long value = dataBase.insert(table, null, values);
		return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		String table = getTableName(uri);
		SQLiteDatabase database = openHelper.getReadableDatabase();
		if (table.equals(DbHelper.TABLE_CATEGORIES_JOIN_DISHES)){
			 SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		        String tables = String.format("%s INNER JOIN %s ON (%s.%s = %s.%s)", DbHelper.TABLE_NAME_DISHES,
		                DbHelper.TABLE_NAME_RELATED_CATEGORIES, DbHelper.TABLE_NAME_DISHES, DbHelper.DISH_ID,
		                DbHelper.TABLE_NAME_RELATED_CATEGORIES, DbHelper.RELATION_CAT_DISH_ID);
		        builder.setTables(tables);
		        String sql = builder.buildQuery(null, selection, null, null, DbHelper.DISH_NAME + " desc", null);
		        return database.rawQuery(sql, selectionArgs);
		} else {
			Cursor cursor = database.query(table, projection, selection, selectionArgs, null, null, sortOrder);
			return cursor;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String table = getTableName(uri);
		SQLiteDatabase database = openHelper.getWritableDatabase();
		return database.update(table, values, selection, selectionArgs);
	}

	public static String getTableName(Uri uri) {
		String value = uri.getPath();
		value = value.replace("/", "");// we need to remove '/'
		return value;
	}
}
