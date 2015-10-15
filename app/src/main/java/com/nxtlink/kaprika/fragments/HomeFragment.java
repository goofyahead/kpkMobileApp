package com.nxtlink.kaprika.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.pagerAdapters.CarrouselAdapter;

import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.db.DataHelper;
import kpklib.models.Dish;

public class HomeFragment extends Fragment {

    @InjectView(R.id.featured_slider)
    ViewPager featured;
    @Inject
    DataHelper dbHelper;

    private LinkedList<Dish> featuredDishes = new LinkedList<>();

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        featuredDishes = dbHelper.getDemoDishes();
        featured.setAdapter(new CarrouselAdapter(featuredDishes, getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, v);
        ((KaprikaApplication) getActivity().getApplication()).inject(this);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
