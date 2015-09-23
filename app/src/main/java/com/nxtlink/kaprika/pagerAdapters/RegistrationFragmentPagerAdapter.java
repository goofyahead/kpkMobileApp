package com.nxtlink.kaprika.pagerAdapters;







import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedList;

/**
 * Created by goofyahead on 16/09/15.
 */
public class RegistrationFragmentPagerAdapter extends FragmentPagerAdapter {

    private LinkedList<Fragment> fragments;

    public RegistrationFragmentPagerAdapter(FragmentManager fm, LinkedList<Fragment> elements) {
        super(fm);
        this.fragments = elements;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
