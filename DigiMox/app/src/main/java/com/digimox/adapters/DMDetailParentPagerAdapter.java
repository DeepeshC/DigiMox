package com.digimox.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepesh on 10-Dec-15.
 */
public class DMDetailParentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    public DMDetailParentPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<Fragment>();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
