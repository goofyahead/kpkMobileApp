package com.nxtlink.kaprika.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.adapters.DishCursorAdapter;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.interfaces.AddToCart;

import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.db.DataHelper;
import kpklib.db.DbHelper;
import kpklib.models.Category;
import kpklib.providers.DishProvider;

public class DishSearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	@InjectView(R.id.dish_search_list)
	ListView dishListView;
    @InjectView(R.id.search_edit_text)
    EditText searchField;


	private OnDishSelectedListener mCallback;
    private AddToCart mCartCallback;
	private DishCursorAdapter dishCursorAdapter;
	private String categoryId;
	public static final String TAG = DishSearchFragment.class.getName();
	private static final int URL_LOADER = 0;
	private static final String CATEGORY_ID = "category_id";
    private String filter = "";

	// Container Activity must implement this interface
	public interface OnDishSelectedListener {
		void onDishSelected(String id);
	}
	
	public static DishSearchFragment newInstance() {
		DishSearchFragment myFragment = new DishSearchFragment();
		Bundle args = new Bundle();
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
//		try {
//			mCallback = (OnDishSelectedListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnHeadlineSelectedListener");
//		}

		try {
            mCartCallback = (AddToCart) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement AddToCart");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.search_dishes_fragment, container, false);
		ButterKnife.inject(this, v);
		((KaprikaApplication) getActivity().getApplication()).inject(this);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();


        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    filter = s.toString();
                    getLoaderManager().restartLoader(0, null, DishSearchFragment.this);
                }
            }
        });
//		dishListView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View arg1,
//                                    int position, long arg3) {
//                String dishId = (String) adapter.getItemAtPosition(position);
////                mCallback.onDishSelected(dishId); give callback to adapter an it will handle activity callback load fragment
//            }
//        });

	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
		categoryId = getArguments().getString(CATEGORY_ID);
		Log.d(TAG, "category id " + categoryId);

		Uri contentUri = Uri.withAppendedPath(DishProvider.CONTENT_URI, DbHelper.TABLE_NAME_DISHES);

        String[] args = new String[1];
        if (filter.length() > 0) {
            args[0] = "%" + filter + "%";
        } else {
            args[0] = "";
        }

		return new CursorLoader(getActivity(), contentUri, null, DbHelper.DISH_NAME + " LIKE ?", args, DbHelper.DISH_NAME);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		dishCursorAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
        Log.d(TAG, "restart loader");
		dishCursorAdapter.changeCursor(null);
	}
}
