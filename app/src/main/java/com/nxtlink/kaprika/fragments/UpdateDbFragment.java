package com.nxtlink.kaprika.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.base.KaprikaApplication;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import kpklib.api.KaprikaApiInterface;
import kpklib.db.DataHelper;
import kpklib.models.Dish;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by goofyahead on 13/08/15.
 */
public class UpdateDbFragment extends Fragment implements Callback<List<Dish>> {

    private static final String TAG = UpdateDbFragment.class.getName();
    @Inject
    KaprikaApiInterface api;
    @Inject
    DataHelper dataHelper;

    public static UpdateDbFragment newInstance(String dishId) {
        UpdateDbFragment myFragment = new UpdateDbFragment();

        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.update_db_fragment, container, false);
        ButterKnife.inject(this, v);
        ((KaprikaApplication) getActivity().getApplication()).inject(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // get menu when clicked on update.
        api.getCurrentMenu(this);
        // loading db
    }

    @Override
    public void success(List<Dish> dishes, Response response) {
        // db loaded X dishes, getting images and video progress
        Log.d(TAG, "saving dishes in db " + dishes.size());
        for (Dish dish : dishes) {
            dataHelper.saveDish(dish);
        }

        Log.d(TAG, "Dishes saved, getting images");
    }

    @Override
    public void failure(RetrofitError error) {

    }
}
