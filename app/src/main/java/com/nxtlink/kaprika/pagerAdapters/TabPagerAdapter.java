package com.nxtlink.kaprika.pagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.nxtlink.kaprika.fragments.AddressFragment;
import com.nxtlink.kaprika.fragments.OrderListFragment;

/**
 * Created by goofyahead on 10/26/15.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public TabPagerAdapter (FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OrderListFragment tab1 = OrderListFragment.newInstance(OrderListFragment.ACTIVE);
                return tab1;
            case 1:
                OrderListFragment tab2 = OrderListFragment.newInstance(OrderListFragment.HISTORY);
                return tab2;
            case 2:
                OrderListFragment tab3 = OrderListFragment.newInstance(OrderListFragment.FAV);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
