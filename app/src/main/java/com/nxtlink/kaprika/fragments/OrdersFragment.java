package com.nxtlink.kaprika.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.base.KaprikaApplication;
import com.nxtlink.kaprika.pagerAdapters.TabPagerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by goofyahead on 10/26/15.
 */
public class OrdersFragment extends Fragment {

    @InjectView(R.id.tab_layout)
    TabLayout tabLayout;
    @InjectView(R.id.pager)
    ViewPager viewPager;

    public static OrdersFragment newInstance() {
        OrdersFragment myFragment = new OrdersFragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.orders_fragment_layout, container, false);
        ButterKnife.inject(this, v);
        ((KaprikaApplication) getActivity().getApplication()).inject(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.orders_active)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.orders_history)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.orders_fav)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final PagerAdapter adapter = new TabPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
