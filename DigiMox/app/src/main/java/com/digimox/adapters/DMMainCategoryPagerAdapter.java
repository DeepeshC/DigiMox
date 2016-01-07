package com.digimox.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepesh on 10-Dec-15.
 */
public class DMMainCategoryPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;

    public DMMainCategoryPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<Fragment>();
    }

    public void addFragment(int position, Fragment fragment) {
        mFragmentList.add(position, fragment);
    }

    public Fragment getFragment(int position) {
        return mFragmentList.get(position);
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
