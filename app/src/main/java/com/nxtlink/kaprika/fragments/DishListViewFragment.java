package com.nxtlink.kaprika.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.adapters.DishCursorAdapter;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.db.DbHelper;
import com.nxtlink.kaprika.interfaces.AddToCart;
import com.nxtlink.kaprika.providers.DishProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DishListViewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	@InjectView(R.id.dishGridView)
	ListView dishListView;
	private OnDishSelectedListener mCallback;
    private AddToCart mCartCallback;
	private DishCursorAdapter dishCursorAdapter;
	private String categoryId;
	public static final String TAG = DishListViewFragment.class.getName();
	private static final int URL_LOADER = 0;
	private static final String CATEGORY_ID = "category_id";

	// Container Activity must implement this interface
	public interface OnDishSelectedListener {
		void onDishSelected(String id);
	}
	
	public static DishListViewFragment newInstance(String categoryId) {
		DishListViewFragment myFragment = new DishListViewFragment();
		Bundle args = new Bundle();
		args.putString(CATEGORY_ID, categoryId);
		myFragment.setArguments(args);
		return myFragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		dishCursorAdapter = new DishCursorAdapter(getActivity(), (AddToCart) getActivity());
		dishListView.setAdapter(dishCursorAdapter);
		getLoaderManager().initLoader(URL_LOADER, null, this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnDishSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}

		try {
            mCartCallback = (AddToCart) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement AddToCart");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dish_grid_view, container, false);
		ButterKnife.inject(this, v);
		((KaprikaApplication) getActivity().getApplication()).inject(this);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		dishListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1,
                                    int position, long arg3) {
                String dishId = (String) adapter.getItemAtPosition(position);
                mCallback.onDishSelected(dishId);
            }
        });

        Log.d(TAG, "COUNT IS WHEN LIST IS LOADED: " + getFragmentManager().getBackStackEntryCount());
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		categoryId = getArguments().getString(CATEGORY_ID);
		Log.d(TAG, "category id " + categoryId);
		Uri contentUri = Uri.withAppendedPath(DishProvider.CONTENT_URI, DbHelper.TABLE_CATEGORIES_JOIN_DISHES);
		String[] args = { categoryId };
		return new CursorLoader(getActivity(), contentUri, null, DbHelper.TABLE_NAME_RELATED_CATEGORIES + "." +
		DbHelper.RELATION_CAT_ID + "=?", args, DbHelper.DISH_NAME);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		dishCursorAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		dishCursorAdapter.changeCursor(null);
	}
}
